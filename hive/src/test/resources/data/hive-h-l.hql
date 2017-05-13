--create table t_u(
--uid int,
--name string)
--row format delimited
--fields terminated by '\t';
--create table t_addr(
--aid int,
--address string)
--row format delimited
--fields terminated by '\t'; 

select u.uid, max(u.name) name, concat_ws(",",collect_set(a.address)) as addr 
from t_u u join t_addr a on u.uid=a.uid 
group by u.uid;


