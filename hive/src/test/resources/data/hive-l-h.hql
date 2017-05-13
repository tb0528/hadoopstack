create table t_u_a as 
select u.uid, max(u.name) name, concat_ws(",",collect_set(a.address)) as addr 
from t_u u join t_addr a on u.uid=a.uid 
group by u.uid;


