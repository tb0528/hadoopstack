<#list courseList as item>
        <div class='xuexi-cc'>
            <img src='${crm}/images/bg_login.jpg' />
            <p>${item.course.name}</p>
            <a href='${crm}/learning/courseDetail?courseId=${item.course.id}' class='xuexi-dd'>课程详情</a>
        </div>
</#list>