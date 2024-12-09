package com.example.tenpaws.global.security.dto;

import com.example.tenpaws.domain.admin.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SuperAdminDetails extends AdminUserDetails {

    public SuperAdminDetails(Admin admin) {
        super(admin);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // SUPER_ADMIN 권한을 추가로 부여
        List<GrantedAuthority> authorities = new ArrayList<>(super.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        return authorities;
    }
}

