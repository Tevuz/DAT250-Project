package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectUserLoginService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(ProjectUserLoginService.class);

    private final UserRepository userRepository;

    public ProjectUserLoginService(UserRepository userRepository) {
        logger.info("🔑 UserDetailsLoginService");
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("👤 Finding: {}", username);
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new User(user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")/*TODO: add authorities*/));
    }
}
