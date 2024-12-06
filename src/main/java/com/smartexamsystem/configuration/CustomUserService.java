package com.smartexamsystem.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartexamsystem.entity.User; // Fully qualify the User class
import com.smartexamsystem.repository.UserRepository;

public class CustomUserService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            User user = repo.findByUsername(username);

            if(user==null) {
            	throw new UsernameNotFoundException("No User");
            }else {
            	return  new CustomUser(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
