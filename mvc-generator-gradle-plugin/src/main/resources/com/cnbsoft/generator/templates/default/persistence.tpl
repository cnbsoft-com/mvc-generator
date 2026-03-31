<#include "common.tpl">
package ${packagePath}.${persistencePath}.<@toAllLow source=tableName />;

import org.springframework.stereotype.Repository;

import java.util.List;

import ${packagePath}.${modelPath}.<@toClass source=tableName />;

/**
 * <@toClass source=tableName />Mapper interface
 *
 */
@Repository
public interface <@toClass source=tableName />Mapper {

	<@toClass source=tableName /> get(<@toClass source=tableName /> <@toField source=tableName />);

	List${"<"}<@toClass source=tableName />${">"} getList(<@toClass source=tableName /> <@toField source=tableName />);

	int create(<@toClass source=tableName /> <@toField source=tableName />);

	int delete(<@toClass source=tableName /> <@toField source=tableName />);

	int update(<@toClass source=tableName /> <@toField source=tableName />);

}
