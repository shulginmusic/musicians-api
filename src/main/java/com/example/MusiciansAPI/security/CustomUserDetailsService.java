package com.example.MusiciansAPI.security;

import com.example.MusiciansAPI.model.User;
import com.example.MusiciansAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 *  custom UserDetailsService which loads a user’s data given their username -
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginCredentials) throws UsernameNotFoundException {
        //Allow either username or email auth
        User user = userRepository.findByUsernameOrEmail(loginCredentials, loginCredentials)
                .orElseThrow(() -> new UsernameNotFoundException("Unable to find user " +
                        "with credentials: " + loginCredentials));
        return UserPrincipalDTO.create(user);
    }

    /**
     * This method is used by JWT Auth Filter
     * @param id
     * @return
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Unable to find user " +
                        "with id: " + id));
        return UserPrincipalDTO.create(user);
    }
}
