var permission = new Vue({
    el: '#permission',
    data: {
        userid: 0,
        dialogTitle: "",
        dialogEnabled: false,
        data: []
    },
    methods: {
        loadPermission() {
            if (this.data.length > 0) return;
            loadData("get", "/oa/permission", {}, r => {
                let topTree = r.data.filter(o => o.pid == 0).map(m => {
                    return {id: m.id, pid: m.pid, label: m.name, children: []};
                }).forEach(e => this.data.push(e));
                this.fillTree(this.data, r.data);
                delete r.data;
            }, e => {
                this.$alert(e.message, '温馨提示');
            });
        },
        fillTree(right, data) {
            if (right.length == 0) return;
            right.forEach(p => {
                let tree = data.filter(o => o.pid == p.id).map(m => {
                    return {id: m.id, pid: m.pid, label: m.name, children: []};
                }).forEach(e => p.children.push(e));
                this.fillTree(p.children, data);
            })
        },
        onSubmit() {
            let checked = permission.$refs.treeviewer.getCheckedKeys();
            let unchecked = permission.$refs.treeviewer.getHalfCheckedKeys();
            let qualify = checked + (checked.length == 0 || unchecked.length == 0 ? "" : ",") + unchecked;
            loadData("post", "/oa/permission/staff/save", {id: this.userid, permission: qualify}, r => {
                if (r.code == 0) {
                    this.dialogEnabled = false;
                    this.$message.success(r.message);
                } else {
                    this.$message.error(r.message);
                }
            }, e => {
                this.$alert(e.message, '温馨提示');
            });
        }
    }
});
var vue = new Vue({
    el: '#app',
    data: {
        tableData: [],
        rowCount: 0,
        page: 1,
        status: "-1",
        searchType: "-1",
        searchText: "",
        staff: {},
        loading: false,
        dialogTitle: "",
        dialogEnabled: false,
        username_disabled: false,
        enums: ENUM,
        editQualify: false,
        permissionQualify: false
    },
    mounted: function () {
        loadData("get", "/login/qualify?url=/oa/staff/save", {}, r => {
            this.editQualify = r.data == true;
        }, null);
        loadData("get", "/login/qualify?url=/oa/permission/staff/save", {}, r => {
            this.permissionQualify = r.data == true;
        }, null);
    },
    methods: {
        toEdit(row) {
            this.dialogTitle = "修改帐号";
            this.username_disabled = true;
            this.staff = row;
            this.dialogEnabled = true;
        },
        toPermission(row) {
            permission.loadPermission();
            loadData("get", "/oa/permission/staff?userid=" + row.id, {}, r => {
                permission.dialogEnabled = true;
                permission.userid = row.id;
                permission.dialogTitle = "【" + row.alias + "】权限设置";
                setTimeout(() => {
                    permission.$refs.treeviewer.setCheckedKeys([]);
                    if (r.data) {
                        let items = [], data = r.data.split(",");
                        permission.data.forEach(o => this.getItems(o, items));
                        let keys = items.filter(o => data.find(t => t == o));
                        permission.$refs.treeviewer.setCheckedKeys(keys);
                    }
                }, 100);
            }, e => {
                this.$alert(e.message, '温馨提示');
            });
        },
        getItems(right, items) {
            if (right.children.length == 0) {
                items.push(right.id);
                return;
            } else {
                right.children.forEach(p => this.getItems(p, items));
            }
        },
        pageChanged(pageid) {
            this.page = pageid;
            this.search();
        },
        btnSearchClick() {
            this.pageChanged(1);
        },
        search() {
            this.loading = true;
            let url = "/oa/staff";
            url += "?page=" + this.page
                + "&value=" + encodeURIComponent(this.searchText)
                + "&status=" + this.status
                + "&key=" + this.searchType;
            loadData("get", url, {}, r => {
                r.data.forEach(o => o.password = "");
                this.tableData = r.data;
                this.rowCount = r.code;
                this.loading = false;
                delete r.data;
            }, e => {
                this.loading = false;
                this.$alert(e.message, '温馨提示');
            });
        },
        showInsertDialog() {
            this.dialogTitle = "添加帐号";
            this.username_disabled = false;
            this.dialogEnabled = true;
            this.staff = {
                id: 0,
                name: '',
                password: '',
                alias: '',
                avatar: '',
                type: null,
                platform: null,
                status: null
            };
        },
        onCancel(formName) {
            this.dialogEnabled = false;
            this.search();
        },
        onSubmit(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.staff.password = this.staff.password.length == 0 ? "" : MD5(this.staff.password);
                    loadData("post", "/oa/staff/save", this.staff, r => {
                        if (r.code == 0) {
                            this.dialogEnabled = false;
                            this.$message.success(r.message);
                            this.search();
                        } else {
                            this.$message.error(r.message);
                        }
                    }, e => {
                        this.$alert(e.message, '温馨提示');
                    });
                } else {
                    return false;
                }
            });
        },
        beforeUpload(file) {
            const app = this;
            const ext = ["image/jpeg", "image/gif", "image/png"];
            if (ext.indexOf(file.type) == -1) {
                this.$message.error('上传头像图片只能是 JPG GIF PNG 格式!');
            } else {
                let image = new Image();
                let resultBlob = "";
                image.src = URL.createObjectURL(file);
                image.onload = () => {
                    let w, h, maxSize = 300;
                    if (image.width <= maxSize && image.height <= maxSize) {
                        w = image.width;
                        h = image.height;
                    } else if (image.width > image.height) {
                        w = maxSize;
                        h = maxSize * image.height / image.width;
                    } else {
                        h = maxSize;
                        w = maxSize * image.width / image.height;
                    }

                    let imageData = this.compressUpload(image, w, h);
                    loadData("post", "/oa/staff/avatar", imageData, r => {
                        app.staff.avatar = r.data;
                    }, e => {
                        app.staff.avatar = "";
                        this.$message.success(e);
                    });
                };
            }
            return false;
        },
        compressUpload(image, width, height) {
            let canvas = document.createElement("canvas");
            let ctx = canvas.getContext("2d");
            canvas.width = width;
            canvas.height = height;
            ctx.fillRect(0, 0, width, height);
            ctx.drawImage(image, 0, 0, width, height);
            return canvas.toDataURL("image/jpeg", 0.9);
        }
    }
});

vue.search();
