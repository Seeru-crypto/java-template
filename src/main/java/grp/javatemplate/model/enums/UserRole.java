package grp.javatemplate.model.enums;

public final class UserRole {
    public enum Roles {
        REGULAR,
        ADMIN,
    }

    public static final String ROLE_ADMIN = Roles.ADMIN.toString();
    public static final String ROLE_REGULAR = Roles.REGULAR.toString();

    private UserRole() {
    }
}