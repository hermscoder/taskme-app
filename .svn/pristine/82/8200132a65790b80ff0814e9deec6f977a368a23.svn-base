package com.herms.taskme.service;

import com.herms.taskme.model.User;
import com.herms.taskme.repository.UserRepository;
import com.herms.taskme.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email, used on JWTAuthenticationFilter
        User user = userRepository.findByUsername(usernameOrEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail);
        }

        return UserPrincipal.create(user);
    }

    // This method can be used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }

    @Transactional
    public User getLoggedUser(){
        UserPrincipal username = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User principal = null;
        if(username != null || !"".equals(username.getUsername())){
            principal = userRepository.findById(username.getId()).orElse(null);
        }
        return principal;
    }
}
