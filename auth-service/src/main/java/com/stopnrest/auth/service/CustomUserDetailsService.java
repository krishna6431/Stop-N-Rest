package com.stopnrest.auth.service;


import com.stopnrest.auth.model.Admin;
import com.stopnrest.auth.model.User;
import com.stopnrest.auth.model.UserPrincipal;
import com.stopnrest.auth.repository.AdminRepository;
import com.stopnrest.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;
    @Autowired
    private AdminRepository adminRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user= repo.findByUserName(username);
        if(user == null){
            Admin admin = adminRepo.findByUserName(username);
            User user2= new User();
            if(admin != null){
                user2.setUserName(admin.getUserName());
                user2.setPassword(admin.getPassword());
                user = user2;
            }
            else{
                System.out.println("User 404");
                throw new UsernameNotFoundException("User 404");
            }
        }
        return new UserPrincipal(user);
    }

}
