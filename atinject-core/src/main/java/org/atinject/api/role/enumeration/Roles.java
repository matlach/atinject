package org.atinject.api.role.enumeration;

public enum Roles implements Role {

    // use power of 2 ?
    GUEST(1),
    REGISTERED(2),
    ADMIN(3);

    private final int state;
    
    private Roles(int state) {
        this.state = state;
    }
    
    @Override
    public int getRole() {
        return state;
    }
    
    public static boolean isGuest(int role){
        return GUEST.getRole() == role;
    }
    
    public static boolean isRegistered(int role){
        return REGISTERED.getRole() == role;
    }
    
    public static boolean isAdmin(int role){
        return ADMIN.getRole() == role;
    }

    
}
