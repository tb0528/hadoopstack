{
    title : {
        text: '${chartView.text}',
        subtext: '${chartView.subText}',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient : 'vertical',
        x : 'left',
        data:[${chartView.legendData}]
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    series : [
        {
            name:'',
            type:'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
            <#list chartView.dataList as item> 
                {value:${item[1]}, name:'${item[0]}'}
                <#if item_has_next>,</#if>
            </#list>
            ]
        }
    ]
}