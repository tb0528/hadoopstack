--insert into table t10_1 select * from t;
--insert into table t10_2 select * from t;
from t
insert overwrite table t10_1 select *
insert overwrite table t10_2 select *;

