<#include "common.tpl">
package ${packagePath}.${servicePath}.<@toAllLow source=tableName />.${implPath};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import ${packagePath}.${persistencePath}.<@toAllLow source=tableName />.<@toClass source=tableName />${persistencePath?cap_first};
import ${packagePath}.${modelPath}.<@toClass source=tableName />;
import ${packagePath}.${servicePath}.<@toAllLow source=tableName />.<@toClass source=tableName />Service;

@Service
public class <@toClass source=tableName />ServiceImpl implements <@toClass source=tableName />Service {
	
	@Autowired
	private <@toClass source=tableName />${persistencePath?cap_first} <@toField source=tableName />${persistencePath?cap_first};
	
	@Override
	public <@toClass source=tableName /> get(<@toClass source=tableName /> <@toField source=tableName />){
		return <@toField source=tableName />${persistencePath?cap_first}.get(<@toField source=tableName />);
	}
	
	@Override
	public List${"<"}<@toClass source=tableName />${">"} getList(<@toClass source=tableName /> <@toField source=tableName />){
		return <@toField source=tableName />${persistencePath?cap_first}.getList(<@toField source=tableName />);
	}
	
	@Override
	public int create(<@toClass source=tableName /> <@toField source=tableName />){
		return <@toField source=tableName />${persistencePath?cap_first}.create(<@toField source=tableName />);
	}
	
	@Override
	public int delete(<@toClass source=tableName /> <@toField source=tableName />){
		return <@toField source=tableName />${persistencePath?cap_first}.delete(<@toField source=tableName />);
	}
	
	@Override
	public int update(<@toClass source=tableName /> <@toField source=tableName />){
		return <@toField source=tableName />${persistencePath?cap_first}.update(<@toField source=tableName />);
	}
	
}
