package com.example.permission.authority;

import com.example.permission.model.Organization;
import com.example.permission.model.Permission;
import com.example.permission.authority.model.PermissionUser;
import com.example.permission.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    public PermissionUser getUser(String userId) {
        if (!userDao.userExist(userId)) {
            throw new UsernameNotFoundException("User '" + userId + "' not found.");
        }

        Set<Organization> orgSet = userDao.getOrganizations(userId);
        Set<Permission> permissionSet = userDao.getPermissions(userId);

        Set<String> roleSet = permissionSet.stream().map(Permission::getRole).collect(Collectors.toSet());
        Set<SimpleGrantedAuthority> grantedAuthoritySet = roleSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        PermissionUser permissionUser = new PermissionUser(userId, "{noop}", grantedAuthoritySet);
        permissionUser.setRoleSet(roleSet);
        permissionUser.setOrganizationSet(orgSet);
        permissionUser.setPermissionSet(permissionSet);

        return permissionUser;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return this.getUser(userId);
    }
}
