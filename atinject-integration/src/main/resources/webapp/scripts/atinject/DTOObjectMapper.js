define(["atinject/CLASS_ALIAS"],
function(CLASS_ALIAS){
	
	function DTOObjectMapper(CLASS_ALIAS) {
		this.classAlias = CLASS_ALIAS;
	}

	DTOObjectMapper.prototype.stringConstructor = "".constructor;
	DTOObjectMapper.prototype.arrayConstructor = [].constructor;
	DTOObjectMapper.prototype.objectConstructor = {}.constructor;
	
	DTOObjectMapper.prototype.cast = function(rawObj, constructor) {
	    var obj = new constructor();
	    for(var i in rawObj){
	    	if (rawObj[i] === null){
	    		alert(i + " is null");
	    		obj[i] = rawObj[i];
	    	}
	    	else if (rawObj[i] === undefined){
	    		alert(i + " is undefined");
	    		obj[i] = rawObj[i];
	    	}
	    	else if (rawObj[i].constructor === stringConstructor){
	    		alert(i + " is string");
	    		obj[i] = rawObj[i];
	    	}
	    	else if (rawObj[i].constructor === arrayConstructor){
	    		alert(i + " is array");
	    		
	    		for (var j in rawObj[i]){
	    			if (rawObj[i][j] === null){
	    				obj[i][j] = rawObj[i][j];
	    			}
	    			else if (rawObj[i][j] === undefined){
	    				obj[i][j] = rawObj[i][j];
	    			}
	    			else if (rawObj[i][j] === stringConstructor){
	    				obj[i][j] = rawObj[i][j];
	    			}
	    			else if (rawObj[i][j] === arrayConstructor){
	    				// TODO extract cast array method to be recursive
	    			}
	    			else if (rawObj[i][j] === objectConstructor){
	    				obj[i][j] = cast(rawObj[i], classAlias[rawObj[i]._class]);
	    			}
	    		}
	    	}
	    	else if (rawObj[i].constructor === objectConstructor){
	    		alert(i + " is object");
	    		obj[i] = cast(rawObj[i], classAlias[rawObj[i]._class]);
	    	}
	    }
	    return obj;
	};
	
	return new DTOObjectMapper(CLASS_ALIAS);
});