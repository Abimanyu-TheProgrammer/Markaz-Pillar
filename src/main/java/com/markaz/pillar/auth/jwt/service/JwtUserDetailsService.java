package com.markaz.pillar.auth.jwt.service;

import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.auth.repository.models.Menu;
import com.markaz.pillar.auth.repository.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private UserRepository repository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUser user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new User(user.getEmail(), user.getPassword(), user.isActive(),
                true, true, true, generateGrantedAuthorities(user));
    }

    private List<GrantedAuthority> generateGrantedAuthorities(AuthUser user) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Role role = user.getRole();
        grantedAuthorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())));

        for(Menu menu : role.getMenus()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(menu.getName()));
        }

        return grantedAuthorities;
    }
}
