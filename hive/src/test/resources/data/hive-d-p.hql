insert overwrite table t7_partition_3 partition(school,year=2016) 
select id, name,year from t7_partition_2;
