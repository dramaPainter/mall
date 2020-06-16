!function () {
    let modify = new Vue({
        el: '#modify',
        data: {
            pwd: {},
            dialogEnabled: false
        },
        methods: {
            toPassword(row) {
                this.pwd = row;
                this.dialogEnabled = true;
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.pwd.password = this.pwd.password.length == 0 ? "" : MD5(this.pwd.password);
                        loadData("post", "/oa/staff/password", {username: this.pwd.name, password: this.pwd.password}, r => {
                            if (r.code == 0) {
                                this.dialogEnabled = false;
                                this.$message.success(r.message);
                                app.search();
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
            }
        }
    });

    let upsert = new Vue({
        el: '#upsert',
        data: {
            roles: [],
            staff: {},
            dialogTitle: "",
            dialogEnabled: false,
            username_disabled: false,
            colors: ["", "success", "warning", "danger", "info"]
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
                let name = find.length == 0 ? "未知角色" : find[0].name;
                return name;
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
                this.$confirm("帐号和权限一并删除，确认操作？", "警告：删除后无法恢复", {type: "warning"}).then(() => {
                    loadData("post", "/oa/staff/remove", row.name, r => {
                        if (r.code == 0) {
                            this.dialogEnabled = false;
                            this.$message.success(r.message);
                            app.search();
                        } else {
                            this.$message.error(r.message);
                        }
                    }, e => {
                        this.$alert(e.message, '温馨提示');
                    });
                }).catch(() => {});
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        loadData("post", "/oa/staff/save", this.staff, r => {
                            if (r.code == 0) {
                                this.dialogEnabled = false;
                                this.$message.success(r.message);
                                app.search();
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
                return uploadSingle(this, file, ENUM.upload.HEAD.value, url => this.staff.avatar = url, true);
            }
        }
    });

    let app = new Vue({
        el: '#app',
        data: {
            tableData: [],
            rowCount: 0,
            page: 1,
            status: "",
            searchType: "",
            searchText: "",
            loading: false,
            editQualify: false,
            removeQualify: false,
            passwordQualify: false
        },
        mounted: function () {
            loadPermission(this, "editQualify", "/dir/qualify?url=/oa/staff/save");
            loadPermission(this, "removeQualify", "/dir/qualify?url=/oa/staff/remove");
            loadPermission(this, "passwordQualify", "/dir/qualify?url=/oa/staff/password");
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
            toPassword(row) {
                modify.toPassword(row);
            },
            toName(id) {
               return upsert.toName(id);
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
                loadTable(this, url, () => {
                    this.tableData.forEach(o => o.role = o.role.length == 0 ? o.role : o.role.join(",").split(","));
                    this.password = "";
                });
            }
        }
    });
}();
