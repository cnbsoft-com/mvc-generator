<#include "common.tpl">
package ${packagePath}.${persistencePath}.<@toAllLow source=tableName />;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import ${packagePath}.${modelPath}.<@toClass source=tableName />;

/**
 * <@toClass source=tableName /> Annotation-based MyBatis Mapper
 */
@Repository
@Mapper
public interface <@toClass source=tableName />Mapper {

    @Select({
        "<script>",
        "SELECT <#list columns as col>${col.columnName?upper_case}<#if col_has_next>, </#if></#list>",
        "FROM ${tableName?upper_case}",
        "<where>",
        <#list columns as column>
        <#if column.primaryKey == true>
        "    <if test='<@toField source=column.columnName /> != null'>AND ${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}</if>",
        </#if>
        </#list>
        "</where>",
        "</script>"
    })
    <@toClass source=tableName /> get(<@toClass source=tableName /> <@toField source=tableName />);

    @Select({
        "<script>",
        "SELECT <#list columns as col>${col.columnName?upper_case}<#if col_has_next>, </#if></#list>",
        "FROM ${tableName?upper_case}",
        "<where>",
        <#list columns as column>
        "    <if test='<@toField source=column.columnName /> != null and !<@toField source=column.columnName />.equals(&quot;&quot;)'>AND ${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}</if>",
        </#list>
        "</where>",
        "</script>"
    })
    List${"<"}<@toClass source=tableName />${">"} getList(<@toClass source=tableName /> <@toField source=tableName />);

    @Insert({
        "<script>",
        "INSERT INTO ${tableName?upper_case}",
        "<trim prefix='(' suffix=')' suffixOverrides=','>",
        <#list columns as column>
        "    <if test='<@toField source=column.columnName /> != null'>${column.columnName?upper_case},</if>",
        </#list>
        "</trim>",
        "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
        <#list columns as column>
        "    <if test='<@toField source=column.columnName /> != null'>${"#{"}<@toField source=column.columnName />},</if>",
        </#list>
        "</trim>",
        "</script>"
    })
    int create(<@toClass source=tableName /> <@toField source=tableName />);

    @Update({
        "<script>",
        "UPDATE ${tableName?upper_case}",
        "<set>",
        <#list columns as column>
        <#if column.primaryKey == false>
        "    <if test='<@toField source=column.columnName /> != null'>${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />},</if>",
        </#if>
        </#list>
        "</set>",
        "<where>",
        <#list columns as column>
        <#if column.primaryKey == true>
        "    <if test='<@toField source=column.columnName /> != null'>AND ${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}</if>",
        </#if>
        </#list>
        "</where>",
        "</script>"
    })
    int update(<@toClass source=tableName /> <@toField source=tableName />);

    @Delete({
        "<script>",
        "DELETE FROM ${tableName?upper_case}",
        "<where>",
        <#list columns as column>
        <#if column.primaryKey == true>
        "    <if test='<@toField source=column.columnName /> != null'>AND ${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}</if>",
        </#if>
        </#list>
        "</where>",
        "</script>"
    })
    int delete(<@toClass source=tableName /> <@toField source=tableName />);

}
