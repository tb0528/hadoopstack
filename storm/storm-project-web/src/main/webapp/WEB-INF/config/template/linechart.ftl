{ title : {
        text: '${chartView.text}'
    },
tooltip : {
        trigger: 'axis'
    },
legend: {
        data:[${chartView.legendData}]
    },
toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
calculable : true,
xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            data : [${chartView.xaxisdata}]
        }
    ],
yAxis : [
        {
            type : 'value',
            axisLabel : {
                formatter: '{value} ${chartView.yaxislable}'
            }
        }
    ],
series : [
<#list chartView.dataList as item> 
			{
				name:'${item[0]}',
				type:'line',
				data:[${item[1]}],
				markPoint : {
					data : [
						{type : 'max', name: '最大值'},
						{type : 'min', name: '最小值'}
					]
				},
				markLine : {
					data : [
						{type : 'average', name: '平均值'}
					]
				}
			}

<#if item_has_next>,</#if>
</#list>
		]	
}