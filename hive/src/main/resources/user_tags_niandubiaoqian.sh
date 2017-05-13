#!/bin/bash
#############################################
# Info:  m_bbs_user_tags
# 用户标签表  用户粘度标签导入
#############################################

############# Param init ...... #############
# 目录结尾必须加"/";文件必须是完整路径
# local-本地文件;mfs-MFS文件;hdfs-HDFS文件;hive-HIVE表数据;db-数据库表


##hive 学员标签表
HIVE_M_BBS_USER_TAGS="m_bbs_user_tags"

set -o pipefail
function run_process()
{ 
    sql="
		use data_middle;
        insert overwrite table $HIVE_M_BBS_USER_TAGS
        partition (tag_type='niandubiaoqian')
        select 
        	user_tags.uid,
        	tag_define.id,
        	user_tags.tags_type
        from
        (
        	select
        			member.uid,
        			(
	        			case when kmeans_tmp.center_id='687' then '僵尸用户'
	        				 when kmeans_tmp.center_id='252' then '忠诚用户'
						else '新活力用户' end
					) as tags_type
	        from data_ods.d_bbs_common_member  member 
	        join m_bbs_kmeans_result_tmp kmeans_tmp on member.uid=kmeans_tmp.user_id
        ) user_tags  left join
        data_ods.d_bbs_tag_define tag_define on tag_define.tag_name=user_tags.tags_type
		"
     hive -e "$sql"
  } 

run_process $*
if [ $? -ne 0 ]
then
        exit 255
fi
exit 0  