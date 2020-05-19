let upsert = new Vue({
    el: '#upsert',
    data: {
        roles: [],
        staff: {id: 0, name: '', password: '', alias: '', avatar: '', role: null, status: null},
        dialogTitle: "",
        dialogEnabled: false,
        username_disabled: false,
        colors: ["", "success", "warning", "danger", "info"],
        enums: ENUM
    },
    methods: {
        loadRoles() {
            return new Promise(load => {
                loadData("get", "/oa/role?page=1&pageSize=9999", {}, r => {
                    if (r.code >= 0) {
                        r.data.forEach(o => o.id = o.id + "");
                        this.roles = r.data;
                        load();
                    } else {
                        this.$message.error(r.message);
                    }
                }, e => {
                    this.$alert(e.message, '温馨提示');
                });
            });
        },
        toName(id) {
            let find = this.roles.filter(o => o.id == id);
            return find.length == 0 ? "未知角色" : find[0].name;
        },
        toColor(id) {
            let role = this.roles.filter(o => o.id == id);
            return this.colors[role.length == 0 ? 0 : this.roles.indexOf(role[0]) % 5];
        },
        toEdit(row) {
            this.dialogTitle = "修改帐号";
            this.username_disabled = true;
            this.staff = row;
            this.dialogEnabled = true;
        },
        toAdd() {
            this.dialogTitle = "添加帐号";
            this.username_disabled = false;
            this.dialogEnabled = true;
            this.staff = {id: 0, name: '', password: '', alias: '', avatar: '', role: null, status: null};
        },
        toRemove(row) {
            this.$confirm("帐号和其权限一并删除，确定要执行此操作？", "警告：删除后无法恢复", {type: "warning"}).then(() => {
                loadData("post", "/oa/staff/remove", row.id, r => {
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
            });
        },
        onSubmit(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.staff.password = this.staff.password.length == 0 ? "" : MD5(this.staff.password);
                    let copy = Object.assign({}, this.staff);
                    copy.role = copy.role == null ? "" : copy.role.join(",");
                    loadData("post", "/oa/staff/save", copy, r => {
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
        status: "",
        password: "",
        searchType: "",
        searchText: "",
        loading: false,
        enums: ENUM,
        editQualify: false,
        removeQualify: false
    },
    mounted: function () {
        upsert.loadRoles().then(() => {
            loadData("get", "/dir/qualify?url=/oa/staff/save", {}, r => {
                this.editQualify = r.data == true;
            }, null);
            loadData("get", "/dir/qualify?url=/oa/staff/remove", {}, r => {
                this.removeQualify = r.data == true;
            }, null);
        });

        this.search();
    },
    methods: {
        toAdd() {
            upsert.toAdd();
        },
        toEdit(row) {
            upsert.toEdit(row);
        },
        toRemove(row) {
            upsert.toRemove(row);
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
            loadTable(this, url);
        }
    }
});
