--create table t2_arr(
--    id int,
--    name string,
--    hobby array<string>
--) row format delimited
--fields terminated by '\t';
create table t2_arr_1(
    id int,
    name string,
    hobby array<string>
) row format delimited
fields terminated by '\t'
collection items terminated by ','
;
