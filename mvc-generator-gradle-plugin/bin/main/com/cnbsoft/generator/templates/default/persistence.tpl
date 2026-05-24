<#include "common.tpl">
package ${packagePath}.${persistencePath}.<@toAllLow source=tableName />;

import org.springframework.stereotype.Repository;

import java.util.List;

import ${packagePath}.${modelPath}.<@toClass source=tableName />${modelSuffix};

/**
 * <@toClass source=tableName />${mapperSuffix} interface
 *
 */
@Repository
public interface <@toClass source=tableName />${mapperSuffix} {

	<@toClass source=tableName />${modelSuffix} get(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

	List${"<"}<@toClass source=tableName />${modelSuffix}${">"} getList(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

	int create(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

	int delete(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

	int update(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />);

}
