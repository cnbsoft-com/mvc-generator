<#macro toClass source>${source?replace('_', ' ')?capitalize?replace(' ','')}</#macro>

<#macro toField source>${source?replace('_', ' ')?capitalize?uncap_first?replace(' ','')}</#macro>

<#macro toMethod source>${source?replace('_', ' ')?capitalize?cap_first?replace(' ','')}</#macro>

<#macro toBookTitle source>${source?replace('_', ' ')?capitalize?cap_first}</#macro>

<#macro toAllLow source>${source?replace('_', '')?lower_case}</#macro>

<#macro fieldType source>${source?replace('java.lang.', '')}</#macro>

<#macro getClassName source><#list source?split('.') as name><#if name?has_next == false>${name}</#if></#list></#macro>

<#-- DDD 패턴인 경우 도메인이 이미 packagePath에 포함되어 있으므로 subPath만 반환, 아닌 경우 domainPath를 추가하여 반환 -->
<#macro domainPackage subPath tableName useDdd><#if useDdd>${subPath}<#else>${subPath}.<@toAllLow source=tableName /></#if></#macro>

<#-- 서비스 패키지 경로 (구현체 import용) -->
<#macro servicePackage tableName><#if useDddPattern>${packagePath}.${servicePath}<#else>${packagePath}.${servicePath}.<@toAllLow source=tableName /></#if></#macro>