package grp.javatemplate.config;

import java.util.Optional;

public class SecurityUtils {

    public static Optional<String> getCurrentUserLogin() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
        return Optional.empty();
    }
}
