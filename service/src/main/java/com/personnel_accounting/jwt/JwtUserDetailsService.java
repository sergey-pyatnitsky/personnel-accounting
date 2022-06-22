package com.personnel_accounting.jwt;

import com.personnel_accounting.domain.User;
import com.personnel_accounting.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found with username: " + username);
        return new org.springframework.security.core.userdetails.User(
                username, user.getPassword().replace("{bcrypt}", ""),
                Collections.singletonList(new SimpleGrantedAuthority(userService.getAuthorityByUsername(username).toString())));
    }
}
