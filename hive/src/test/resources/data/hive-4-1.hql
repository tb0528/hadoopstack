create table t4_struct(
    id int,
    name string,
    np struct<province:string, city:string, zip:int>
)
row format delimited
fields terminated by '\t'
collection items terminated by ','
;
