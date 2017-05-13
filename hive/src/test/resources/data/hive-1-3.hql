create table t1_3(
    id int comment 'ID',
    name string comment "stu's name",
    birthday date comment "stu's birthday",
    online boolean comment "stu's 是否online"
)
row format delimited
fields terminated by '\t';
