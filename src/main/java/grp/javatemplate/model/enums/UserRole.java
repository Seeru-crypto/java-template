package grp.javatemplate.model.enums;

public final class UserRole {
    public enum Roles {
        REGULAR,
        ADMIN,
    }

    public static final String ROLE_ADMIN = "ADMIN_1";
    public static final String ROLE_REGULAR = "REGULAR_2";

    public static final String[] ROLE_ALL_ROLES =  {ROLE_ADMIN, ROLE_REGULAR};

    private UserRole() {
    }
}