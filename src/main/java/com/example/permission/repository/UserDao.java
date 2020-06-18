package com.example.permission.repository;

import com.example.permission.model.Organization;
import com.example.permission.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userExist(String userId) {
        return jdbcTemplate.queryForList("SELECT name FROM cvc_user WHERE id = ?", new Object[] {Integer.parseInt(userId)}, String.class)
                .stream().findFirst().isPresent();
    }

    public Set<Organization> getOrganizations(String userId) {

        List<Map<String, Object>> results = jdbcTemplate.queryForList(" SELECT cvc_user.id, cvc_org.path_id as org_id, cvc_org.name as org_name, cvc_org.full_path_id FROM cvc_user " +
                " INNER JOIN cvc_user_op_org ON cvc_user.id = cvc_user_op_org.user_id" +
                " INNER JOIN cvc_org ON cvc_user_op_org.op_org_id = cvc_org.path_id" +
                " WHERE cvc_user.id = ?", new Object[] {Integer.parseInt(userId)});

        Set<Organization> organizationList = results.stream().map(m -> {
            Organization organization = new Organization();
            organization.setId(String.valueOf(m.get("org_id")));
            organization.setName(String.valueOf(m.get("org_name")));
            organization.setFullPath(String.valueOf(m.get("full_path_id")));

            return organization;
        }).collect(Collectors.toSet());

        return organizationList;
    }

    public Set<Permission> getPermissions(String userId) {

        List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT cvc_user.id, cvc_user.name, cvc_role.role, cvc_permission.permission, cvc_permission.type FROM cvc_user " +
                "        INNER JOIN cvc_user_role ON cvc_user.id = cvc_user_role.user_id" +
                " INNER JOIN cvc_role ON cvc_user_role.role_id = cvc_role.id" +
                " INNER JOIN cvc_role_permission ON cvc_role.id = cvc_role_permission.role_id" +
                " INNER JOIN cvc_permission ON cvc_role_permission.permission_id = cvc_permission.id" +
                " WHERE cvc_user.id = ?;", new Object[] {Integer.parseInt(userId)});

        Set<Permission> permissionList = results.stream().map(m -> {
            Permission permission = new Permission();
            permission.setRole(String.valueOf(m.get("role")));
            permission.setPermission(String.valueOf(m.get("permission")));
            permission.setType(String.valueOf(m.get("type")));

            return permission;
        }).collect(Collectors.toSet());

        return permissionList;
    }

}
