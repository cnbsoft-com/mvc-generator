<#include "common.tpl">
package ${packagePath}.${controllerPath};

import org.springframework.beans.factory.annotation.Autowired;
<#if controllerType == "api">
import org.springframework.web.bind.annotation.*;
import java.util.List;
<#else>
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
</#if>

import ${packagePath}.${servicePath}.<@toAllLow source=tableName />.<@toClass source=tableName />${serviceSuffix};
import ${packagePath}.${modelPath}.<@toClass source=tableName />${modelSuffix};

<#if controllerType == "api">
@RestController
@RequestMapping("/<@toAllLow source=tableName />")
<#else>
@Controller
@RequestMapping("/<@toAllLow source=tableName />/*")
</#if>
public class <@toClass source=tableName />${controllerSuffix} {

	@Autowired
	private <@toClass source=tableName />${serviceSuffix} <@toField source=tableName />${serviceSuffix?uncap_first};

<#if controllerType == "api">
	@GetMapping("/get")
	public <@toClass source=tableName />${modelSuffix} get(@ModelAttribute <@toClass source=tableName />${modelSuffix} <@toField source=tableName />) {
		return <@toField source=tableName />${serviceSuffix?uncap_first}.get(<@toField source=tableName />);
	}

	@GetMapping("/list")
	public List${"<"}<@toClass source=tableName />${modelSuffix}${">"} getList(@ModelAttribute <@toClass source=tableName />${modelSuffix} <@toField source=tableName />) {
		return <@toField source=tableName />${serviceSuffix?uncap_first}.getList(<@toField source=tableName />);
	}

	@PostMapping("/create")
	public int create(@RequestBody <@toClass source=tableName />${modelSuffix} <@toField source=tableName />) {
		return <@toField source=tableName />${serviceSuffix?uncap_first}.create(<@toField source=tableName />);
	}

	@PutMapping("/update")
	public int update(@RequestBody <@toClass source=tableName />${modelSuffix} <@toField source=tableName />) {
		return <@toField source=tableName />${serviceSuffix?uncap_first}.update(<@toField source=tableName />);
	}

	@DeleteMapping("/delete")
	public int delete(@ModelAttribute <@toClass source=tableName />${modelSuffix} <@toField source=tableName />) {
		return <@toField source=tableName />${serviceSuffix?uncap_first}.delete(<@toField source=tableName />);
	}
<#else>
	@PostMapping
	public void form(@ModelAttribute("${tableName?lower_case}_form") <@toClass source=tableName />${modelSuffix} <@toField source=tableName />) {
	}

	@GetMapping
	public void get(@ModelAttribute("${tableName?lower_case}_form") <@toClass source=tableName />${modelSuffix} <@toField source=tableName />, Model model) {
		model.addAttribute("<@toField source=tableName />", <@toField source=tableName />${serviceSuffix?uncap_first}.get(<@toField source=tableName />));
	}

	@GetMapping
	public void list(@ModelAttribute <@toClass source=tableName />${modelSuffix} <@toField source=tableName />, Model model) {
		model.addAttribute("<@toField source=tableName />List", <@toField source=tableName />${serviceSuffix?uncap_first}.getList(<@toField source=tableName />));
	}

	@PostMapping
	public void create(@ModelAttribute <@toClass source=tableName />${modelSuffix} <@toField source=tableName />) {
		<@toField source=tableName />${serviceSuffix?uncap_first}.create(<@toField source=tableName />);
	}

	@PostMapping
	public void delete(@ModelAttribute <@toClass source=tableName />${modelSuffix} <@toField source=tableName />) {
		<@toField source=tableName />${serviceSuffix?uncap_first}.delete(<@toField source=tableName />);
	}

	@PutMapping("/{id}")
	public void update(@PathVariable("id") String id, @ModelAttribute <@toClass source=tableName />${modelSuffix} <@toField source=tableName />) {
		<@toField source=tableName />${serviceSuffix?uncap_first}.update(<@toField source=tableName />);
	}
</#if>

}
