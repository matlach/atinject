package org.atinject.core.session;

import java.util.Collection;
import java.util.function.Predicate;

public class SessionSpecifications {

    public static Predicate<Session> matchingMachineId(String machineId) {
    	return s -> machineId.equals(s.getMachineId());
    }
    
    public static Predicate<Session> matchingMachineId(Collection<String> machineIds) {
    	return s -> machineIds.contains(s.getMachineId());
    }
    
}
