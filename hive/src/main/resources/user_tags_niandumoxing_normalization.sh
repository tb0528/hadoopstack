#!/bin/bash
#############################################
# Info:用户粘度模型   模型数据归一化 标准归一化
#############################################

############# Param init ...... #############
# 目录结尾必须加"/";文件必须是完整路径
# local-本地文件;mfs-MFS文件;hdfs-HDFS文件;hive-HIVE表数据;db-数据库表

#所有参数
ALL_PARAMS=""
MAX_POSTS=0
MIN_POSTS=0
MAX_OLTIME=0
MIN_OLTIME=0
MAX_REGDATE=0
MIN_REGDATE=0
MAX_LASTVISIT=0
MIN_LASTVISTI=0
MAX_LASTPOST=0
MIN_LASTPOST=0

##hive 学员标签表
HIVE_M_BBS_USER_TAGS="m_bbs_user_tags"
##hive 模型数据表 归一化
HIVE_M_BBS_ACTIVE_MODEL_NORMALIZATION="m_bbs_active_model_normalization"
set -o pipefail
function run_process()
{ 
	
	sql="
		use data_middle;
		CREATE TABLE IF NOT EXISTS  $HIVE_M_BBS_ACTIVE_MODEL_NORMALIZATION (
				user_id string  COMMENT '学员id',
				posts double   COMMENT '发贴量',
				oltime double  COMMENT '在线时长时',
				regdate_days double	 COMMENT '注册距离现在的时间单位天',
				lastvisit_days double	 COMMENT '最后一次访问距离现在的时间单位天',
				lastpost_days double	 COMMENT '最后一次发贴距离现在的时间单位天'
		) COMMENT '模型归一化后表'
		ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
    "
    hive -e "$sql"
     if [ $? -ne 0 ]
    then
            exit 255
    fi
     ##获取所有参数 最大值、最小值
    sql="
		use data_middle;
        select
        	max(posts),min(posts),
        	max(oltime),min(oltime),
        	abs(max(regdate_days)),abs(min(regdate_days)),
        	abs(max(lastvisit_days)),abs(min(lastvisit_days)),
        	abs(max(lastpost_days)),abs(min(lastpost_days))
        from m_bbs_active_model_base where groupid not in(1,2,3) ;
		"
		ALL_PARAMS=`hive -e "$sql"`
		arr=(${ALL_PARAMS//\t/ })  
		MAX_POSTS=${arr[0]}
		MIN_POSTS=${arr[1]}
		MAX_OLTIME=${arr[2]}
		MIN_OLTIME=${arr[3]}
		MAX_REGDATE=${arr[4]}
		MIN_REGDATE=${arr[5]}
		MAX_LASTVISIT=${arr[6]}
		MIN_LASTVISTI=${arr[7]}
		MAX_LASTPOST=${arr[8]}
		MIN_LASTPOST=${arr[9]} 
		echo "***********"$MIN_LASTVISTI
	
    if [ $? -ne 0 ]
    then
            exit 255
    fi
     ##模型数据归一化
    sql="
		use data_middle;
		set hive.exec.parallel=true;
		insert overwrite table data_middle.$HIVE_M_BBS_ACTIVE_MODEL_NORMALIZATION
        select
        	user_id,
        	(posts-'$MIN_POSTS')/('$MAX_POSTS'-'$MIN_POSTS') as posts,
        	(oltime-'$MIN_OLTIME')/('$MAX_OLTIME'-'$MIN_OLTIME') as oltime,
        	(abs(regdate_days)-'$MIN_REGDATE')/('$MAX_REGDATE'-'$MIN_REGDATE') as regdate_days,
        	(abs(lastvisit_days)-'$MIN_LASTVISTI')/('$MAX_LASTVISIT'-'$MIN_LASTVISTI') as lastvisit_days,
        	(abs(lastpost_days)-'$MIN_LASTPOST')/('$MAX_LASTPOST'-'$MIN_LASTPOST') as lastpost_days
        from m_bbs_active_model_base where groupid not in (1,2,3) and posts is not null ;        
		"
echo $sql
     hive -e "$sql"
    
  } 

run_process $*
if [ $? -ne 0 ]
then
        exit 255
fi
exit 0  