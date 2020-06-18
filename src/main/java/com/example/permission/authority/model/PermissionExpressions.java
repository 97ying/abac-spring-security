package com.example.permission.authority.model;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class PermissionExpressions {

    private static ExpressionParser exp = new SpelExpressionParser();

    public static Expression VEHICLE_READ_PERMISSION = exp.parseExpression("subject.permissionSet.?[permission == 'vehicle:read'].size() > 0");
    public static Expression VEHICLE_CREATE_PERMISSION = exp.parseExpression("subject.permissionSet.?[permission == 'vehicle:create'].size() > 0");

    public static Expression ROLE_AND_PERMISSION = exp.parseExpression("subject.roleSet.contains(resource) and subject.permissionSet.![permission].contains(action)");

    public static Expression ORG_ID_AND_PERMISSION = exp.parseExpression("subject.organizationSet.![id].contains(resource) and subject.permissionSet.![permission].contains(action)");
    public static Expression ORG_PATH_PREFIX_AND_PERMISSION = exp.parseExpression("subject.organizationSet.![id].contains(resource) and subject.permissionSet.![permission].contains(action)");

}
