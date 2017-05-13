#!/bin/bash
#############################################
# Info:  m_bbs_user_tags
# 用户标签表  用户活跃度 发贴频次
#############################################

############# Param init ...... #############
# 目录结尾必须加"/";文件必须是完整路径
# local-本地文件;mfs-MFS文件;hdfs-HDFS文件;hive-HIVE表数据;db-数据库表

##平均发贴频次
AVG_FRE_NUM=0.0

##hive 学员标签表
HIVE_M_BBS_USER_TAGS="m_bbs_user_tags"

set -o pipefail
function run_process()
{ 
    
     ##计算2015年以来平均发贴频次平均发贴频次
    sql="
		use data_ods; 
        select count(forum_thread.tid)/(abs(datediff('2015-01-01','$1'))*count(distinct forum_thread.authorid )) as avg_num from d_bbs_forum_thread forum_thread where forum_thread.fid in 
		(
			select fid from d_bbs_forum_forum where fup=69
		) and from_unixtime(forum_thread.dateline,'yyyy-MM-dd')>='2015-01-01';
		"
    AVG_FRE_NUM=`hive -e "$sql"`
    sql="
		use data_ods;
        insert overwrite table data_middle.$HIVE_M_BBS_USER_TAGS
        partition (tag_type='huoyuedu')
        select 
        	user_tags.authorid,
        	tag_define.id,
        	user_tags.tags_type
        from
        (
			select
				member.uid as authorid,
				if(tmp.tags_type is null,'沉睡用户',tags_type) as tags_type
			from d_bbs_common_member member  left join
			(
				select  
						forum_thread.authorid,
						(
							case 
								when (count(forum_thread.tid)/abs(datediff('2015-01-01','$1')))>='$AVG_FRE_NUM' then '活跃用户'
							else '偶尔用户' end
						) as tags_type
				from d_bbs_forum_thread forum_thread where forum_thread.fid in 
						(
							select fid from d_bbs_forum_forum where fup=69
						) and from_unixtime(forum_thread.dateline,'yyyy-MM-dd')>='2015-01-01' group by forum_thread.authorid
    		)tmp on member.uid=tmp.authorid
         ) user_tags left join 
        d_bbs_tag_define tag_define on tag_define.tag_name=user_tags.tags_type
		"
     hive -e "$sql"
  } 

run_process $*
if [ $? -ne 0 ]
then
        exit 255
fi
exit 0  