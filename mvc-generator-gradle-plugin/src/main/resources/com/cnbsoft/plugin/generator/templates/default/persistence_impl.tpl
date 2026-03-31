<#include "common.tpl">
package ${packagePath}.${persistencePath}.<@toAllLow source=tableName />.${implPath};

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import ${packagePath}.${persistencePath}.<@toAllLow source=tableName />.<@toClass source=tableName />Dao;
import ${packagePath}.${modelPath}.<@toClass source=tableName />;

/**
 * <@toClass source=tableName />${packagePath?cap_first}Impl implementation class with mybatis
 *
 */
@Repository
public class <@toClass source=tableName />${packagePath?cap_first}Impl extends SqlSessionDaoSupport implements <@toClass source=tableName />Dao {
	
	
	@Override
	public <@toClass source=tableName /> get(<@toClass source=tableName /> <@toField source=tableName />){
		return getSqlSession().selectOne("<@toClass source=tableName />.getByPrimaryKey", <@toField source=tableName />);
	}
	
	@Override
	public List${"<"}<@toClass source=tableName />${">"} getList(<@toClass source=tableName /> <@toField source=tableName />){
		return getSqlSession().selectList("<@toClass source=tableName />.getByCondition", <@toField source=tableName />);
	}
	
	@Override
	public int create(<@toClass source=tableName /> <@toField source=tableName />){
		return getSqlSession().insert("<@toClass source=tableName />.insertSelective", <@toField source=tableName />);
	}
	
	@Override
	public int delete(<@toClass source=tableName /> <@toField source=tableName />){
		return getSqlSession().delete("<@toClass source=tableName />.deleteByPrimaryKey", <@toField source=tableName />);
	}
	
	@Override
	public int update(<@toClass source=tableName /> <@toField source=tableName />){
		return getSqlSession().delete("<@toClass source=tableName />.updateSelective", <@toField source=tableName />);
	}
	
}
