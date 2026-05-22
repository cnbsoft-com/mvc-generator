<#include "common.tpl">
package ${packagePath}.${servicePath}.<@toAllLow source=tableName />;

import java.util.List;

import ${packagePath}.${modelPath}.<@toClass source=tableName />${modelSuffix};
<@printImports columns />

public interface <@toClass source=tableName />${serviceSuffix} {

	<@toClass source=tableName />${modelSuffix} get();

	List${"<"}<@toClass source=tableName />${modelSuffix}${">"} getList(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

	int create(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

	int delete(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

	int update(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);
}
