<#include "common.tpl">
package ${packagePath}.${modelPath};

import org.apache.ibatis.type.Alias;

@Alias("<@toClass source=tableName />")
public class <@toClass source=tableName />${modelSuffix} {

	<#list columns as column>
    private <@fieldType source=column.columnClassName /> <@toField source=column.columnName />;
	</#list>

    private <@toClass source=tableName />${modelSuffix}() {}

    private <@toClass source=tableName />${modelSuffix}(Builder builder) {
	<#list columns as column>
        this.<@toField source=column.columnName /> = builder.<@toField source=column.columnName />;
	</#list>
    }

    public static Builder builder() {
        return new Builder();
    }

	<#list columns as column>
    public <@fieldType source=column.columnClassName /> get<@toMethod source=column.columnName />() {
        return this.<@toField source=column.columnName />;
    }
    public void set<@toMethod source=column.columnName />(<@fieldType source=column.columnClassName /> <@toField source=column.columnName />) {
        this.<@toField source=column.columnName /> = <@toField source=column.columnName />;
    }
	</#list>

    public static class Builder {
	<#list columns as column>
        private <@fieldType source=column.columnClassName /> <@toField source=column.columnName />;
	</#list>

	<#list columns as column>
        public Builder <@toField source=column.columnName />(<@fieldType source=column.columnClassName /> <@toField source=column.columnName />) {
            this.<@toField source=column.columnName /> = <@toField source=column.columnName />;
            return this;
        }
	</#list>

        public <@toClass source=tableName />${modelSuffix} build() {
            return new <@toClass source=tableName />${modelSuffix}(this);
        }
    }
}
