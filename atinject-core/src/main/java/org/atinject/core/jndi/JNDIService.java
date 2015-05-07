package org.atinject.core.jndi;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

import org.atinject.core.tiers.Service;

@Service
public class JNDIService {

	@PostConstruct
	public void initialize() {
		if (! NamingManager.hasInitialContextFactoryBuilder()) {
			try {
				NamingManager.setInitialContextFactoryBuilder(new SimpleNamingContextBuilder());
			}
			catch (NamingException e) {
				throw new RuntimeException(e);
			}
		}
		// TODO introduce delegates
//		new InitialContext().bind(name, obj);
//		new InitialContext().composeName(name, prefix);
//		new InitialContext().createSubcontext(name);
//		new InitialContext().destroySubcontext(name);
//		new InitialContext().list(name);
//		new InitialContext().listBindings(name);
//		new InitialContext().lookup(name);
//		new InitialContext().lookupLink(name);
//		new InitialContext().rebind(name, obj);
//		new InitialContext().rename(oldName, newName);
//		new InitialContext().unbind(name);
	}
	
}
