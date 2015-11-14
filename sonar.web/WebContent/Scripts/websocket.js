function WebClient() {

this.endPointURL = "ws://" + window.location.host + window.location.pathname + "socket";
 
this.chatClient = null;
this.requests = new Array();
this.connected = false;
 
	this.connect = function () {
		this.chatClient = new WebSocket(this.endPointURL);
		this.chatClient.onmessage = function (event) {
	        var jsonObj = JSON.parse(event.data);
	        if (jsonObj.id != undefined && socketClient.requests[jsonObj.id] != undefined) {
	        	var obj = socketClient.requests[jsonObj.id];
	        	if (obj.param == undefined) {
	        		obj.param = null;
	        	}
	        	obj.eventHandler(jsonObj.value, obj.param);
	        	delete socketClient.requests[jsonObj.id];
	        }
	    };
	    
	    this.chatClient.onopen = function()
	    {
	    	socketClient.connected = true;
	    	if (socketClient.onconnected != undefined)
	    		socketClient.onconnected();
	    };
	    
	    this.chatClient.onclose = function(event)
	    {
	    	socketClient.connected =false;
	    	if (event.wasClean)
	    	{
	    		//alert('Соединение закрыто чисто');
	    	} else {
	    		//alert('Обрыв соединения'); // например, "убит" процесс сервера
	    	}
	    	//alert('Код: ' + event.code + ' причина: ' + event.reason);
	    };
	};
 
	this.disconnect = function () {
		this.chatClient.close();
	};

	this.sendMessage2 = function(id, cmd, param) {
	    var jsonObj = { "id" : id, "name" : cmd, "param" : param};
	    this.chatClient.send(JSON.stringify(jsonObj));
	};
	/*cmd - комманда
	 * param - параметр команды
	 * obj - объект с обработчиком obj.event и параметром obj.param
	 */
	this.sendCommand = function(cmd, param, obj) {
		var newId = MiscService.inc();
		this.requests[newId] = obj;
		this.sendMessage2(newId, cmd, param);
	};

};

var socketClient = new WebClient();

function MiscFunc() {
	this.counter = 100;
	this.inc = function()
	{
		return ++this.counter;
	};
};

var MiscService = new MiscFunc();

