#!/bin/sh

source /etc/profile

start_i=1
end_j=0

while [ $start_i -gt $end_j  ]
do

##设置起始日期
startDay=`date -d "$start_i day ago"  +%Y-%m-%d`
echo $startDay

##设置结束日期
let temp=start_i-1
endDay=`date -d "$temp day ago"  +%Y-%m-%d`
echo $endDay

##导入语句
sqoop import \
--connect jdbc:mysql://192.168.1.155:3306/test \
--username root \
--password haha \
--query "select * from t_user_info where time >  '$startDay' and time < '$endDay' and \$CONDITIONS " \
--fields-terminated-by '\t' -m 2 \
--split-by id \
--target-dir data/$startDay \
--delete-target-dir

let start_i=start_i-1
done