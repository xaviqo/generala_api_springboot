package tech.xavi.generalabe.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.service.user.CommonUserService;
import tech.xavi.generalabe.service.user.UserService;


@Service
@AllArgsConstructor
public class GeneralaUserDetailsService implements UserDetailsService {

    private final CommonUserService commonUserService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        GeneralaUser generalaUser = commonUserService.findByUserId(userId);

        return User.builder()
                .username(generalaUser.getId())
                .password(generalaUser.getPassword())
                .disabled(!generalaUser.isEnabled())
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities(generalaUser.getAuthorities())
                .build();

        //TODO: If the builder isn't working, then use constructor!
        //return new User(player.getUsername(), player.getPassword(), player.isEnabled())
    }
}