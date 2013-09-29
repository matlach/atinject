package org.atinject.core.topology;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultMachineIdUrlMapper implements MachineIdUrlMapper{

    Map<String, String> urls;
    
    @PostConstruct
    public void initialize(){
        urls = new HashMap<>();
        urls.put("default-machineId", "http://localhost:8080/");
    }
    
    @Override
    public String getUrl(String machineId) {
        return null;
    }

}
