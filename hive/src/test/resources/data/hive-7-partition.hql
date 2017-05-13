create table t7_partition_2(
    id int,
    name string
) partitioned by(school string, year int)
 row format delimited
fields terminated by '\t';
