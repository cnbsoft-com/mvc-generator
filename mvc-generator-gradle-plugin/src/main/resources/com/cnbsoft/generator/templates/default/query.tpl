<#include "common.tpl">
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD ${mapperSuffix} 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${packagePath}.${persistencePath}.${modelName}.<@toClass source=tableName />${mapperSuffix}">
	<resultMap id="<@toClass source=tableName />ResultMap" type="<@toClass source=tableName />">
		<#list columns as column>
		<result property="<@toField source=column.columnName />" column="${column.columnName?upper_case}" />
		</#list>
	</resultMap>
	<sql id="baseColumnList">
		<#list columns as column>${column.columnName?upper_case}<#if column_has_next>, </#if></#list>
	</sql>

	<select id="get" parameterType="<@toClass source=tableName />" resultMap="<@toClass source=tableName />ResultMap">
		SELECT <include refid="${packagePath}.${persistencePath}.${modelName}.<@toClass source=tableName />${mapperSuffix}.baseColumnList" />
		FROM ${tableName?upper_case}
		<#list columns as column><#if column.primaryKey == true><#if column_index == 0>WHERE <#else>AND </#if> ${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}</#if></#list>
	</select>

	<select id="getList" parameterType="<@toClass source=tableName />" resultMap="<@toClass source=tableName />ResultMap">
		SELECT <include refid="${packagePath}.${persistencePath}.${modelName}.<@toClass source=tableName />${mapperSuffix}.baseColumnList" />
		FROM ${tableName?upper_case}
		<where>
			<#list columns as column>
			<if test="<@toField source=column.columnName /> != null and !<@toField source=column.columnName /> eq ''.toString()">
				AND ${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}
			</if>
			</#list>
		</where>
	</select>

	<insert id="create" parameterType="<@toClass source=tableName />">
		INSERT INTO ${tableName?upper_case}
		<trim prefix="(" suffix=")" suffixOverrides=",">
			<#list columns as column>
			<if test="<@toField source=column.columnName /> != null">
				${column.columnName?upper_case},
			</if>
			</#list>
		</trim>
		<trim prefix="VALUES (" suffix=")" suffixOverrides=",">
			<#list columns as column>
			<if test="<@toField source=column.columnName /> != null">
				${"#{"}<@toField source=column.columnName />},
			</if>
			</#list>
		</trim>
	</insert>

	<update id="update" parameterType="<@toClass source=tableName />">
		UPDATE ${tableName?upper_case}
		<set>
			<#list columns as column>
			<if test="<@toField source=column.columnName /> != null">
				${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />},
			</if>
			</#list>
		</set>
		<#list columns as column><#if column.primaryKey == true><#if column_index == 0>WHERE <#else>AND </#if> ${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}</#if></#list>
	</update>

	<delete id="delete" parameterType="<@toClass source=tableName />">
		DELETE FROM ${tableName?upper_case}
		<#list columns as column><#if column.primaryKey == true><#if column_index == 0>WHERE <#else>AND </#if> ${column.columnName?upper_case} = ${"#{"}<@toField source=column.columnName />}</#if></#list>
	</delete>

</mapper>