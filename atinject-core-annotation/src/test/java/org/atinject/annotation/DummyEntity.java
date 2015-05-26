package org.atinject.annotation;

public class DummyEntity {

	private String name;
	
	private DummyEmbeddedEntity embedded;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmbedded(DummyEmbeddedEntity embedded) {
		this.embedded = embedded;
	}
}
