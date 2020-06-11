
    let sku = new Vue({
        el: '#sku',
        data: {
            spu: {id: 0, pid: 0, name: null, impact: false, list: []},
            product: {},
            dialogTitle: "",
            dialogEnabled: false,
            newTagVisible: false,
            newTagName: "",
            currentTab: 0
        },
        methods: {
            toStock(row) {
                this.product = row;
                this.product.sku.forEach(s => {
                    let combo = s.combo.split("-");
                    let name = [];
                    this.product.spu.forEach(o => o.list.filter(t => combo.indexOf(t.id + "") > -1).forEach(m => name.push(m.value)));
                    s.name = name.join(" ");
                });
                this.dialogTitle = "商品库存 - " + this.product.name;
                this.dialogEnabled = true;
            },
            stockRemove(row) {
                this.$confirm("确定要删除 [" + row.name + "] ？", "温馨提示", {type: 'warning'}).then(() =>
                    this.product.sku.splice(this.product.sku.findIndex(item => item.combo === row.combo), 1)
                ).catch(() => {});
            },
            skuTabClick(targetName, action) {
                if (action == "add") {
                    this.product.spu.push({id: null, pid: this.product.id, name: "新建属性", impact: 0, list: []})
                    this.$nextTick(() => {this.currentTab = this.$refs.tabRef.length - 1;});
                } else if (action == "remove") {
                    this.$confirm("确定要删除 [" + this.product.spu[targetName].name + "] ？", "温馨提示", {type: 'warning'}).then(() => {
                        this.product.spu.splice(targetName, 1);
                        this.$nextTick(() => {this.currentTab = targetName - 1;});
                    }).catch(() => {});
                }
            },
            removeSpuItem(item, tag) {
                this.$confirm("确定要删除此属性？", "温馨提示", {type: 'warning'}).then(() =>
                    item.list.splice(item.list.findIndex(item => item.value === tag.value), 1)
                ).catch(() => {});
            },
            showTagInput(item, index) {
                this.spu = item;
                this.newTagVisible = true;
                this.$nextTick(() => this.$refs.tagRef[index].$refs.input.focus());
            },
            saveTag() {
                if (this.newTagName) {
                    this.spu.list.push({id: null, nameId: this.spu.id, value: this.newTagName});
                }
                this.newTagVisible = false;
                this.newTagName = '';
            },
            fill() {
                let res = [];
                if (this.product.spu == null || this.product.spu.length == 0) return;
                this.backTrace(this.product.spu.filter(o => o.impact), 0, res, []);
                res.forEach(r => {
                    let combo = r.map(o => o.id).join("-");
                    let name = r.map(o => o.value).join(" ");
                    let found = this.product.sku.find(t => t.combo == combo);
                    if (found) {
                        this.product.sku.splice(this.product.sku.indexOf(found), 1);
                    } else {
                        found = {combo: combo, name: name, pid: this.product.id, sn: 0, stock: 0, retail: 0, market: 0, purchase: 0};
                    }
                    this.product.sku.push(found);
                });
            },
            backTrace(dimValue, index, result, curList) {
                if (curList.length == dimValue.length) {
                    let t = [];
                    Object.assign(t, curList);
                    result.push(t);
                } else {
                    for (let j = 0; j < dimValue[index].list.length; j++) {
                        curList.push(dimValue[index].list[j]);
                        this.backTrace(dimValue, index + 1, result, curList);
                        curList.pop();
                    }
                }
            },
            onCancel() {
                this.dialogEnabled = false;
                app.search();
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        loadData("post", "/eb/product/save/sku", this.product, r => {
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
            brands: [],
            categories: [],
            keywords: [],
            product: {},
            uploadUrl: '',
            uploadVisible: false,
            editorInit: false,
            dialogTitle: "",
            dialogEnabled: false,
            newKeywordVisible: false,
            newKeywordName: ""
        },
        methods: {
            loadCategories() {
                return new Promise(load => {
                    loadData("get", "/eb/category?page=1&pageSize=9999", {}, r => {
                        if (r.code >= 0) {
                            r.data.forEach(o => o.id = o.id + "");
                            this.categories = r.data;
                            load();
                        } else {
                            this.$message.error(r.message);
                        }
                    }, e => {
                        this.$alert(e.message, '温馨提示');
                    });
                });
            },
            loadBrands() {
                return new Promise(load => {
                    loadData("get", "/eb/brand?page=1&pageSize=9999", {}, r => {
                        if (r.code >= 0) {
                            r.data.forEach(o => o.id = o.id + "");
                            this.brands = r.data;
                            load();
                        } else {
                            this.$message.error(r.message);
                        }
                    }, e => {
                        this.$alert(e.message, '温馨提示');
                    });
                });
            },
            toEdit(row) {
                this.dialogTitle = "修改商品";
                this.dialogEnabled = true;
                this.product = row;
                this.keywords = row.keyword.length == 0 ? [] : row.keyword.split(",");
                this.$nextTick(() => {
                    if (!this.editorInit) {
                        tinymce.init({selector: 'textarea', height: 400, language: 'zh_CN'});
                        this.editorInit = true;
                    }
                })
            },
            toAdd() {
                this.dialogTitle = "添加商品";
                this.dialogEnabled = true;
                this.keywords = [];
                this.product = {id: null, name: null, code: null, category: null, brand: null, sort: null, status: null, hottest: null, latest: null, sale: null, keyword: "", avatar: "", body: "", sku: [], spu: []};
            },
            removeKeyword(key) {
                this.keywords.splice(this.keywords.findIndex(item => item == key), 1);
            },
            showKeywordInput() {
                this.newKeywordVisible = true;
                this.$nextTick(() => {
                    this.$refs.keywordRef.$refs.input.focus();
                });
            },
            saveKeyword() {
                if (this.newKeywordName) {
                    this.keywords.push(this.newKeywordName);
                }
                this.newKeywordVisible = false;
                this.newKeywordName = '';
            },
            onCancel() {
                this.dialogEnabled = false;
                app.search();
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.product.keyword = this.keywords.join(",");
                        loadData("post", "/eb/product/saveInfo", this.product, r => {
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
            uploadRemove(file, fileList) {
                console.log(file, fileList);
            },
            uploadPreview(file) {
                this.uploadUrl = file.url;
                this.uploadVisible = true;
            },
            uploadAvatar(file) {

            },
            beforeUpload(file) {
                return uploadSingle(this, file, ENUM.upload.PRODUCT.value, url => this.product.avatar = url, true);
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
            displayQualify: false,
            editQualify: false,
            sort: 0,
            sale: 0,
            enums: ENUM
        },
        mounted: function () {
            loadData("get", "/dir/qualify?url=/eb/product/save", {}, r => {
                this.editQualify = r.data == true;
            }, null);
            loadData("get", "/dir/qualify?url=/eb/product/display", {}, r => {
                this.displayQualify = r.data == true;
            }, null);
            upsert.loadCategories();
            upsert.loadBrands();

            this.search();
        },
        methods: {
            toAdd() {
                upsert.toAdd();
            },
            toEdit(row) {
                upsert.toEdit(row);
            },
            toStock(row) {
                sku.toStock(row);
            },
            toDisplay(row) {
                let msg = "确定要" + (row.status == ENUM.status.ENABLE.value ? "下架" : "上架") + "此商品？";
                this.$confirm(msg, "温馨提示", {type: "warning"}).then(() => {
                    loadData("post", "/eb/product/display", row.code, r => {
                        if (r.code == 0) {
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
            toCategory(row) {
                let one = upsert.categories.find(o => o.id == row.id);
                return one ? one.name : "";
            },
            toBrand(row) {
                let one = upsert.brands.find(o => o.id == row.id);
                return one ? one.name : "";
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
                let url = "/eb/product?page=" + this.page + "&status=" + this.status + "&key=" + this.searchType + "&value=" + encodeURIComponent(this.searchText);
                loadTable(this, url, () => this.tableData.forEach(o => {
                    o.brand = o.brand + "";
                    o.category = o.category + "";
                    upsert.product = o;
                }));
            }
        }
    });

