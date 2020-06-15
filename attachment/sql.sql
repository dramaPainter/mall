create table oa_permission(
    id     int auto_increment comment '页面ID' primary key,
    name   varchar(25)      not null comment '页面名称',
    url    varchar(50)      not null comment '页面地址',
    pid    int              not null comment '父节点',
    type   tinyint          not null comment '类型：2、菜单 1、页面 0、子项',
    sort   tinyint unsigned not null comment '排名'
) comment '权限表' charset = utf8mb4;

create table oa_role (
    id     int auto_increment comment '角色ID' primary key,
    name   varchar(30) not null comment '角色名称',
    remark varchar(30) not null comment '备注'
) comment '角色表';

create table oa_role_permission (
    role       int not null comment '角色ID',
    permission int not null comment '权限ID',
    primary key (role, permission)
) comment '角色权限表';

create table oa_staff (
    id       int auto_increment comment '帐号ID' primary key,
    name     varchar(16)  not null comment '帐号名称',
    alias    varchar(16)  not null comment '昵称',
    status   bit          not null comment '状态：1.启用 0.删除',
    salt     char(8)      not null comment '动态码',
    password char(32)     not null comment '帐号密码',
    avatar   varchar(100) not null comment '头像',
    unique index idx_name(name)
) comment '员工表' charset = utf8mb4;

create table oa_staff_role (
    staff int not null comment '帐号ID',
    role  int not null comment '角色ID',
    primary key (staff, role)
) comment '员工角色表' charset = utf8mb4;

INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100000, '后台系统', '', 0, 2, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100001, '系统管理', '', 100000, 2, 99);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100002, '上传管理', '', 100001, 2, 0);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100003, '上传单图', '/upload/single', 100002, 0, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100004, '上传多图', '/upload/multiple', 100002, 0, 2);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100005, '员工管理', '/oa/staff', 100001, 1, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100006, '查看员工列表', '/oa/staff', 100005, 0, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100007, '设置员工资料', '/oa/staff/save', 100005, 0, 3);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100008, '删除员工资料', '/oa/staff/remove', 100005, 0, 3);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100009, '修改员工密码', '/oa/staff/password', 100005, 0, 3);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100010, '角色管理', '/oa/role', 100001, 1, 5);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100011, '查看角色列表', '/oa/role', 100010, 0, 5);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100012, '设置角色', '/oa/role/save', 100010, 0, 5);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100013, '删除角色', '/oa/role/remove', 100010, 0, 5);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100014, '权限管理', '/oa/permission', 100001, 1, 6);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100015, '查看权限', '/oa/permission', 100014, 0, 5);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100016, '设置权限', '/oa/permission/save', 100014, 0, 6);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100017, '删除权限', '/oa/permission/remove', 100014, 0, 6);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100018, '日志管理', '/oa/operation', 100001, 1, 7);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100019, '查看操作日志', '/oa/operation', 100018, 0, 8);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100020, '商品管理', '', 100000, 2, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100021, '商品管理', '/eb/product', 100020, 1, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100022, '查看商品列表', '/eb/product', 100021, 0, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100023, '设置商品信息', '/eb/product/save', 100021, 0, 2);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100024, '上架下架商品', '/eb/product/display', 100021, 0, 3);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100025, '修改价格和库存', '/eb/product/saveSku', 100021, 0, 3);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100026, '分类管理', '/eb/category', 100020, 1, 2);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100027, '查询商品分类', '/eb/category', 100026, 0, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100028, '设置商品分类', '/eb/category/save', 100026, 0, 2);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100029, '删除商品分类', '/eb/category/remove', 100026, 0, 3);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100030, '品牌管理', '/eb/brand', 100020, 1, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100031, '查询商品品牌', '/eb/brand', 100030, 0, 1);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100032, '设置商品品牌', '/eb/brand/save', 100030, 0, 2);
INSERT INTO oa_permission (id, name, url, pid, type, sort) VALUES (100033, '删除商品品牌', '/eb/brand/remove', 100030, 0, 3);

INSERT INTO oa_role (id, name, remark) VALUES (1, '超管', '');
INSERT INTO oa_role (id, name, remark) VALUES (3, '财务', '');
INSERT INTO oa_role (id, name, remark) VALUES (2, '客服', '');

INSERT INTO oa_staff (id, name, alias, status, salt, password, avatar) VALUES (1, 'admin', '管理员', true, 'dWwlO4iI', '5C2791C78DCB6C0FDFAA962002ABB075', '');
INSERT INTO oa_role_permission (role, permission) SELECT 1, id FROM oa_permission
INSERT INTO oa_staff_role (staff, role) VALUES (1, 1);

create table eb_product (
    id       int auto_increment primary key comment '产品ID',
    name     varchar(50)   not null comment '产品名称',
    code     char(32)      not null comment '产品代号',
    category int           not null comment '所属分类',
    brand    int           not null comment '所属品牌',
    sort     int           not null comment '排序(越高越靠前)',
    status   bit           not null comment '是否上架',
    hottest  bit           not null comment '是否热销',
    latest   bit           not null comment '是否最新',
    sale     int           not null comment '已售数量',
    keyword  varchar(50)   not null comment '关键词',
    avatar   varchar(100)  not null comment '图片(480x480)',
    body     varchar(3000) not null comment '产品详细描述',
    unique index IDX_CODE (code),
    index IDX_CATE (category),
    index IDX_BRAND (brand),
    index IDX_SORT (sort)
) comment '产品列表';

create table eb_product_sku (
    pid      int            not null comment '产品ID',
    combo    varchar(30)    not null comment '产品属性ID组合',
    sn       bigint         not null comment '产品编码',
    stock    int            not null comment '库存',
    retail   decimal(10, 2) not null comment '零售价',
    market   decimal(10, 2) not null comment '市场价',
    purchase decimal(10, 2) not null comment '采购价',
    index IDX_SN (sn),
    primary key (pid, combo);
) comment '产品SKU';

create table eb_product_sku_name (
    id     int auto_increment primary key comment '自增列',
    pid    int         not null comment '产品ID',
    name   varchar(30) not null comment '属性名称',
    impact bit         not null comment '是否影响价格或库存',
    index  IDX_PID (pid)
) comment '产品SKU属性名';

create table eb_product_sku_value (
    id      int auto_increment primary key comment '自增列',
    name_id int         not null comment '产品SKU属性ID',
    value   varchar(30) not null comment '属性值',
    index IDX_NAME_ID (name_id)
) comment '产品SKU属性值';

create table eb_image (
    id    int auto_increment primary key comment '自增列',
    type  tinyint      not null comment '所属类型',
    value int          not null comment '类型对应ID',
    name  varchar(50)  not null comment '标题',
    url   varchar(100) not null comment '图片地址',
    index IDX_VALUE (value),
    index IDX_TYPE (type)
) comment '产品集合图片库'
