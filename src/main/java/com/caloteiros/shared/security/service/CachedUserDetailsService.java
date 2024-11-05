package com.caloteiros.shared.security.service;

import com.caloteiros.user.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CachedUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    private final Map<String, UserDetails> userCache = new HashMap<>();

    public CachedUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userCache.containsKey(email)) {
            return userCache.get(email);
        }

        UserDetails user = (UserDetails) userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userCache.put(email, user);
        return user;
    }
}

