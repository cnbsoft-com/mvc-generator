<#include "common.tpl">
package ${packagePath}.<@domainPackage subPath=persistencePath tableName=tableName useDdd=useDddPattern />.${implPath};

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import ${packagePath}.<@domainPackage subPath=persistencePath tableName=tableName useDdd=useDddPattern />.<@toClass source=tableName />${mapperSuffix};
import ${packagePath}.${modelPath}.<@toClass source=tableName />${modelSuffix};

/**
 * <@toClass source=tableName />${mapperSuffix}Impl implementation class with mybatis
 *
 */
@Repository
public class <@toClass source=tableName />${mapperSuffix}Impl extends SqlSessionDaoSupport implements <@toClass source=tableName />${mapperSuffix} {
	
	
	@Override
	public <@toClass source=tableName />${modelSuffix} get(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />){
		return getSqlSession().selectOne("<@toClass source=tableName />.getByPrimaryKey", <@toField source=tableName />);
	}
	
	@Override
	public List${"<"}<@toClass source=tableName />${modelSuffix}${">"} getList(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />){
		return getSqlSession().selectList("<@toClass source=tableName />.getByCondition", <@toField source=tableName />);
	}
	
	@Override
	public int create(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />){
		return getSqlSession().insert("<@toClass source=tableName />.insertSelective", <@toField source=tableName />);
	}
	
	@Override
	public int delete(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />){
		return getSqlSession().delete("<@toClass source=tableName />.deleteByPrimaryKey", <@toField source=tableName />);
	}
	
	@Override
	public int update(<@toClass source=tableName />${modelSuffix} <@toField source=tableName />){
		return getSqlSession().delete("<@toClass source=tableName />.updateSelective", <@toField source=tableName />);
	}
	
}
