<#macro toClass source>${source?replace('_', ' ')?capitalize?replace(' ','')}</#macro>

<#macro toField source>${source?replace('_', ' ')?capitalize?uncap_first?replace(' ','')}</#macro>

<#macro toMethod source>${source?replace('_', ' ')?capitalize?cap_first?replace(' ','')}</#macro>

<#macro toBookTitle source>${source?replace('_', ' ')?capitalize?cap_first}</#macro>

<#macro toAllLow source>${source?replace('_', '')?lower_case}</#macro>

<#-- Mapper SQL의 테이블명 케이스 (useModelUpperCase 옵션에 따라 대/소문자 결정, 컬럼명에는 영향 없음) -->
<#macro tableCase source><#if useModelUpperCase>${source?upper_case}<#else>${source?lower_case}</#if></#macro>

<#macro fieldType source>${source?replace('java.lang.', '')}</#macro>

<#macro getClassName source><#list source?split('.') as name><#if name?has_next == false>${name}</#if></#list></#macro>

<#-- DDD 패턴인 경우 도메인이 이미 packagePath에 포함되어 있으므로 subPath만 반환, 아닌 경우 domainPath를 추가하여 반환 -->
<#macro domainPackage subPath tableName useDdd><#if useDdd>${subPath}<#else>${subPath}.<@toAllLow source=tableName /></#if></#macro>

<#-- 서비스 패키지 경로 (구현체 import용) -->
<#macro servicePackage tableName><#if useDddPattern>${packagePath}.${servicePath}<#else>${packagePath}.${servicePath}.<@toAllLow source=tableName /></#if></#macro>