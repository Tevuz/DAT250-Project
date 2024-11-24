package no.hvl.dat250.g13.project;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.security.ApiAccessFilter;
import no.hvl.dat250.g13.project.service.UserDetailsLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final UserRepository  userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/").permitAll();
                    request.requestMatchers("/favicon.ico").permitAll();
                    request.requestMatchers("/assets/*").permitAll();
                    request.requestMatchers("/error").permitAll();
                    request.requestMatchers("/login").permitAll();
                    request.anyRequest().authenticated(); })
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/"))
                .logout(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new ApiAccessFilter(), AuthorizationFilter.class)
                .build();
    }

    @Bean
    CommandLineRunner addExampleUsers() {
        // TODO: demo data, remove in production
        logger.info("👥‍ Adding users (TODO: remove)");
        return args -> {
            userRepository.save(new UserEntity(1L, "user", passwordEncoder().encode("password")));
            userRepository.save(new UserEntity(2L, "admin", passwordEncoder().encode("pa$$word")));
        };
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsLoginService(userRepository);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
