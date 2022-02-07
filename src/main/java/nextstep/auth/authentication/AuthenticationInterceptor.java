package nextstep.auth.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {
    private final CustomUserDetailsService userDetailsService;

    protected AuthenticationInterceptor(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}