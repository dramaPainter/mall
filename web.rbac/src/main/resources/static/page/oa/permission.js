let upsert = new Vue({
    el: '#upsert',
    data: {
        permission: {},
        dialogTitle: "",
        dialogEnabled: false,
        id_disabled: false,
        enums: ENUM
    },
    methods: {
        toEdit(row) {
            this.dialogTitle = "修改权限";
            this.id_disabled = true;
            this.permission = row;
            this.dialogEnabled = true;
        },
        toAdd() {
            this.dialogTitle = "添加权限";
            this.id_disabled = false;
            this.dialogEnabled = true;
            this.staff = {id: 0, name: '', url: '', pid: 0, type: 0, sort: 0, status: null};
        },
        toRemove(row) {
            this.$confirm("该节点的上级节点如果只有一个子节点将会一并删除，确定要执行此操作？", "警告：删除后无法恢复", {type: "warning"}).then(() => {
                loadData("post", "/oa/permission/remove", row.id, r => {
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
                    loadData("post", "/oa/permission/save", this.permission, r => {
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
        loadData("get", "/dir/qualify?url=/oa/permission/save", {}, r => {
            this.editQualify = r.data == true;
        }, null);
        loadData("get", "/dir/qualify?url=/oa/permission/remove", {}, r => {
            this.removeQualify = r.data == true;
        }, null);

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
            let url = "/oa/permission?pageSize=15&page=" + this.page + "&status=" + this.status + "&key=" + this.searchType + "&value=" + encodeURIComponent(this.searchText);
            loadTable(this, url);
        }
    }
});
