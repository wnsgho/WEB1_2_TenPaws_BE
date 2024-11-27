package com.example.tenpaws.global.security.dto;

import com.example.tenpaws.domain.shelter.entity.Shelter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class ShelterUserDetails implements UserDetails {

    private final Shelter shelter;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return shelter.getUserRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return shelter.getPw();
    }

    @Override
    public String getUsername() {
        return shelter.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
