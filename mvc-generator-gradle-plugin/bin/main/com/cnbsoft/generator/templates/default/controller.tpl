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

import ${packagePath}.${servicePath}.<@toAllLow source=tableName />.<@toClass source=tableName />Service;
import ${packagePath}.${modelPath}.<@toClass source=tableName />;

<#if controllerType == "api">
@RestController
@RequestMapping("/<@toAllLow source=tableName />")
<#else>
@Controller
@RequestMapping("/<@toAllLow source=tableName />/*")
</#if>
public class <@toClass source=tableName />Controller {

	@Autowired
	private <@toClass source=tableName />Service <@toField source=tableName />Service;

<#if controllerType == "api">
	@GetMapping("/get")
	public <@toClass source=tableName /> get(@ModelAttribute <@toClass source=tableName /> <@toField source=tableName />) {
		return <@toField source=tableName />Service.get(<@toField source=tableName />);
	}

	@GetMapping("/list")
	public List${"<"}<@toClass source=tableName />${">"} getList(@ModelAttribute <@toClass source=tableName /> <@toField source=tableName />) {
		return <@toField source=tableName />Service.getList(<@toField source=tableName />);
	}

	@PostMapping("/create")
	public int create(@RequestBody <@toClass source=tableName /> <@toField source=tableName />) {
		return <@toField source=tableName />Service.create(<@toField source=tableName />);
	}

	@PutMapping("/update")
	public int update(@RequestBody <@toClass source=tableName /> <@toField source=tableName />) {
		return <@toField source=tableName />Service.update(<@toField source=tableName />);
	}

	@DeleteMapping("/delete")
	public int delete(@ModelAttribute <@toClass source=tableName /> <@toField source=tableName />) {
		return <@toField source=tableName />Service.delete(<@toField source=tableName />);
	}
<#else>
	@PostMapping
	public void form(@ModelAttribute("${tableName?lower_case}_form") <@toClass source=tableName /> <@toField source=tableName />) {
	}

	@GetMapping
	public void get(@ModelAttribute("${tableName?lower_case}_form") <@toClass source=tableName /> <@toField source=tableName />, Model model) {
		model.addAttribute("<@toField source=tableName />", <@toField source=tableName />Service.get(<@toField source=tableName />));
	}

	@GetMapping
	public void list(@ModelAttribute <@toClass source=tableName /> <@toField source=tableName />, Model model) {
		model.addAttribute("<@toField source=tableName />List", <@toField source=tableName />Service.getList(<@toField source=tableName />));
	}

	@PostMapping
	public void create(@ModelAttribute <@toClass source=tableName /> <@toField source=tableName />) {
		<@toField source=tableName />Service.create(<@toField source=tableName />);
	}

	@PostMapping
	public void delete(@ModelAttribute <@toClass source=tableName /> <@toField source=tableName />) {
		<@toField source=tableName />Service.delete(<@toField source=tableName />);
	}

	@PutMapping("/{id}")
	public void update(@PathVariable("id") String id, @ModelAttribute <@toClass source=tableName /> <@toField source=tableName />) {
		<@toField source=tableName />Service.update(<@toField source=tableName />);
	}
</#if>

}
