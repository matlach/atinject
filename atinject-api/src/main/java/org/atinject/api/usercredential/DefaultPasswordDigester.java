package org.atinject.api.usercredential;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.security.SHA256Digester;

@ApplicationScoped
public class DefaultPasswordDigester implements PasswordDigester {

    @Inject
    private SHA256Digester sha256Digester;

    @Override
    public String digest(String input) {
        return sha256Digester.digest(input);
    }
}
