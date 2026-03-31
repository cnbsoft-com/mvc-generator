<#include "common.tpl">
${"<#"}include "/common/taglibs.tpl" >
	<div>
		<h1 class="page-header">${tableName?capitalize} list</h1>	
		<table class="table table-striped table-hover">
            <thead>
                <tr>
                <th>No</th>
                <#list columns as column>
                <th><@toBookTitle source=column.columnName /></th>
                </#list>
                <th>Setting</th>
                </tr>
            </thead>
            <tbody>
            ${"<#"}list <@toField source=tableName />List as <@toField source=tableName />>
            <tr>
              <td>&nbsp;</td>
               <#list columns as column>
              <td>${'${'}<@toField source=tableName />.<@toField source=column.columnName />}</td>
              </#list>
              <td>
                 <a href="" class="btn btn-warning btn-xs btn_${tableName}_edit" role="button">EDIT</a>
                 <a href="" class="btn btn-warning btn-xs btn_${tableName}_del" role="button">DEL</a>
                </td>
            </tr>
            ${"</#list>"}
            </tbody>
		</table>
	</div>

	<div class="col-sm-5" id="pagination_${tableName}">
		
	</div>
    
   
	<div class="col-sm-7">
		<form method="get" action="${tableName}" id="search_${tableName}_form">
			<div class="form-inline" style="text-align: right">
				<select name="skey" id="skey" class="form-control">
                	<option value=''>All</option>
                    <#list columns as column>
                    <option value="<@toField source=column.columnName />"><@toBookTitle source=column.columnName /></option>
                    </#list>
				</select>
				<input type="text" id="stxt" name="stxt" class="form-control" placeholder="Search" value="">
				<button type="submit" class="btn btn-primary">Submit</button>
				<button type="button" id="new" name="new" class="btn btn-success">New</button>
				<button type="button" id="new" name="new" class="btn btn-warning">Edit</button>
				<button type="button" id="del" name="del" class="btn btn-danger">Del</button>
			</div>
		</form>
        
	</div>