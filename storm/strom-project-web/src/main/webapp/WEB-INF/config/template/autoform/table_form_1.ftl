<!--隐藏域-->
<#macro input_hidden item>
<input type="hidden" name="${item.name}"  value="${item.value}"/>
</#macro>

<!--文本输入框-->
<#macro input_text item>
<tr>
	<td width="10%">${item.name_cn}</td>
	<td width="40%">
	<input type="text" name="${item.name}"  value="${item.value}"/>
	</td>
</tr>
</#macro>

<!--下拉框-->
<#macro input_select item>
<tr>
	<td>${item.name_cn}</td>
	<td>
	<select id="${item.id}" name="${item.name}">
		<#list item.values as item>
			<option value="${item.value}">${item.name}</option>
		</#list>
	</select>
	</td>
</tr>
</#macro>


<!--迭代输出-->
</#macro table_rows items>
<#list items as item>
<#if item.style=='hidden'>
	<@input_hidden item=${item}/>
</#if>
<#if item.style=='text'>
	<@input_text item=${item}/>
</#if>
<#if item.style=='select'>
	<@input_select item=${item}/>
</#if>
</#list>
</#macro>



<form id="${form.id}" action="${form.action}" method="post">
	<table width="80%" align="center">
		<@table_rows items=${form.items}/>
	</table>

	<input type="submit" value="保存"/>
</form>