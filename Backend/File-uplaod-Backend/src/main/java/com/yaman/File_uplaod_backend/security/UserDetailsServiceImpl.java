package com.yaman.File_uplaod_backend.security;

import com.yaman.File_uplaod_backend.model.User;
import com.yaman.File_uplaod_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with email: " + email));

        return UserDetailsImpl.build(user);
    }
}

