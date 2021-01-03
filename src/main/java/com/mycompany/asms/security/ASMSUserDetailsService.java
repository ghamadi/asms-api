package com.mycompany.asms.security;

import com.mycompany.asms.dao.UserDAO;
import com.mycompany.asms.exceptions.RecordNotFoundException;
import com.mycompany.asms.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ASMSUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public ASMSUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user;
        try {
            user = (User) userDAO.selectByIDs(new String[] {username}).get(username);
        }catch (RecordNotFoundException e) {
            throw new UsernameNotFoundException("Bad Credentials");
        }
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return userDAO.getRoles(username);
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getId();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return user.isActive();
            }
        };
    }
}
