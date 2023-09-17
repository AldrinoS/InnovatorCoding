package com.example.innovatorcodingchallenge.security;

import com.example.innovatorcodingchallenge.model.Roles;
import com.example.innovatorcodingchallenge.model.UserEntity;
import com.example.innovatorcodingchallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsImpl implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new User(userEntity.getUsername(), userEntity.getPassword(), mapRolesToGrantedAuthority(userEntity.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToGrantedAuthority(List<Roles> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
