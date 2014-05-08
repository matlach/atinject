package org.atinject.core.security;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SHA224Digester extends MessageDigester {

    public static final String ALGORITHM = "SHA-224";
    
    public String digest(String input) {
        return super.digest(input, ALGORITHM);
    }
    
}
