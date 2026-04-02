<#include "common.tpl">
package ${packagePath}.${persistencePath}.<@toAllLow source=tableName />;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import ${packagePath}.${modelPath}.<@toClass source=tableName />${modelSuffix};

/**
 * <@toClass source=tableName />${mapperSuffix} - Annotation-based MyBatis Mapper
 */
@Repository
@Mapper
public interface <@toClass source=tableName />${mapperSuffix} {

    @Select("""
            SELECT <#list columns as col>${col.columnName?upper_case}<#if col_has_next>, </#if></#list>
            FROM ${tableName?upper_case}
            WHERE <#assign pkFirst = true><#list columns as column><#if column.primaryKey == true><#if !pkFirst> AND </#if>${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}<#assign pkFirst = false></#if></#list>
            """)
    <@toClass source=tableName />${modelSuffix} get(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

    @Select("""
            SELECT <#list columns as col>${col.columnName?upper_case}<#if col_has_next>, </#if></#list>
            FROM ${tableName?upper_case}
            """)
    List${"<"}<@toClass source=tableName />${modelSuffix}${">"} getList(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

    @Insert("""
            INSERT INTO ${tableName?upper_case}
            (<#list columns as col>${col.columnName?upper_case}<#if col_has_next>, </#if></#list>)
            VALUES (<#list columns as col>${"#{"}<@toField source=col.columnName />}<#if col_has_next>, </#if></#list>)
            """)
    int create(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

    @Update("""
            UPDATE ${tableName?upper_case}
            SET <#assign setFirst = true><#list columns as column><#if column.primaryKey == false><#if !setFirst>, </#if>${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}<#assign setFirst = false></#if></#list>
            WHERE <#assign pkFirst = true><#list columns as column><#if column.primaryKey == true><#if !pkFirst> AND </#if>${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}<#assign pkFirst = false></#if></#list>
            """)
    int update(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

    @Delete("""
            DELETE FROM ${tableName?upper_case}
            WHERE <#assign pkFirst = true><#list columns as column><#if column.primaryKey == true><#if !pkFirst> AND </#if>${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}<#assign pkFirst = false></#if></#list>
            """)
    int delete(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

}
