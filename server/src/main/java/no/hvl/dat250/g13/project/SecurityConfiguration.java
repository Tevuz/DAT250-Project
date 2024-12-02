package no.hvl.dat250.g13.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.security.LoginHandler;
import no.hvl.dat250.g13.project.service.ProjectUserLoginService;
import no.hvl.dat250.g13.project.service.data.option.OptionCreate;
import no.hvl.dat250.g13.project.service.data.poll.PollCreate;
import no.hvl.dat250.g13.project.service.data.survey.SurveyCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final UserRepository  userRepository;
    private final SurveyRepository surveyRepository;

    @Value("classpath:${resources.client.config}")
    private Resource clientConfig;

    public SecurityConfiguration(UserRepository userRepository, SurveyRepository surveyRepository) {
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(request -> {
                request.requestMatchers("/").permitAll();
                request.requestMatchers("/favicon.ico").permitAll();
                request.requestMatchers("/assets/*").permitAll();
                request.requestMatchers("/error").permitAll();
                request.requestMatchers("/auth/*").permitAll();
                request.requestMatchers("/account/*").permitAll();
                request.requestMatchers(getClientEndpoints()).permitAll();
                request.anyRequest().authenticated(); })
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .successHandler(LoginHandler::onSuccess)
                .failureHandler(LoginHandler::onFailure)
            )
            .logout(form -> form
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/"))
            .oauth2Login(form -> form
                    .loginPage("/auth/login").permitAll()
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/error"))
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }

    @Bean
    CommandLineRunner addExamplePolls() {
        // TODO: demo data, remove in production
        logger.info("📃 Adding polls (TODO: remove)");
        return args -> {
            surveyRepository.save(new SurveyCreate("survey", 1L, List.of(
                    new PollCreate("What is your favorite color?", Optional.empty(), List.of(
                            new OptionCreate("Red", Optional.empty()),
                            new OptionCreate("Green", Optional.empty()),
                            new OptionCreate("Blue", Optional.empty())
                    ))
            )).into());
        };
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
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    UserDetailsService userDetailsService() {
//        return new UserDetailsLoginService(userRepository);
//    }

    @Bean
    AuthenticationManager customAuthenticationManager(ProjectUserLoginService loginService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(loginService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }


    private String[] getClientEndpoints() {
        var endpoints = new ArrayList<String>();
        try (InputStream inputStream = clientConfig.getInputStream()) {
            JsonNode rootNode = new ObjectMapper().readTree(inputStream);
            JsonNode pathsNode = rootNode.at("/client/paths");
            pathsNode.fieldNames().forEachRemaining(endpoints::add);
        } catch (IOException exception) {
            logger.error(exception.getMessage());
            return new String[0];
        }
        return endpoints.toArray(String[]::new);
    }
}
