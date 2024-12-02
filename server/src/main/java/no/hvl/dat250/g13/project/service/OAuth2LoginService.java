package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

@Service
public class OAuth2LoginService extends DefaultOAuth2UserService {

    Logger logger = LoggerFactory.getLogger(OAuth2LoginService.class);

    private final UserRepository userRepository;
    private final RandomUsernameService randomUsernameService;

    public OAuth2LoginService(UserRepository userRepository, RandomUsernameService randomUsernameService) {
        this.randomUsernameService = randomUsernameService;
        logger.info("🔑 OAuth2LoginService");
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var oAuth2User = super.loadUser(userRequest);
        logger.info("🔑 Authenticating user: {}", oAuth2User);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        var result = switch (provider) {
            case "google" -> userRepository.findByGoogleId(parse(oAuth2User.getAttribute("sub")));
            case "github" -> userRepository.findByGithubId(parse(oAuth2User.getAttribute("id")));
            default -> throw new OAuth2AuthenticationException("Provider '" + provider + "' is not supported");
        };

        if (result.isEmpty()) {
            var user = new UserEntity();
            //user.setUsername(randomUsernameService.getRandomUsername());
            user.setUsername(oAuth2User.getAttribute("login"));
            logger.info("🔑 Creating user: {}", user.getUsername());
            switch (provider) {
                case "google" -> user.setGoogleId(parse(oAuth2User.getAttribute("sub")));
                case "github" -> user.setGithubId(parse(oAuth2User.getAttribute("id")));
            }
            userRepository.save(user);
        }

        return oAuth2User;
    }

    private static String parse(Object object) {
        if (object == null)
            return "";
        return switch (object) {
            case String string -> string;
            case Integer integer -> "" + integer;
            default -> "";
        };
    }

}
