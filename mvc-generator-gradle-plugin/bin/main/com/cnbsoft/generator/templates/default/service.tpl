<#include "common.tpl">
package ${packagePath}.${servicePath}.<@toAllLow source=tableName />;

import java.util.List;

import ${packagePath}.${modelPath}.<@toClass source=tableName />;

public interface <@toClass source=tableName />Service {

	<@toClass source=tableName /> get(<@toClass source=tableName /> <@toField source=tableName />);
	
	List${"<"}<@toClass source=tableName />${">"} getList(<@toClass source=tableName /> <@toField source=tableName />);
	
	int create(<@toClass source=tableName /> <@toField source=tableName />);
	
	int delete(<@toClass source=tableName /> <@toField source=tableName />);
	
	int update(<@toClass source=tableName /> <@toField source=tableName />);
}
