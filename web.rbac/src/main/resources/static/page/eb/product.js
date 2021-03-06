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

let library = new Vue({
    el: '#library',
    data: {
        tableData: [],
        previewImages: [],
        callback: function () {},
        loading: false,
        fullScreen: false,
        init: false,
        rowCount: 0,
        page: 1,
        size: 15,
        uploadData: {type: 2, value: ""},
        dialogEnabled: false
    },
    methods: {
        onCancel() {
            this.dialogEnabled = false;
        },
        uploadRemove(file) {
            loadData("post", "/upload/remove", file.id, r => {
                if (r.code == 0) {
                    let index = upsert.product.image.map(o => o.url).indexOf(file.url);
                    if (index > -1) {
                        upsert.product.image.splice(index, 1);
                    }
                    this.$message.success(r.message);
                    this.search();
                } else {
                    this.$message.error(r.message);
                }
            }, e => {
                this.$alert(e.message, '温馨提示');
            });
        },
        uploadPreview(file) {
            let index = this.tableData.map(o => o.url).indexOf(file.url);
            this.$refs.previewer.clickHandler();
        },
        uploadSuccess() {
            this.page = 1;
            this.search();
        },
        show(type, callback) {
            this.uploadData.type = type;
            this.callback = callback || function () {};
            this.dialogEnabled = true;
            if (!this.init) {
                this.init = true;
                this.search();
            }
        },
        onConfirm() {
            let urls = this.tableData.filter(t => t.selected);
            urls.forEach(t => t.selected = false);
            this.callback(urls);
            this.dialogEnabled = false;
        },
        pageChanged(pageid) {
            this.page = pageid;
            this.search();
        },
        sizeChanged(size) {
            this.size = size;
            this.search();
        },
        search() {
            this.loading = true;
            let url = "/upload/list?page=" + this.page + "&pageSize=" + this.size + "&type=" + this.uploadData.type + "&value=" + this.uploadData.value;
            loadTable(this, url, data => {
                data.forEach(o => o.selected = false);
                this.previewImages = data.map(o => o.url);
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
        preview: [],
        product: {},
        uploadUrl: '',
        editorInit: false,
        dialogTitle: "",
        dialogEnabled: false,
        newKeywordVisible: false,
        newKeywordName: ""
    },
    mounted: function () {
        loadOption(this, "/eb/category?page=1&pageSize=9999", "categories").then(x => x.forEach(o => o.id = o.id + ""));
        loadOption(this, "/eb/brand?page=1&pageSize=9999", "brands").then(x => x.forEach(o => o.id = o.id + ""));
    },
    methods: {
        toEdit(row) {
            this.dialogTitle = "修改商品";
            this.dialogEnabled = true;
            this.product = row;
            this.preview = row.image.map(o => o.url);
            this.keywords = row.keyword.length == 0 ? [] : row.keyword.split(",");
            this.$nextTick(() => {
                if (!this.editorInit) {
                    tinymce.init({
                        selector: 'textarea', height: 400, language: 'zh_CN',
                        plugins: [
                            'advlist autolink lists link imageLibrary hr',
                            'visualblocks visualchars code',
                            'textpattern'
                        ],
                        toolbar: ['styleselect | forecolor backcolor | bold italic | imageLibrary', 'alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link'],
                        imageSelectorCallback: this.showImageSelector
                    });
                    this.editorInit = true;
                }
            })
        },
        showImageSelector(cb) {
            show(ENUM.upload.PRODUCT_CAROUSEL.value, cb);
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
                    this.product.body = tinymce.editors[0].getContent();
                    this.product.keyword = this.keywords.join(",");
                    loadData("post", "/eb/product/save", this.product, r => {
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
        uploadRemove(file) {
            let index = this.product.image.map(o => o.url).indexOf(file.url);
            if (index > -1) {
                this.product.image.splice(index, 1);
            }
        },
        uploadPreview(index) {
            this.$refs.reflect[index].clickHandler();
        },
        uploadAvatar(file) {
            this.product.avatar = file.url;
        },
        show() {
            let map = this.product.image.map(o => o.url);
            library.show(ENUM.upload.PRODUCT_CAROUSEL.value, urls => urls.forEach(o => {
                if (map.indexOf(o.url) == -1) {
                    this.product.image.push(o);
                }
            }));
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
        editQualify: false
    },
    mounted: function () {
        loadPermission(this, "editQualify", "/dir/qualify?url=/eb/product/save");
        loadPermission(this, "editQualify", "/dir/qualify?url=/eb/product/display");
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
            loadTable(this, url, res => res.forEach(o => {
                o.brand = o.brand + "";
                o.category = o.category + "";
            }));
        }
    }
});
