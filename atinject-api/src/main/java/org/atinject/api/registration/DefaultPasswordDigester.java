package org.atinject.api.registration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.security.SHA1Digester;

@ApplicationScoped
public class DefaultPasswordDigester implements PasswordDigester {

    @Inject
    private SHA1Digester sha1Digester;

    @Override
    public String digest(String input) {
        return sha1Digester.digest(input);
    }
}
