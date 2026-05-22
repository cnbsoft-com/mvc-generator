<#macro toClass source>${source?replace('_', ' ')?capitalize?replace(' ','')}</#macro>

<#macro toField source>${source?replace('_', ' ')?capitalize?uncap_first?replace(' ','')}</#macro>

<#macro toMethod source>${source?replace('_', ' ')?capitalize?cap_first?replace(' ','')}</#macro>

<#macro toBookTitle source>${source?replace('_', ' ')?capitalize?cap_first}</#macro>

<#macro toAllLow source>${source?replace('_', '')?lower_case}</#macro>

<#macro fieldType source>${source?replace('java.lang.', '')}</#macro>

<#macro getClassName source><#list source?split('.') as name><#if name?has_next == false>${name}</#if></#list></#macro>

<#macro printImports columns>
<#assign imports = [] />
<#list columns as column>
	<#assign className = column.columnClassName />
	<#if className?contains('.') && !className?starts_with('java.lang.')>
		<#if !imports?seq_contains(className)>
			<#assign imports = imports + [className] />
		</#if>
	</#if>
</#list>
<#list imports?sort as import>
import ${import};
</#list>
</#macro>

<#macro servicePackage tableName>
<#if useDddPattern>${packagePath}.<@toAllLow source=tableName />.${servicePath}
<#else>${packagePath}.${servicePath}.<@toAllLow source=tableName />
</#if>
</#macro>

<#macro mapperPackage tableName>
<#if useDddPattern>${packagePath}.<@toAllLow source=tableName />.${persistencePath}
<#else>${packagePath}.${persistencePath}.<@toAllLow source=tableName />
</#if>
</#macro>