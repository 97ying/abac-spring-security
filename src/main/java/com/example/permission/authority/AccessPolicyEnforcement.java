package com.example.permission.authority;

import com.example.permission.authority.model.ExpressionTarget;
import com.example.permission.authority.model.TargetEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccessPolicyEnforcement {

    @Autowired
    private AccessPermissionsEvaluator permissionsEvaluator;

    public void checkOrgIdPermission(String orgId, String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ExpressionTarget expressionTarget = new ExpressionTarget();
        expressionTarget.setTargetEnum(TargetEnum.ORG_ID);
        expressionTarget.setValue(orgId);

        if(!permissionsEvaluator.hasPermission(auth, expressionTarget, permission)) {
            throw new AccessDeniedException("Access is denied");
        }
    }

    public void checkRolePermission(String role, String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ExpressionTarget expressionTarget = new ExpressionTarget();
        expressionTarget.setTargetEnum(TargetEnum.ROLE);
        expressionTarget.setValue(role);

        if(!permissionsEvaluator.hasPermission(auth, expressionTarget, permission)) {
            throw new AccessDeniedException("Access is denied");
        }
    }

    public void checkPermission(Object resource, Expression permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!permissionsEvaluator.hasPermission(auth, resource, permission)) {
            throw new AccessDeniedException("Access is denied");
        }
    }
}
