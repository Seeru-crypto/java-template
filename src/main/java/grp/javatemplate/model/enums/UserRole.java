package grp.javatemplate.model.enums;

public final class UserRole {
    public enum Roles {
        REGULAR,
        ADMIN,
    }

    public static final String ROLE_ADMIN = "ADMIN_1";
    public static final String ROLE_REGULAR = "REGULAR_2";
    private UserRole() {
    }
}