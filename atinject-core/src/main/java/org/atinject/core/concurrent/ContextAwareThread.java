package org.atinject.core.concurrent;


public class ContextAwareThread extends Thread {

    public ContextAwareThread() {
        super();
    }
    
    public ContextAwareThread(Runnable target) {
        super(target);
    }
    
    public ContextAwareThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }
    
    public ContextAwareThread(String name) {
        super(name);
    }
    
    public ContextAwareThread(ThreadGroup group, String name) {
        super(group, name);
    }
    
    public ContextAwareThread(Runnable target, String name) {
        super(target, name);
    }
    
    public ContextAwareThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }
    
    public ContextAwareThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }
    
    @Override
    public void run() {
        try {
            super.run();
        }
        finally {
            InheritableContext.remove();
        }
    }
}
