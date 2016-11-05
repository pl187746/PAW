package pl.iis.paw.trello.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component(value = "userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService{

    private Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Authenticating {}", s);
        String lowerCaseLogin = s.toLowerCase();
        User user = userRepository.findByLogin(lowerCaseLogin);
        if (user == null) {
            throw new UsernameNotFoundException("User " + lowerCaseLogin + " was not found");
        }

        List<SimpleGrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(lowerCaseLogin, user.getPassword(), grantedAuthorities);
    }
}
