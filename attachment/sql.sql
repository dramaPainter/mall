create table zero.oa_permission(
    id     int auto_increment comment '页面ID' primary key,
    name   varchar(25)      not null comment '页面名称',
    url    varchar(50)      not null comment '页面地址',
    pid    int              not null comment '父节点',
    type   tinyint          not null comment '类型：2、菜单 1、页面 0、子项',
    sort   tinyint unsigned not null comment '排名',
    status bit              not null comment '状态 1.启用 2.隐藏 3.禁用'
) comment '权限表' charset = utf8mb4;

create table zero.oa_role (
    id     int auto_increment comment '角色ID' primary key,
    name   varchar(30) not null comment '角色名称',
    status bit         not null comment '状态'
) comment '角色表';

create table zero.oa_role_permission (
    role       int not null comment '角色ID',
    permission int not null comment '权限ID',
    primary key (role, permission)
) comment '角色权限表';

create table zero.oa_staff (
    id       int auto_increment comment '帐号ID' primary key,
    name     varchar(16)  not null comment '帐号名称',
    alias    varchar(16)  not null comment '昵称',
    status   bit          not null comment '状态：1.启用 0.删除',
    salt     char(8)      not null comment '动态码',
    password char(32)     not null comment '帐号密码',
    avatar   varchar(100) not null comment '头像'
) comment '员工表' charset = utf8mb4;

create table zero.oa_staff_role (
    staff int not null comment '帐号ID',
    role  int not null comment '角色ID',
    primary key (staff, role)
) comment '员工角色表' charset = utf8mb4;

INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100001, '后台系统', '', 0, 2, 1, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100002, '系统管理', '', 100001, 2, 99, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100003, '员工管理', '/oa/staff', 100002, 1, 1, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100004, '查看员工列表', '/oa/staff', 100003, 0, 1, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100005, '设置员工资料', '/oa/staff/save', 100003, 0, 3, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100006, '删除员工资料', '/oa/staff/remove', 100003, 0, 3, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100007, '上传员工头像', '/oa/staff/avatar', 100003, 0, 2, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100008, '角色管理', '/oa/role', 100002, 1, 5, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100009, '查看角色列表', '/oa/role', 100008, 0, 5, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100010, '设置角色', '/oa/role/save', 100008, 0, 5, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100011, '删除角色', '/oa/role/remove', 100008, 0, 5, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100012, '权限管理', '/oa/permission', 100002, 1, 6, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100013, '查看权限', '/oa/permission', 100012, 0, 5, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100014, '设置权限', '/oa/permission/save', 100012, 0, 6, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100015, '删除权限', '/oa/permission/remove', 100012, 0, 6, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100016, '日志管理', '/oa/operation', 100002, 1, 7, true);
INSERT INTO zero.oa_permission (id, name, url, pid, type, sort, status) VALUES (100017, '查看操作日志', '/oa/operation', 100016, 0, 8, true);

INSERT INTO zero.oa_role (id, name, status) VALUES (1, '管理员', true);
INSERT INTO zero.oa_role (id, name, status) VALUES (2, '运维', true);
INSERT INTO zero.oa_role (id, name, status) VALUES (3, '财务', true);
INSERT INTO zero.oa_role (id, name, status) VALUES (4, '运营', true);
INSERT INTO zero.oa_role (id, name, status) VALUES (5, '客服', true);
INSERT INTO zero.oa_role (id, name, status) VALUES (6, '行政', true);
INSERT INTO zero.oa_role (id, name, status) VALUES (7, '开发', true);

INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100001);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100002);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100003);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100004);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100005);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100006);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100007);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100008);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100009);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100010);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100011);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100012);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100013);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100014);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100015);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100016);
INSERT INTO zero.oa_role_permission (role, permission) VALUES (1, 100017);

INSERT INTO zero.oa_staff (id, name, alias, status, salt, password, avatar) VALUES (1, 'admin', '管理员', true, 'dWwlO4iI', '5c2791c78dcb6c0fdfaa962002abb075', '');

INSERT INTO zero.oa_staff_role (staff, role) VALUES (84, 1);
