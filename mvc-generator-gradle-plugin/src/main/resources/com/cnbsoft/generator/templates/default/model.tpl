<#include "common.tpl">
package ${packagePath}.${modelPath};

import org.apache.ibatis.type.Alias;

<#assign capitalizedTableName="${tableName?replace('_', ' ')?capitalize?replace(' ','')}">

@Alias("<@toClass source=tableName />")
public class <@toClass source=tableName /> {

	<#list columns as column>
    private <@fieldType source=column.columnClassName /> <@toField source=column.columnName />;
	</#list>
	
	<#list columns as column>
    public <@fieldType source=column.columnClassName /> get<@toMethod source=column.columnName />() {
        return this.<@toField source=column.columnName />;
    }
    public void set<@toMethod source=column.columnName />(<@fieldType source=column.columnClassName /> <@toField source=column.columnName />) {
        this.<@toField source=column.columnName /> = <@toField source=column.columnName />;
    }
	</#list>
}
