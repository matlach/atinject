package org.atinject.core.security;

public interface PasswordDigester {

    String getAlgorithm();
    
    String digest(String input);
}
