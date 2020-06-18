package com.example.permission.authority;

import com.example.permission.authority.model.ExpressionTarget;
import com.example.permission.authority.model.PermissionExpressions;
import com.example.permission.authority.model.SecurityAccessContext;
import com.example.permission.authority.model.TargetEnum;
import com.example.permission.model.Organization;
import com.example.permission.model.Permission;
import com.example.permission.authority.model.PermissionUser;
import org.springframework.expression.Expression;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class AccessPermissionsEvaluator implements PermissionEvaluator {

    private static final Map<TargetEnum, Expression> expressionMap = new HashMap<>();

    @PostConstruct
    public void init() {
        expressionMap.put(TargetEnum.ROLE, PermissionExpressions.ROLE_AND_PERMISSION);
        expressionMap.put(TargetEnum.ORG_ID, PermissionExpressions.ORG_ID_AND_PERMISSION);
        expressionMap.put(TargetEnum.ORG_FULL_PATH, PermissionExpressions.ORG_ID_AND_PERMISSION);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        if (targetDomainObject != null) {
            if (targetDomainObject instanceof ExpressionTarget) {
                return hasPermissionByExpressionEvaluation(authentication, targetDomainObject, permission);
            } else if (targetDomainObject instanceof String) {
                return hasPermissionByPlainTextEvaluation(authentication, (String)targetDomainObject, (String)permission);
            }
        }

        return false;
    }

    private boolean hasPermissionByExpressionEvaluation(Authentication authentication, Object targetDomainObject, Object permission) {
        ExpressionTarget expressionTarget = (ExpressionTarget) targetDomainObject;

        Expression expression = expressionMap.get(expressionTarget.getTargetEnum());

        if (expression != null) {
            SecurityAccessContext cxt = new SecurityAccessContext(authentication.getPrincipal(), expressionTarget.getValue(), permission);
            return Optional.ofNullable(expression.getValue(cxt, Boolean.class)).orElse(false);
        }
        return false;
    }



    private boolean hasPermissionByPlainTextEvaluation(Authentication authentication, String targetDomainObject, String permission) {
        PermissionUser permissionUser = (PermissionUser) authentication.getPrincipal();
        Set<Organization> organizationSet = permissionUser.getOrganizationSet();
        Set<Permission> permissionSet = permissionUser.getPermissionSet();

        long permissionCount = permissionSet.stream().filter(p -> p.getPermission().equalsIgnoreCase(permission))
                .count();

        if (permissionCount > 0) {
            if (StringUtils.isEmpty(targetDomainObject)) {
                return true;
            } else {
                long orgCount = organizationSet.stream().filter(o -> targetDomainObject.equals(o.getId())).count();
                return (orgCount > 0);
            }
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
