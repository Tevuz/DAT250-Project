package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomUsernameService {

    private static final String[] prefixes = {
            "Speedy",
            "Silly",
            "Mighty",
            "Shiny",
            "Glorious",
            "Sneaky",
            "Chill",
            "Brave",
            "Lazy",
            "Witty",
            "Stormy",
            "Frosty",
            "Cloudy",
            "Rocky",
            "Sunny",
            "Windy",
            "Wild",
            "Aqua",
            "Shadowy",
            "Fiery",
            "Mystic",
            "Arcane",
            "Lunar",
            "Stellar",
            "Cosmic",
            "Phoenix",
            "Radiant",
            "Nebula",
            "Eternal"
    };

    private static final String[] postfixes = {
            "Panther",
            "Tiger",
            "Wolf",
            "Falcon",
            "Dragon",
            "Shark",
            "Bear",
            "Eagle",
            "Lynx",
            "Fox",
            "Blade",
            "Shield",
            "Flame",
            "Spark",
            "Stone",
            "Star",
            "Wave",
            "Ember",
            "Arrow",
            "Striker",
            "Seeker",
            "Wanderer",
            "Whisperer",
            "Slayer",
            "Guardian",
            "Sorcerer",
            "Prophet",
            "Hunter",
            "Voyager",
            "Mage"
    };

    private final UserRepository userRepository;

    public RandomUsernameService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getRandomUsername() {
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int i0 = random.nextInt(prefixes.length);
            int i1 = random.nextInt(postfixes.length);

            String username = prefixes[i0] + postfixes[i1];

            if (!userRepository.existsByUsername(username))
                return username;
        }

        for (int i = 0; i < 16; i++) {
            int i0 = random.nextInt(prefixes.length);
            int i1 = random.nextInt(postfixes.length);
            int i2 = random.nextInt(1000);

            String username = prefixes[i0] + postfixes[i1] + i2;

            if (!userRepository.existsByUsername(username))
                return username;
        }

        throw new RuntimeException("Unable to generate username");
    }
}
