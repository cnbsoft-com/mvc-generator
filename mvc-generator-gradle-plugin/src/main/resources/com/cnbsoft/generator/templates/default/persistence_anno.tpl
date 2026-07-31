<#include "common.tpl">
package ${packagePath}.<@domainPackage subPath=persistencePath tableName=tableName useDdd=useDddPattern />;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import ${packagePath}.${modelPath}.<@toClass source=tableName />${modelSuffix};

<#assign persistencePackage><@domainPackage subPath=persistencePath tableName=tableName useDdd=useDddPattern /></#assign>
<#assign mapperClassName><@toClass source=tableName />${mapperSuffix}</#assign>
<#assign mapperFqcn = packagePath + "." + persistencePackage + "." + mapperClassName>
<#assign findByMethodName>findBy<#list columns as column><#if column.primaryKey == true><@toMethod source=column.columnName /></#if></#list></#assign>

/**
 * <@toClass source=tableName />${mapperSuffix} - Annotation-based MyBatis Mapper
 */
@Repository
@Mapper
public interface <@toClass source=tableName />${mapperSuffix} {

    @Select("""
            select /* ${mapperFqcn}.get */
            <#list columns as col>    <#if col_index == 0>  <#else>, </#if><#if (col.comment!"")?has_content>${col.columnName?lower_case?right_pad(20)}/* ${col.comment} */<#else>${col.columnName?lower_case}</#if>
            </#list>     from ${tableName?lower_case}
             where <#assign pkFirst = true><#list columns as column><#if column.primaryKey == true><#if !pkFirst> and </#if>${column.columnName?lower_case} = ${"#{"}<@toField source=column.columnName />}<#assign pkFirst = false></#if></#list>
            """)
    <@toClass source=tableName />${modelSuffix} get(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

    @Select("""
            select /* ${mapperFqcn}.${findByMethodName} */
            <#list columns as col>    <#if col_index == 0>  <#else>, </#if><#if (col.comment!"")?has_content>${col.columnName?lower_case?right_pad(20)}/* ${col.comment} */<#else>${col.columnName?lower_case}</#if>
            </#list>     from ${tableName?lower_case}
             where <#assign pkFirst = true><#list columns as column><#if column.primaryKey == true><#if !pkFirst> and </#if>${column.columnName?lower_case} = ${"#{"}<@toField source=column.columnName />}<#assign pkFirst = false></#if></#list>
            """)
    <@toClass source=tableName />${modelSuffix} ${findByMethodName}(<#assign pkFirst = true><#list columns as column><#if column.primaryKey == true><#if !pkFirst>, </#if>@Param("<@toField source=column.columnName />") <@fieldType source=column.columnClassName /> <@toField source=column.columnName /><#assign pkFirst = false></#if></#list>);

    @Select("""
            select /* ${mapperFqcn}.getList */
            <#list columns as col>    <#if col_index == 0>  <#else>, </#if><#if (col.comment!"")?has_content>${col.columnName?lower_case?right_pad(20)}/* ${col.comment} */<#else>${col.columnName?lower_case}</#if>
            </#list>     from ${tableName?lower_case}
            """)
    List${"<"}<@toClass source=tableName />${modelSuffix}${">"} getList(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

    @Insert("""
            INSERT INTO <@tableCase source=tableName />
            (<#list columns as col>${col.columnName?upper_case}<#if col_has_next>, </#if></#list>)
            VALUES (<#list columns as col>${"#{"}<@toField source=col.columnName />}<#if col_has_next>, </#if></#list>)
            """)
    int create(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

    @Update("""
            UPDATE <@tableCase source=tableName />
            SET <#assign setFirst = true><#list columns as column><#if column.primaryKey == false><#if !setFirst>, </#if>${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}<#assign setFirst = false></#if></#list>
            WHERE <#assign pkFirst = true><#list columns as column><#if column.primaryKey == true><#if !pkFirst> AND </#if>${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}<#assign pkFirst = false></#if></#list>
            """)
    int update(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

    @Delete("""
            DELETE FROM <@tableCase source=tableName />
            WHERE <#assign pkFirst = true><#list columns as column><#if column.primaryKey == true><#if !pkFirst> AND </#if>${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}<#assign pkFirst = false></#if></#list>
            """)
    int delete(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

}
