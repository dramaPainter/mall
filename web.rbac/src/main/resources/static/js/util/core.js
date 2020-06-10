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

const PAGE_SIZE = 15;

const ENUM = {};
ENUM.status = new Enum()
    .add('ENABLE', '启用', 1)
    .add('DISABLE', '冻结', 0);
ENUM.product = new Enum()
    .add('ENABLE', '上架', 1)
    .add('DISABLE', '下架', 0);
ENUM.yesno = new Enum()
    .add('YES', '是', 1)
    .add('NO', '否', 0);
ENUM.upload = new Enum()
    .add('HEAD', '头像', 1)
    .add('PRODUCT', '产品', 2);
ENUM.menuType = new Enum()
    .add('MENU', '菜单', 2)
    .add('PAGE', '页面', 1)
    .add('ITEM', '子项', 0);

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

function loadData(method, url, param, succeedCallback, failedCallback = function () {}, multipart = false) {
    axios.defaults.headers.post['Content-Type'] = multipart ? 'multipart/form-data' : 'application/json; charset=utf-8';
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

function loadTable(app, url, callback = function () {}) {
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

function uploadSingle(app, file, type, callback = function () {}, compress = true) {
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

        let formData = new FormData();
        if (compress) {
            let imageData = compressImage(image, w, h);
            formData.append('image', imageData);
            formData.append('type', type);
        } else {
            formData.append('file', file);
            formData.append('type', type);
        }
        loadData("post", "/upload/single", formData, r => {
            if (r.code == 0) {
                callback(r.data);
            } else {
                app.$alert(r.message, '温馨提示');
            }
        }, e => {
            app.$alert(e.message, '温馨提示');
        }, true);
    };
    return false;
}

function compressImage(image, width, height) {
    let canvas = document.createElement("canvas");
    let ctx = canvas.getContext("2d");
    canvas.width = width;
    canvas.height = height;
    ctx.fillRect(0, 0, width, height);
    ctx.drawImage(image, 0, 0, width, height);
    return canvas.toDataURL("image/jpeg", 0.9);
}
