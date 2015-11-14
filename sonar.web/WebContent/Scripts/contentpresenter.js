
function ContentPresenter() {

this.__proto__.clientCommands = new Array();

this.__proto__.loadClientCommands = function(cmds)
{
	ContentPresenter.prototype.clientCommands = cmds;
}

this.__proto__.loadView = function(component)
{
	var root = document.createDocumentFragment();
	var child = document.createElement("div");
	child.innerHTML = component.content;
	var child2 = child.cloneNode(true);
	delete child;
	var ch = child2.childNodes[0];
	root.appendChild(ch);
	delete child2;
	var cmd = root.querySelector("[component]");
	if (cmd != undefined)
	{
		cmd.attributes.getNamedItem("component").nodeValue = component.name;
	}
	component.ui = ch;
}

this.__proto__.makeModel = function(component)
{
	var node = component.ui.cloneNode(true);
	this._ui = node;//save ui control
	this._ui.model = this;//save model
	this._component = component;//save component
	
	for(var p = 0; p < component.Properties.length; p++)
	{
		var prop = component.Properties[p];
		var target = this._ui.querySelector("[name='" + prop.target + "']");
		if (target == undefined)
			continue;
		var method = prop.method;
		if (method == undefined)
			continue;
		
		var tgt = 'var target = this._ui.querySelector("[name=\'' + prop.target + '\']");';
		var clearCh = 'while (target.hasChildNodes()) { target.removeChild(target.lastChild); };';
		var plainText = "target.innerHTML";
		var srcSet  = "this.__defineSetter__(prop.name, function(e)";
		var srcGet  = "this.__defineGetter__(prop.name, function()";
		
		var scr = srcSet + "{" + tgt;
		if (method == "Text")
		{
			scr += clearCh + plainText + " = e;" + "});";
		}
		else if (method == "Property")
		{
			scr += "target." + prop.attr + " = e;" + "});";
		}
		eval(scr);
		
		var scr = srcGet + "{" + tgt;
		if (method == "Text")
		{
			scr += "return "+ plainText + ";" + "});";
		}
		else if (method == "Property")
		{
			scr += "return target." + prop.attr + " ;" + "});";
		}
		eval(scr);
	}
}

this.__proto__.addEvents = function(component, model)
{
	if (component == undefined || model == undefined)
		return;
	for(var p = 0; p < component.Events.length; p++)
	{
		var event = component.Events[p];
		var tgt = model._ui.querySelector("[name='" + event.elementName + "']");
		if (tgt == undefined)
			continue;
		tgt.addEventListener(event.event, ContentPresenter.prototype.processEvent, false);
	}
}

this.__proto__.getModel = function(elem)
{
	while (elem != undefined)
	{
		var c = elem.attributes.getNamedItem("component");
		if (c != undefined)
		{
			return elem.model;
		}
		elem = elem.parentNode;
	}
	return null;
}

this.__proto__.findEvent = function(component, elementName, event)
{
	for(var p = 0; p < component.Events.length; p++)
	{
		var ev = component.Events[p];
		if (ev.elementName == elementName && ev.event == event)
			return ev;
	}
	return null;
}

this.__proto__.processEvent = function(e)
{
	var tgt = e.target;
	var name = tgt.attributes.getNamedItem("name").nodeValue;
	var model = ContentPresenter.prototype.getModel(tgt);
	var type = e.type;
	var event = ContentPresenter.prototype.findEvent(model._component, name, type);
	ContentPresenter.prototype.executeCommand(model, event.cmdName);
}

this.__proto__.executeCommand = function(model, cmdName)
{
	var ehandler = ContentPresenter.prototype.clientCommands[cmdName];
	var result;
	if (ehandler != undefined)
		result = ehandler(model);
	if (result == undefined)
		return;
	ContentPresenter.prototype.addResult(cmdName, result);
}

this.__proto__.addResult = function(cmdName, result)
{
	if (result == null)
		return;
	for(var compName in components)
	{
		var comp = components[compName];
		var cmdBind = comp.CommandBinding[cmdName];
		if (cmdBind == null)
			continue;
		//нашли, куда положить результат
		//создаем объект
		var model = ContentPresenter.prototype.createModel(cmdBind.modelClass);
		//заполняем данными
		for (var prop in result)
		{
			var val = result[prop];
			if (model[prop] != undefined)
				model[prop] = val;
		}
		//помещаем в нужное место
		var holder = comp.PlaceHolders[cmdBind.placeHolderName];
		if (holder == null)
			continue;
		var query = 'div[component="' + comp.name + '"] ' + holder.query;
		var place = document.querySelector(query);
		if (place == null)
			continue;
		if (holder.type == 'Single')
		{
			while(place.firstChild != null)
			{
				place.removeChild(place.firstChild);
			}
		}
		place.appendChild(model._ui);
	}
}

this.__proto__.loadViewAll = function(components)
{
	for(var compName in components)
	{
		var comp = components[compName];
		ContentPresenter.prototype.loadView(comp);
	}
}

this.__proto__.createModel = function(modelName)
{
	var model = components[modelName];
	if (model == null)
		return;
	var res = new ContentPresenter.prototype.makeModel(model);
	ContentPresenter.prototype.addEvents(model, res);
	return res;
}

this.__proto__.preProcessComponents = function(components)
{
	for(var x = 0; x < components.length; x++)
	{
		var comp = components[x];
		ContentPresenter.prototype.preProcessComponent(comp);
		components[comp.name] = comp;
	}
}

this.__proto__.preProcessComponent = function (component)
{
	if (component == null)
		return;
	ContentPresenter.prototype.loadView(component);
	for(var z = 0;z < component.PlaceHolders.length; z++)
	{
		var place = component.PlaceHolders[z];
		component.PlaceHolders[place.name] = place;
	}
	for (var x = 0; x < component.CommandBinding.length; x++)
	{
		var cmdBind = component.CommandBinding[x];
		component.CommandBinding[cmdBind.cmdClass] = cmdBind;
	}
	
}

}