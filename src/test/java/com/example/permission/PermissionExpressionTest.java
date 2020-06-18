package com.example.permission;


import com.example.permission.authority.model.PermissionExpressions;
import com.example.permission.authority.model.SecurityAccessContext;
import com.example.permission.model.Device;
import com.example.permission.model.Permission;
import com.example.permission.authority.model.PermissionUser;
import org.junit.jupiter.api.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PermissionExpressionTest {

    @Test
    public void testExpr() {
        ExpressionParser exp = new SpelExpressionParser();

        PermissionUser user = new PermissionUser("user-1", "123456", new ArrayList<GrantedAuthority>(0));
        user.setRoleSet(getRoles());
        user.setPermissionSet(getPermissions());

        SecurityAccessContext cxt = new SecurityAccessContext(user, "org-A", "vehicle:read");

        System.out.println(exp.parseExpression("subject.roleSet.contains('admin') ").getValue(cxt, Boolean.class));
        System.out.println(exp.parseExpression("subject.permissionSet.contains('adm') ").getValue(cxt, Boolean.class));
        System.out.println(exp.parseExpression("subject.permissionSet.?[permission eq 'vehicle:read'].size() > 1").getValue(cxt, Boolean.class));
    }

    class Resource {
        public String org;
    }

    @Test
    public void testExpr2() {
        ExpressionParser exp = new SpelExpressionParser();
        Resource resource = new Resource();
        resource.org = "org-A";

        PermissionUser user = new PermissionUser("user-1", "123456", new ArrayList<GrantedAuthority>(0));
        user.setRoleSet(getRoles());
        user.setPermissionSet(getPermissions());

        SecurityAccessContext cxt = new SecurityAccessContext(user, "vehicle:rad", "vehicle:read");

        System.out.println(exp.parseExpression("new com.example.permission.model.Device('aa', 'bb')").getValue(Device.class));
        System.out.println(exp.parseExpression("subject.roleSet.contains('admin') ").getValue(cxt, Boolean.class));
        System.out.println(exp.parseExpression("subject.permissionSet.contains('adm') ").getValue(cxt, Boolean.class));
        System.out.println(exp.parseExpression("subject.permissionSet.![permission].size() > 0").getValue(cxt, Boolean.class));
        System.out.println(exp.parseExpression("subject.permissionSet.![permission].contains(resource)").getValue(cxt, Boolean.class));
//        System.out.println(exp.parseExpression("subject.permissionSet.?[permission eq 'vehicle:read'].size() > 0 && resource.org == 'org-A'").getValue(cxt, Boolean.class));
    }

    @Test
    public void testPermissionExpression() {
        PermissionUser user = new PermissionUser("user-1", "123456", new ArrayList<GrantedAuthority>(0));
        user.setPermissionSet(getPermissions());

        SecurityAccessContext cxt = new SecurityAccessContext(user, null, "vehicle:ead");

        PermissionExpressions.VEHICLE_READ_PERMISSION.getValue(cxt, Boolean.class);
    }

    private Set<Permission> getPermissions() {
        Permission permission = new Permission();
        permission.setPermission("vehicle:read");

        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);

        return permissionSet;
    }

    private Set<String> getRoles() {

        Set<String> roleSet = new HashSet<>();
        roleSet.add("admin");

        return roleSet;
    }
}
