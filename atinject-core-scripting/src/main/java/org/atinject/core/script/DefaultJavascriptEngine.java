package org.atinject.core.script;

import java.io.Reader;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@ApplicationScoped
public class DefaultJavascriptEngine {

	private ScriptEngine javascriptEngine;
	
	@PostConstruct
	public void initialize() {
		javascriptEngine = new ScriptEngineManager()
			.getEngineByName("nashorn");
	}

	public Object eval(String script, ScriptContext context) throws ScriptException {
		return javascriptEngine.eval(script, context);
	}

	public Object eval(Reader reader, ScriptContext context)
			throws ScriptException {
		return javascriptEngine.eval(reader, context);
	}

	public Object eval(String script) throws ScriptException {
		return javascriptEngine.eval(script);
	}

	public Object eval(Reader reader) throws ScriptException {
		return javascriptEngine.eval(reader);
	}

	public Object eval(String script, Bindings n) throws ScriptException {
		return javascriptEngine.eval(script, n);
	}

	public Object eval(Reader reader, Bindings n) throws ScriptException {
		return javascriptEngine.eval(reader, n);
	}

	public void put(String key, Object value) {
		javascriptEngine.put(key, value);
	}

	public Object get(String key) {
		return javascriptEngine.get(key);
	}

	public Bindings getBindings(int scope) {
		return javascriptEngine.getBindings(scope);
	}

	public void setBindings(Bindings bindings, int scope) {
		javascriptEngine.setBindings(bindings, scope);
	}

	public Bindings createBindings() {
		return javascriptEngine.createBindings();
	}

	public ScriptContext getContext() {
		return javascriptEngine.getContext();
	}

	public void setContext(ScriptContext context) {
		javascriptEngine.setContext(context);
	}

	public ScriptEngineFactory getFactory() {
		return javascriptEngine.getFactory();
	}
	
}
