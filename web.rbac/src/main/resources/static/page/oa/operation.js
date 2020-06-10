(function () {
    let app = new Vue({
        el: '#app',
        data: {
            tableData: [],
            rowCount: 0,
            page: 1,
            datetime: ["", ""],
            timespan: "",
            searchText: "",
            loading: false,
            pickerOptions: PICKER_OPTION
        },
        methods: {
            mounted() {
                this.search();
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
                let url = "/oa/operation";
                url += "?page=" + this.page
                    + "&searchText=" + encodeURIComponent(this.searchText)
                    + "&timespan=" + this.timespan
                    + "&startTime=" + this.datetime[0]
                    + "&endTime=" + this.datetime[1];
                loadData("get", url, {}, r => {
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
})();
