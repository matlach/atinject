package org.atinject.core.marshallable;

import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;

public abstract class MarshallableObject implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	// TODO this need to be configured optimally
	private static final ObjectMapper mapper = new ObjectMapper(new SmileFactory());
	
	/**
	 * perform clone using serialization technique using jackson smile format
	 */
	@Override
	public MarshallableObject clone(){
	    try {
	        byte[] bytes = mapper.writeValueAsBytes(this);
	        return mapper.readValue(bytes, this.getClass());
	    }
	    catch (Exception e){
	        throw new RuntimeException(e);
	    }
	}
}
