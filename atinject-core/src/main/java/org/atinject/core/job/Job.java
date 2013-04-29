package org.atinject.core.job;

public class Job {

    private String name;
    private Trigger trigger; // List of triggers ?
    
    public String getName(){
        return name;
    }
    
    public Job setName(String name){
        this.name = name;
        return this;
    }
    
    // TODO this need to be an interface or abstract class
    public void run(){
        
    }
}
