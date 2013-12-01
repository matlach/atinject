package org.atinject.core.security;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MD5Digester extends MessageDigester {

    public static final String ALGORITHM = "MD5";
    
    public String digest(String input) {
        return super.digest(input, ALGORITHM);
    }
}
