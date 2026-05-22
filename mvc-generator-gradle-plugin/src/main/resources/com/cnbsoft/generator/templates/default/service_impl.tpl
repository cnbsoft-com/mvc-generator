<#include "common.tpl">
package ${packagePath}.${servicePath}.<@toAllLow source=tableName />.${implPath};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import <@mapperPackage tableName />.<@toClass source=tableName />${mapperSuffix};
import ${packagePath}.${modelPath}.<@toClass source=tableName />${modelSuffix};
import <@servicePackage tableName />.<@toClass source=tableName />${serviceSuffix};
<@printImports columns />

@Service
public class <@toClass source=tableName />${serviceImplSuffix} implements <@toClass source=tableName />${serviceSuffix} {

	@Autowired
	private <@toClass source=tableName />${mapperSuffix} <@toField source=tableName />${mapperSuffix?uncap_first};

	@Override
	public <@toClass source=tableName />${modelSuffix} get(){
		return <@toField source=tableName />${mapperSuffix?uncap_first}.get();
	}

	@Override
	public List${"<"}<@toClass source=tableName />${modelSuffix}${">"} getList(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />){
		return <@toField source=tableName />${mapperSuffix?uncap_first}.getList(<@toField source=tableName />);
	}

	@Override
	public int create(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />){
		return <@toField source=tableName />${mapperSuffix?uncap_first}.create(<@toField source=tableName />);
	}

	@Override
	public int delete(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />){
		return <@toField source=tableName />${mapperSuffix?uncap_first}.delete(<@toField source=tableName />);
	}

	@Override
	public int update(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />){
		return <@toField source=tableName />${mapperSuffix?uncap_first}.update(<@toField source=tableName />);
	}

}
