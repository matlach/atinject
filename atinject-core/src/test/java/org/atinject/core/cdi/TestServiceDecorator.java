package org.atinject.core.cdi;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@Decorator
public abstract class TestServiceDecorator implements TestServiceInterface {

    @Inject @Delegate @Any TestServiceInterface service;
    
    @Override
    public String getString(){
        return "hello " + service.getString();
    }
}
