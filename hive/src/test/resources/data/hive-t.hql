create external table t7 (
    id int,
    name string,
    birthday date,
    online boolean
) row format delimited
 fields terminated by '\t';


create external table test (
    lime string
) row format delimited
 fields terminated by '\t';