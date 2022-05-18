package com.ovelychko.service;

import com.ovelychko.auth.LandonUserPrincipal;
import com.ovelychko.auth.User;
import com.ovelychko.auth.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LandonUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public LandonUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("cannot find username: " + username);
        }
        return new LandonUserPrincipal(user);
    }
}
