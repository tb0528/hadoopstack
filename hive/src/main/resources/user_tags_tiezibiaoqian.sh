#!/bin/bash
#############################################
# Info:  m_bbs_user_tags
# 用户标签表
#计算用户的帖子标签
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
        CREATE TABLE IF NOT EXISTS  $HIVE_M_BBS_USER_TAGS (
				user_id string  COMMENT '学员id',
				tags_id string   COMMENT '标签id',
				tags string   COMMENT '标签名称'				
		) COMMENT '学员标签表'
		PARTITIONED BY ( tag_type string )
		ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
    "
    hive -e "$sql"
     if [ $? -ne 0 ]
    then
            exit 255
    fi
     ##计算学员帖子标签
    sql="
		use data_ods;
		set hive.exec.parallel=true;
		insert overwrite table data_middle.$HIVE_M_BBS_USER_TAGS
        partition (tag_type='tiezibiaoqian')
        select
        	user_tags.authorid,
        	concat_ws(',', collect_set(cast  (user_tags.id as string))),
        	concat_ws(',', collect_set(user_tags.name))
        from 
        (
			select   
				forum_post.authorid,
				tag_define.id,
				forum_forum.name
			from d_bbs_forum_post forum_post  join
			(
				select fid,name from d_bbs_forum_forum where fup=69 group by fid,name
			) forum_forum on forum_post.fid=forum_forum.fid and trim(forum_post.subject)=''
			left join
			d_bbs_tag_define tag_define on tag_define.tag_name=forum_forum.name
			group by forum_post.authorid,tag_define.id,forum_forum.name
        ) user_tags group by user_tags.authorid;
		"
     hive -e "$sql"
    
  } 

run_process $*
if [ $? -ne 0 ]
then
        exit 255
fi
exit 0  