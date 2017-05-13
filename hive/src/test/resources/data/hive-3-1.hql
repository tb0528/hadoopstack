--create table t3_map(
--    id int,
--    name string,
--    address map<string,string>
--) row format delimited
--fields terminated by '\t';

create table t3_map_1(
    id int,
    name string,
    address map<string,string>
) row format delimited
fields terminated by '\t'
collection items terminated by ','
map keys terminated by ':'
;
