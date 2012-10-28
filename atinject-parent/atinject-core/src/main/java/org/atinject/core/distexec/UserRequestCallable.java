package org.atinject.core.distexec;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class UserRequestCallable implements Callable<Object> {

	private transient Object target;
	
	private transient Method targetMethod;
	
	private Object[] args;
	
	public UserRequestCallable(Object target, Method targetMethod, Object[] args){
		super();
		this.target = target;
		this.targetMethod = targetMethod;
		this.args = args;
	}

	// callable will be sent on the wire, serialize transient field
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeObject(target.getClass());
		oos.writeUTF(targetMethod.toGenericString());
	}

	// callable is read from the wire, get back target and target method references
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		Class<?> targetClass = (Class<?>) ois.readObject();
		String targetMethodGenericString = ois.readUTF();
		
		// target = ...
		// targetMethod = ...
	}

	@Override
	public Object call() throws Exception {
		return targetMethod.invoke(target, args);
	}

}
