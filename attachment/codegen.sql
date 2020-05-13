# 使用一个function解决camel命名问题
drop function if exists camel;
create function camel(name varchar(1000))
    returns varchar(1000)
begin
    set name = concat(upper(left(name, 1)), substring(name, 2, 1000));
    set @idx = locate('_', name);
    if @idx = 0 then
        return name;
    else
        while @idx > 0
            do
                set name = concat(left(name, @idx - 1), UPPER(substring(name, @idx + 1, 1)), substring(name, @idx + 2));
                set @idx = locate('_', name);
            end while;
        return name;
    end if;
end;

# 输出 mybatis mapper
select TABLE_NAME,
       concat('/**\n * 查询单个对象\n * ',
              (select group_concat('\n * @param ', c2.COLUMN_NAME, ' ', c2.COLUMN_COMMENT separator '') from information_schema.COLUMNS c2 where c.TABLE_SCHEMA = c2.TABLE_SCHEMA and c.TABLE_NAME = c2.TABLE_NAME and c2.COLUMN_KEY = 'PRI'),
              '\n * @return\n */\n@Select("SELECT ',
              group_concat(COLUMN_NAME), ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME, ' where ',
              (select group_concat(c2.COLUMN_NAME, ' = #{', c2.COLUMN_NAME, '}' separator ' AND ') from information_schema.COLUMNS c2 where c.TABLE_SCHEMA = c2.TABLE_SCHEMA and c.TABLE_NAME = c2.TABLE_NAME and c2.COLUMN_KEY = 'PRI'),
              '")\n', camel(TABLE_NAME), ' get(',
              (select trim(group_concat(' @Param("', c2.COLUMN_NAME, '") ',
                                        case c2.DATA_TYPE
                                            when 'bit' then 'bool'
                                            when 'tinyint' then 'byte'
                                            when 'smallint' then 'short'
                                            when 'int' then 'int'
                                            when 'bigint' then 'long'
                                            when 'decimal' then 'float'
                                            else 'String' end, ' ', c2.COLUMN_NAME))
               from information_schema.COLUMNS c2
               where c.TABLE_SCHEMA = c2.TABLE_SCHEMA
                 and c.TABLE_NAME = c2.TABLE_NAME
                 and c2.COLUMN_KEY = 'PRI'), ');\n')                                                                                                 AS GETS,
       concat('/**\n * 查询',
              (select TABLE_COMMENT from information_schema.TABLES t where t.TABLE_SCHEMA = c.TABLE_SCHEMA and t.TABLE_NAME = c.TABLE_NAME),
              '\n * \n * @param ', TABLE_NAME, ' 查询条件对象\n * @return\n */\n@Select({"<script>SELECT ',
              group_concat(COLUMN_NAME), ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME, ' <where>",',
              group_concat('\n"<if test=''', COLUMN_NAME, ' != null''>AND ', COLUMN_NAME, ' = #{', COLUMN_NAME, '}</if>"'),
              ',\n"</where>ORDER BY ', trim(group_concat(if(COLUMN_KEY = 'PRI', concat(COLUMN_NAME, ' DESC'), '') separator ' ')),
              '</script>"})\nList<', camel(TABLE_NAME), '> list(@Param("', TABLE_NAME, '") ', camel(TABLE_NAME), ' ', TABLE_NAME, ');\n')            AS LISTS,
       concat('/**\n * 添加数据到',
              (select TABLE_COMMENT from information_schema.TABLES t where t.TABLE_SCHEMA = c.TABLE_SCHEMA and t.TABLE_NAME = c.TABLE_NAME),
              '\n * \n * @param ', TABLE_NAME, ' 添加对象\n */\n@Options(useGeneratedKeys = true, keyProperty = "',
              (select group_concat(c2.COLUMN_NAME) from information_schema.COLUMNS c2 where c.TABLE_SCHEMA = c2.TABLE_SCHEMA and c.TABLE_NAME = c2.TABLE_NAME and c2.COLUMN_KEY = 'PRI'),
              '")\n@Insert({"INSERT INTO ', TABLE_SCHEMA, '.', TABLE_NAME, '(', trim(group_concat(' ', COLUMN_NAME)), ') VALUES",\n"(',
              trim(group_concat(' #{', COLUMN_NAME, '}')), ')"})\nvoid add(@Param("', TABLE_NAME, '") ', camel(TABLE_NAME), ' ', TABLE_NAME, ');\n') AS INSERTS,
       concat('/**\n * 更新数据到',
              (select TABLE_COMMENT from information_schema.TABLES t where t.TABLE_SCHEMA = c.TABLE_SCHEMA and t.TABLE_NAME = c.TABLE_NAME),
              '\n * \n * @param ', TABLE_NAME, ' 更新对象\n */\n@Update({"<script>UPDATE ', TABLE_SCHEMA, '.', TABLE_NAME, '<set>",\n',
              group_concat('"<if test=''', COLUMN_NAME, ' != null''>', COLUMN_NAME, ' = ', '#{', COLUMN_NAME, '}</if>",' separator '\n'), '\n"</set><where>',
              (select group_concat(c2.COLUMN_NAME, ' = #{', c2.COLUMN_NAME, '}' separator ' AND ') from information_schema.COLUMNS c2 where c.TABLE_SCHEMA = c2.TABLE_SCHEMA and c.TABLE_NAME = c2.TABLE_NAME and c2.COLUMN_KEY = 'PRI'),
              '</where></script>"})\nvoid update(@Param("', TABLE_NAME, '") ', camel(TABLE_NAME), ' ', TABLE_NAME, ');\n')                           AS UPDATES,
       concat('/**\n * 从', (select TABLE_COMMENT from information_schema.TABLES t where t.TABLE_SCHEMA = c.TABLE_SCHEMA and t.TABLE_NAME = c.TABLE_NAME), '删除数据\n *',
              (select group_concat('\n * @param ', c2.COLUMN_NAME, ' ', c2.COLUMN_COMMENT separator '') from information_schema.COLUMNS c2 where c.TABLE_SCHEMA = c2.TABLE_SCHEMA and c.TABLE_NAME = c2.TABLE_NAME and c2.COLUMN_KEY = 'PRI'),
              '\n */\n@Delete("DELETE FROM ', TABLE_SCHEMA, '.', TABLE_NAME, ' WHERE ',
              (select group_concat(c2.COLUMN_NAME, ' = #{', c2.COLUMN_NAME, '}' separator ' AND ') from information_schema.COLUMNS c2 where c.TABLE_SCHEMA = c2.TABLE_SCHEMA and c.TABLE_NAME = c2.TABLE_NAME and c2.COLUMN_KEY = 'PRI'),
              '")\nvoid delete(', (select trim(group_concat(' @Param("', c2.COLUMN_NAME, '") ',
                                                            case c2.DATA_TYPE
                                                                when 'bit' then 'bool'
                                                                when 'tinyint' then 'byte'
                                                                when 'smallint' then 'short'
                                                                when 'int' then 'int'
                                                                when 'bigint' then 'long'
                                                                when 'decimal' then 'float'
                                                                else 'String' end, ' ', c2.COLUMN_NAME))
                                   from information_schema.COLUMNS c2
                                   where c.TABLE_SCHEMA = c2.TABLE_SCHEMA
                                     and c.TABLE_NAME = c2.TABLE_NAME
                                     and c2.COLUMN_KEY = 'PRI'),
              ');')                                                                                                                                  AS DELETES
from information_schema.COLUMNS c
where TABLE_SCHEMA = 'zero'
group by TABLE_NAME;
