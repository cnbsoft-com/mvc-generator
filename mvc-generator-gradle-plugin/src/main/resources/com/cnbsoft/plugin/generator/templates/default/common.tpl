<#macro toClass source>${source?replace('_', ' ')?capitalize?replace(' ','')}</#macro>

<#macro toField source>${source?replace('_', ' ')?capitalize?uncap_first?replace(' ','')}</#macro>

<#macro toMethod source>${source?replace('_', ' ')?capitalize?cap_first?replace(' ','')}</#macro>

<#macro toBookTitle source>${source?replace('_', ' ')?capitalize?cap_first}</#macro>

<#macro toAllLow source>${source?replace('_', '')?lower_case}</#macro>

<#macro fieldType source>${source?replace('java.lang.', '')}</#macro>

<#macro getClassName source><#list source?split('.') as name><#if name?has_next == false>${name}</#if></#list></#macro>