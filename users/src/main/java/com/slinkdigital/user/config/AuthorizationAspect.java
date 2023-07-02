package com.slinkdigital.user.config;

import com.slinkdigital.user.constant.DefaultRoles;
import com.slinkdigital.user.controller.UserController1;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.repository.RefreshTokenRepository;
import com.slinkdigital.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository; 

    @Before("@annotation(com.slinkdigital.user.config.RequiresAuthorizationById)")
    public void authorizeById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String userIdFromApiGateway = getUserIdFromApiGateway(args);
        Long newUserIdFromApiGateway = Long.parseLong(userIdFromApiGateway);
        Long userIdFromDB = getUserIdFromDB(joinPoint, args);
        boolean isAuthorized = newUserIdFromApiGateway == userIdFromDB && userIdFromApiGateway != null;
        if (!isAuthorized) {
            throw new UserException("Insufficient privileges to access the endpoint.");
        }
    }
    
    @Before("@annotation(com.slinkdigital.user.config.RequiresAuthorizationByRole)")
    public void authorizeByRoleAdmin(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String[] userRolesFromApiGateway = getUserRolesFromApiGateway(args);
        boolean isAuthorized = true;
        for (String userRole : userRolesFromApiGateway) {
            log.info(userRole);
            if (userRole == DefaultRoles.ROLE_ADMIN) {
                isAuthorized = true;
            }
        }
        if (isAuthorized == false) {
            throw new UserException("Insufficient privileges to access the endpoint.");
        }
    }

    private String[] getUserRolesFromApiGateway(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest request) {
                String roles = request.getHeader("x-roles");
                return roles.split(",");
            }
        }
        return null;
    }
    
    private String getUserIdFromApiGateway(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest request) {
                return request.getHeader("x-id");
            }
        }
        return null;
    }

    private Long getUserIdFromDB(JoinPoint joinPoint, Object[] args) {
        Class<?> declaringClass = joinPoint.getSignature().getDeclaringType();
        if (declaringClass.equals(UserController1.class)) {
            return getUserIdFromDB(args);
        }
        throw new UnsupportedOperationException("Authorization check not implemented for the given joinPoint.");
    }

    private Long getUserIdFromDB(Object[] args) {
        for (Object arg : args) {
            String uniqueValue;
            Long userId = null;
            if (args.length > 0 && args[0] instanceof RefreshTokenRequest) {
                RefreshTokenRequest refreshTokenRequest = (RefreshTokenRequest) arg;
                uniqueValue = refreshTokenRequest.getRefreshToken();
                userId = refreshTokenRepository.findByToken(uniqueValue).get().getId();
            }
            return userId;
        }
        return null;
    }
}
