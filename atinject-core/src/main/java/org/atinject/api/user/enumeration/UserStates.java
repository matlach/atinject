package org.atinject.api.user.enumeration;

public enum UserStates implements UserState {

    GUEST(1),
    REGISTERED(2),
    ADMIN(3);

    private final int state;
    
    private UserStates(int state) {
        this.state = state;
    }
    
    @Override
    public int getState() {
        return state;
    }
    
    public static boolean isGuest(int state){
        return GUEST.getState() == state;
    }
    
    public static boolean isRegistered(int state){
        return REGISTERED.getState() == state;
    }
    
    public static boolean isAdmin(int state){
        return ADMIN.getState() == state;
    }
}
