package org.atinject.core.validation;

import javax.inject.Inject;

import org.atinject.integration.WeldRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldRunner.class)
public class ValidationTest
{

    @Inject Validator validator;
    
    @Inject ValidatedService validatedService;
    
    @Test
    public void testValidation(){
        validator.validate(new String());
    }
    
    @Test(expected=RuntimeException.class)
    public void testValidatedService(){
        try {
            validatedService.validateNotNull(null);
        }
        catch (RuntimeException e){
            throw e;
        }
        
    }
}
