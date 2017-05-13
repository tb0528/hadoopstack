#!/bin/bash
#############################################
# Info:  m_bbs_user_tags
# 用户标签表  发贴喜好时段
#############################################

############# Param init ...... #############
# 目录结尾必须加"/";文件必须是完整路径
# local-本地文件;mfs-MFS文件;hdfs-HDFS文件;hive-HIVE表数据;db-数据库表


##hive 学员标签表
HIVE_M_BBS_USER_TAGS="m_bbs_user_tags"
set -o pipefail
function run_process()
{ 
    
     ##计算不同时段用户发帖量  获取最喜欢发贴的时间段 维度:时
	 ##主要时段 非大众喜好时段:0~8 12~13 18~19  大众喜好时段:9~11 14~17 20~23
    sql="
		use data_ods;
		add jar /root/xxo/work/hive_udf/hiveUDF.jar;
        create temporary function time_format as 'cn.xxo.hive.udf.time.FormatTime';
        create temporary function max_favoritetime as 'cn.xxo.hive.udf.resource.MaxFavoriteTime';
        insert overwrite table data_middle.$HIVE_M_BBS_USER_TAGS
        partition (tag_type='fatiexihao')
        select 
        	user_tags.authorid,
        	tag_define.id,
        	max_favoritetime(map('凌晨(0~8)',user_tags.time_0_8,'上午(9~11)',user_tags.time_9_11,'中午(12~13)',user_tags.time_12_13,'下午(14~17)',user_tags.time_14_17,'傍晚(18~19)',user_tags.time_18_19,'晚上(20~23)',user_tags.time_20_23)) as favorite_time
        from
        (
			select   
					sum(if(time_format(forum_post.dateline,'HH')>='00' and time_format(forum_post.dateline,'HH')<='08',1,0)) as time_0_8,
					sum(if(time_format(forum_post.dateline,'HH')>='09' and time_format(forum_post.dateline,'HH')<='11',1,0)) as time_9_11,
					sum(if(time_format(forum_post.dateline,'HH')>='12' and time_format(forum_post.dateline,'HH')<='13',1,0)) as time_12_13,
					sum(if(time_format(forum_post.dateline,'HH')>='14' and time_format(forum_post.dateline,'HH')<='17',1,0)) as time_14_17,
					sum(if(time_format(forum_post.dateline,'HH')>='18' and time_format(forum_post.dateline,'HH')<='19',1,0)) as time_18_19,
					sum(if(time_format(forum_post.dateline,'HH')>='20' and time_format(forum_post.dateline,'HH')<='24',1,0)) as time_20_23,
					forum_post.authorid
			from d_bbs_forum_post forum_post where forum_post.fid in 
			(
				select fid from d_bbs_forum_forum where fup=69
			) and trim(forum_post.subject)='' 
			group by forum_post.authorid
        ) user_tags left join 
        d_bbs_tag_define tag_define on tag_define.tag_name=max_favoritetime(map('凌晨(0~8)',user_tags.time_0_8,'上午(9~11)',user_tags.time_9_11,'中午(12~13)',user_tags.time_12_13,'下午(14~17)',user_tags.time_14_17,'傍晚(18~19)',user_tags.time_18_19,'晚上(20~23)',user_tags.time_20_23))
		"
     hive -e "$sql"
  } 

run_process $*
if [ $? -ne 0 ]
then
        exit 255
fi
exit 0  