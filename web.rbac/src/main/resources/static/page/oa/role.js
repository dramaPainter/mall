let upsert = new Vue({
    el: '#upsert',
    data: {
        permissions: [],
        role: {},
        dialogTitle: "",
        dialogEnabled: false,
        id_disabled: false,
        enums: ENUM
    },
    methods: {
        loadPermission() {
            return new Promise(load => {
                loadData("get", "/oa/permission?pageSize=9999&page=1", {}, r => {
                    r.data.filter(o => o.pid == 0).map(m => {
                        return {id: m.id + "", pid: m.pid + "", label: m.name, children: []};
                    }).forEach(e => this.permissions.push(e));
                    this.feedTree(this.permissions, r.data);
                    delete r.data;
                    load();
                }, e => {
                    this.$alert(e.message, '温馨提示');
                });
            });
        },
        feedTree(right, data) {
            if (right.length == 0) return;
            right.forEach(p => {
                data.filter(o => o.pid == p.id).map(m => {
                    return {id: m.id, pid: m.pid, label: m.name, children: []};
                }).forEach(e => p.children.push(e));
                this.feedTree(p.children, data);
            })
        },
        onCheck() {
            this.role.permission = this.$refs.treeviewer.getCheckedKeys();
        },
        toEdit(row) {
            this.dialogTitle = "修改角色";
            this.id_disabled = true;
            this.role = row;
            this.dialogEnabled = true;
            let keys = row.permission == null ? [] : row.permission;
            this.$nextTick(() => {
                this.$refs.treeviewer.setCheckedKeys(keys);
            });
        },
        toAdd() {
            this.dialogTitle = "添加角色";
            this.id_disabled = false;
            this.dialogEnabled = true;
            this.role = {id: 0, name: '', permission: [], status: null};
            this.$nextTick(() => this.$refs.treeviewer.setCheckedKeys([]));
        },
        toRemove(row) {
            this.$confirm("删除角色的同时也会删除该角色绑定的权限，确定要执行此操作？", "警告：删除后无法恢复", {type: "warning"}).then(() => {
                loadData("post", "/oa/role/remove", row.id, r => {
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
                    loadData("post", "/oa/role/save", this.role, r => {
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
        enums: ENUM,
        editQualify: false,
        removeQualify: false
    },
    mounted: function () {
        upsert.loadPermission().then(() => {
            loadData("get", "/dir/qualify?url=/oa/role/save", {}, r => {
                this.editQualify = r.data == true;
            }, null);
            loadData("get", "/dir/qualify?url=/oa/role/remove", {}, r => {
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
            let url = "/oa/role?pageSize=15&page=" + this.page + "&status=" + this.status + "&key=" + this.searchType + "&value=" + encodeURIComponent(this.searchText);
            loadTable(this, url);
        }
    }
});
