//弹出窗口
function openFullWin(url, title){
	window.open (url, title, ' left=10,top=10,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',scrollbars,resizable=yes,toolbar=no');
}
function openWin(url, title){
	window.open (url, title, 'height=500, width=700, top=0,left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
}

function openModal(url, title){
	window.showModalDialog(url, title, ' left=0,top=0,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',scrollbars,resizable=yes,toolbar=no'); 
}

function openModal(url, title, msg){
	window.showModalDialog(url, title, msg); 
}

function popMsg(msg){
	// 页面加载后 右下角 弹出窗口
	/**************/
	window.setTimeout(function(){
		$.messager.show({
			title:"消息提示",
			msg:msg,
			timeout:5000
		});
	},1000);
	/*************/
};

//睡觉
function sleep(seconds) {
    this.date = Math.round(new Date().getTime()/1000);
    while(1) {
        if(Math.round(new Date().getTime()/1000) - this.date >= seconds) break;
    }
    return true;
}

Number.prototype.formatMoney = function (places, symbol, thousand, decimal) {
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    symbol = symbol !== undefined ? symbol : "$";
    thousand = thousand || ",";
    decimal = decimal || ".";
    var number = this,
        negative = number < 0 ? "-" : "",
        i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
    return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};



////////////////////////////
var Provinces=new Array( 
		new Array("110000","北京市"), 
		new Array("120000","天津市"), 
		new Array("130000","河北省"), 
		new Array("140000","山西省"), 
		new Array("150000","内蒙古自治区"), 
		new Array("210000","辽宁省"), 
		new Array("220000","吉林省"), 
		new Array("230000","黑龙江省"), 
		new Array("310000","上海市"), 
		new Array("320000","江苏省"), 
		new Array("330000","浙江省"), 
		new Array("340000","安徽省"), 
		new Array("350000","福建省"), 
		new Array("360000","江西省"), 
		new Array("370000","山东省"), 
		new Array("410000","河南省"), 
		new Array("420000","湖北省"), 
		new Array("430000","湖南省"), 
		new Array("440000","广东省"), 
		new Array("450000","广西壮族自治区"), 
		new Array("460000","海南省"), 
		new Array("500000","重庆市"), 
		new Array("510000","四川省"), 
		new Array("520000","贵州省"), 
		new Array("530000","云南省"), 
		new Array("540000","西藏自治区"), 
		new Array("610000","陕西省"), 
		new Array("620000","甘肃省"), 
		new Array("630000","青海省"), 
		new Array("640000","宁夏回族自治区"), 
		new Array("650000","新疆维吾尔自治区"), 
		new Array("710000","台湾省"), 
		new Array("810000","香港特别行政区"), 
		new Array("820000","澳门特别行政区") 
		); 
