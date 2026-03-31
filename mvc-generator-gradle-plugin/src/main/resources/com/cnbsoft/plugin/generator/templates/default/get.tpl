<#include "common.tpl">
${"<#"}include "/common/taglibs.tpl" >
<div class="inner-box-wrapper">
	<div class="inner-box">
		<div class="panel panel-default">
			<div class="panel-body">
				<div class="col-sm-6">
					<h2><@toBookTitle source=tableName /></h2>
					<hr />
				</div>
				<div class="col-sm-6 inner-box-msg">
					<p><@toBookTitle source=tableName /> 정보</p>
					<br />
					<div style="color: red"></div>
				</div>
				<div class="col-sm-6 inner-box-form">
					<#list columns as column>
					<div class="form-group">
						<label><@toBookTitle source=column.columnName /></label>
						<div class="col-sm-*">${'$'}{(<@toField source=column.columnName />)!""}</div>
					</div>
					</#list>
					<div>
						<button class="btn btn-warning" type="button">List</button>
						<button class="btn btn-warning" type="submit">Add</button>
					</div>
				</div>
			</div>
		</div>

	</div>
</div>