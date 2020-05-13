let permission = new Vue({
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
                r.data.filter(o => o.pid == 0).map(m => {
                    return {id: m.id, pid: m.pid, label: m.name, children: []};
                }).forEach(e => this.data.push(e));
                this.fillTree(this.data, r.data);
                delete r.data;
            }, e => {
                this.$alert(e.message, '温馨提示');
            });
        },
        toQualify(row) {
            this.loadPermission();
            loadData("get", "/oa/permission/staff?userid=" + row.id, {}, r => {
                permission.dialogEnabled = true;
                permission.userid = row.id;
                permission.dialogTitle = "【" + row.alias + "】权限设置";
                setTimeout(() => {
                    this.$refs.treeviewer.setCheckedKeys([]);
                    if (r.data) {
                        this.$refs.treeviewer.setCheckedKeys(r.data.split(","));
                    }
                }, 100);
            }, e => {
                this.$alert(e.message, '温馨提示');
            });
        },
        fillTree(right, data) {
            if (right.length == 0) return;
            right.forEach(p => {
                data.filter(o => o.pid == p.id).map(m => {
                    return {id: m.id, pid: m.pid, label: m.name, children: []};
                }).forEach(e => p.children.push(e));
                this.fillTree(p.children, data);
            })
        },
        onSubmit() {
            let qualify = permission.$refs.treeviewer.getCheckedKeys() + "";
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

let upsert = new Vue({
    el: '#upsert',
    data: {
        staff: {},
        dialogTitle: "",
        dialogEnabled: false,
        username_disabled: false,
        enums: ENUM
    },
    methods: {
        toEdit(row) {
            this.dialogTitle = "修改帐号";
            this.username_disabled = true;
            this.staff = row;
            this.dialogEnabled = true;
        },
        getItems(right, items) {
            if (right.children.length == 0) {
                items.push(right.id);
                return;
            } else {
                right.children.forEach(p => this.getItems(p, items));
            }
        },
        toAdd() {
            this.dialogTitle = "添加帐号";
            this.username_disabled = false;
            this.dialogEnabled = true;
            this.staff = {id: 0, name: '', password: '', alias: '', avatar: '', type: null, platform: null, status: null};
        },
        onSubmit(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.staff.password = this.staff.password.length == 0 ? "" : MD5(this.staff.password);
                    loadData("post", "/oa/staff/save", this.staff, r => {
                        if (r.code == 0) {
                            this.dialogEnabled = false;
                            this.$message.success(r.message);
                            vue.search();
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
            let image = new Image();
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

                let imageData = this.compress(image, w, h);
                loadData("post", "/oa/staff/avatar", imageData, r => {
                    this.staff.avatar = r.data;
                }, e => {
                    this.staff.avatar = "";
                    this.$message.success(e);
                });
            };
            return false;
        },
        compress(image, width, height) {
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

let vue = new Vue({
    el: '#app',
    data: {
        tableData: [],
        rowCount: 0,
        page: 1,
        status: "-1",
        searchType: "-1",
        searchText: "",
        loading: false,
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
        toAdd() {
            upsert.toAdd();
        },
        toEdit(row) {
            upsert.toEdit(row);
        },
        toQualify(row) {
            permission.toQualify(row);
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
            let url = "/oa/staff?page=" + this.page + "&status=" + this.status + "&key=" + this.searchType + "&value=" + encodeURIComponent(this.searchText);
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
        }
    }
});

vue.search();
