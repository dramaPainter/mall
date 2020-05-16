class Enum {
    add(field, name, value) {
        this[field] = {name, value};
        return this;
    }

    getValue(name) {
        if (name === undefined || name === null) return null;
        for (let i in this) if (this[i].name === name) return this[i].value;
        return null;
    }

    getName(value) {
        if (value === undefined || value === null) return "";
        for (let i in this) if (this[i].value === value) return this[i].name;
        return "";
    }
}

const ENUM = {};
ENUM.status = new Enum()
    .add('DISABLE', '冻结', 0)
    .add('ENABLE', '启用', 1);
ENUM.menuType = new Enum()
    .add('MENU', '菜单', 2)
    .add('PAGE', '页面', 1)
    .add('ITEM', '子项', 0);
ENUM.platform = new Enum()
    .add('ALL', '全平台', 0)
    .add('PLATFORM_1', '默认', 1)
    .add('PLATFORM_2', '2平台', 2)
    .add('PLATFORM_3', '3平台', 3)
    .add('PLATFORM_4', '4平台', 4)
    .add('PLATFORM_5', '5平台', 5);
ENUM.staffType = new Enum()
    .add('CS_OTHER', '其他客服帐号', 1)
    .add('CS_RECHARGE', '充值客服帐号', 2)
    .add('CS_EXCHANGE', '兑换客服帐号', 4)
    .add('CS_MANAGER', '客服组长帐号', 8)
    .add('PLATFORM_ONE', '单平台帐号', 16)
    .add('PLATFORM_ALL', '全平台帐号', 32)
    .add('PLATFORM_NATIVE', '内部帐号', 64)
    .add('PLATFORM_ADMIN', '管理员帐号', 128);

const PICKER_OPTION = {
    shortcuts: [{
        text: '当天',
        onClick(picker) {
            const end = new Date();
            const start = new Date(new Date().toISOString().substring(0, 10) + " 00:00:00");
            picker.$emit('pick', [start, end]);
        }
    }, {
        text: '最近一周',
        onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
        }
    }, {
        text: '最近一个月',
        onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
        }
    }, {
        text: '最近三个月',
        onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
        }
    }]
}

function loadScript(path) {
    let script = `
        <link href="/js/vue/element/element-ui.css" rel="stylesheet"/>
        <link href="/css/core.css" rel="stylesheet"/>
        <script src="/js/vue/element/vue.js"></script>
        <script src="/js/vue/element/axios.js"></script>
        <script src="/js/vue/element/element-ui.js"></script>
    `;
    path.forEach(o => script += `<script src="${o}"></script>`);
    document.write(script);
}

function loadData(method, url, param, succeedCallback, failedCallback) {
    failedCallback = failedCallback || function () {
    };
    axios.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8';
    axios({method: method, url: url, data: param}).then(r => {
        let tIndex = r.request.responseURL.indexOf("?");
        let cIndex = url.indexOf("?");
        let tUrl = tIndex == -1 ? r.request.responseURL : r.request.responseURL.substring(0, tIndex);
        let cUrl = cIndex == -1 ? url : url.substring(0, cIndex);
        if (r.status !== 200) {
            alert(r.statusText);
            failedCallback({code: -r.status, message: r.statusText});
        } else if (tUrl.indexOf(cUrl) == -1) {
            location.href = r.request.responseURL;
        } else if (r.data.code < 0) {
            failedCallback(r.data);
        } else {
            succeedCallback(r.data);
        }
        delete r.data;
    }).catch(r => failedCallback({code: -1, message: r}));
}

function loadTable(app, url, callback) {
    callback = callback || function () {
    };
    loadData("get", url, {}, r => {
        app.tableData = r.data;
        app.rowCount = r.code;
        app.loading = false;
        delete r.data;
        callback();
    }, e => {
        app.loading = false;
        app.$alert(e.message, '温馨提示');
    });
}
