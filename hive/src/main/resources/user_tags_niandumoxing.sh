#!/bin/bash
#############################################
# Info:用户活跃度模型  
#############################################

############# Param init ...... #############
# 目录结尾必须加"/";文件必须是完整路径
# local-本地文件;mfs-MFS文件;hdfs-HDFS文件;hive-HIVE表数据;db-数据库表

##hive 学员标签表
HIVE_M_BBS_USER_TAGS="m_bbs_user_tags"
##hive 粘度模型数据 中间表
HIVE_M_BBS_ACTIVE_MODEL_BASE="m_bbs_active_model_base"
set -o pipefail
function run_process()
{ 
	
	sql="
		use data_middle;
        CREATE TABLE IF NOT EXISTS  $HIVE_M_BBS_ACTIVE_MODEL_BASE (
				user_id string  COMMENT '学员id',
				groupid int COMMENT  '会员组id',
				group_name string COMMENT '组名称',
				posts bigint   COMMENT '发贴量',
				oltime int  COMMENT '在线时长时',
				regdate_days bigint	 COMMENT '注册距离现在的时间单位天',
				regdate bigint	 COMMENT '注册时间',
				lastvisit_days bigint	 COMMENT '最后一次访问距离现在的时间单位天',
				lastvisit bigint	 COMMENT '最后一次访问时间',
				lastpost_days bigint	 COMMENT '最后一次发贴距离现在的时间单位天',
				lastpost bigint	 COMMENT '最后一次发贴时间'
		) COMMENT '模型基础表'
		ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
    "
    hive -e "$sql"
    if [ $? -ne 0 ]
    then
            exit 255
    fi
     ##模型基础表
    sql="
		use data_ods;
		set hive.exec.parallel=true;
		insert overwrite table data_middle.$HIVE_M_BBS_ACTIVE_MODEL_BASE
        select
        	member.uid,
        	member.groupid,
        	onlinelist.title,
        	member_count.posts,
        	member_count.oltime,
        	datediff('$1',from_unixtime(member.regdate,'yyyy-MM-dd')) as regdate_days,
        	member.regdate, 
        	datediff('$1',from_unixtime(member_status.lastvisit,'yyyy-MM-dd')) as lastvisit_days,
        	member_status.lastvisit, 
        	datediff('$1',from_unixtime(member_status.lastpost,'yyyy-MM-dd')) as lastpost_days,
        	member_status.lastpost
        from d_bbs_common_member member  
        left join d_bbs_common_member_status member_status on member.uid=member_status.uid
        left join d_bbs_common_member_count member_count on member.uid=member_count.uid
        left join d_bbs_forum_onlinelist onlinelist on member.groupid=onlinelist.groupid;
		"
     hive -e "$sql"
    
  } 

run_process $*
if [ $? -ne 0 ]
then
        exit 255
fi
exit 0  