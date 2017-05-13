{
	title : {
        text: '${chartView.text}',
        subtext: '${chartView.subText}'
    },
    tooltip : {
        formatter: "{a} <br/>{b} : {c}%"
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    series : [
        {
            name:'${chartView.seriesname}',
            type:'gauge',
            detail : {formatter:'{value}%'},
            data:[{value: ${chartView.datastr[1]}, name: '${chartView.datastr[0]}'}]
        }
    ]
}