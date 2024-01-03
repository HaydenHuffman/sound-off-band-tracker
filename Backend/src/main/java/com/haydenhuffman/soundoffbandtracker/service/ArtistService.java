package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.ArtistRepository;
import com.haydenhuffman.soundoffbandtracker.repository.PerformanceRepository;
import com.haydenhuffman.soundoffbandtracker.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ArtistService {
    private ArtistRepository artistRepository;
    private PerformanceRepository performanceRepository;
    private UserRepository userRepository;

    public ArtistService(ArtistRepository artistRepository, PerformanceRepository performanceRepository, UserRepository userRepository) {
        this.artistRepository = artistRepository;
        this.performanceRepository = performanceRepository;
        this.userRepository = userRepository;
    }

    public Artist createRandomArtist(User user, Artist artist) {
        artist.setName(generateArtistName());
        artist.setUser(user);
        userRepository.save(user);
        artistRepository.save(artist);
        user.getArtists().add(artist);
        return artist;
    }

    public Artist findById(Long artistId) {
        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        Artist artist = artistOpt.orElse(new Artist());
        Hibernate.initialize(artist.getPerformances()); // Initialize performances
        return artist;
    }

    public void save(User user, Artist artist) {
        artist.setUser(user);
        artistRepository.save(artist);
    }

    public void delete(Long artistId) {
        Artist artist = findById(artistId);
        performanceRepository.deleteAll(artist.getPerformances());
        artistRepository.deleteById(artistId);
    }

    public void createNewArtist(User user, Artist artist) {
        artist.setUser(user);
        artistRepository.save(artist);
        user.getArtists().add(artist);
    }


    public Double calculateAggScore(Artist artist) {
        if (artist.getPerformances() == null || artist.getPerformances().isEmpty()) {
            return 0.0;
        }
        Double average = calculateAverageRating(artist);
        Long daysSinceLastPerformance = daysSinceLastPerformance(artist.getArtistId());
        Double artistAggScore = average * (1.1 - (1 / daysSinceLastPerformance));
        Double roundedAggScore = (double) (Math.round(artistAggScore * 10) / 10);
        return roundedAggScore;
    }

    public Double calculateAverageRating(Artist artist) {
        if (artist.getPerformances() == null || artist.getPerformances().isEmpty()) {
            return 0.0;
        }
        double totalRating = artist.getPerformances().stream()
                .map(Performance::getPerfScore)
                .filter(Objects::nonNull)
                .mapToDouble(perfScore -> perfScore)
                .average()
                .orElse(0.0);

        return totalRating;
    }

    public Long daysSinceLastPerformance(Long artistId) {
        Artist currentArtist = findById(artistId);
        if (currentArtist.getPerformances() == null || currentArtist.getPerformances().isEmpty()) {
            return 0L;
        }

        return currentArtist.getPerformances().stream()
                .filter(performance -> performance.getDate() != null)
                .max(Comparator.comparing(Performance::getDate))
                .map(Performance::getDate)
                .map(date -> ChronoUnit.DAYS.between(date, LocalDate.now()))
                .orElse(0L);
    }

    public void updateArtistAggScore(Artist artist) {
        double newScore = calculateAggScore(artist);
        artist.setAggScore(newScore);
//        artistRepository.save(artist);
    }

    public String generateArtistName() {
        List<String> prefixes = Arrays.asList("Flaming", "Black", "Talking", "Broken", "Ashen", "Rainbow",
                "Wandering", "Lost", "Breathing", "Rough", "Rolling", "Thundering", "Hipster", "Punk", "Goth",
                "White", "Pale", "Lunar", "Mystic", "Screaming", "Sexy", "Diabolical", "Evil", "Thumping",
                "Ascending", "Falling", "Spinning", "Drooling", "Secret", "Mad", "Hot", "Veiled", "Hidden",
                "Psychic", "Nightly", "Eerie", "Transparent", "Wild", "Smashing", "Chunking", "Guns and",
                "Roamin' ", "Stylish", "Creepy", "Nerdy", "Anti", "Panoramic", "McShizzle", "Pensive",
                "Crushing", "Dead Man's", "Lords of", "Burnt", "Wheeled", "Living", "Azure", "Undead",
                "Exploding", "Spiralling", "Boom-boom", "Serious", "Stoic", "Deep", "Somber", "Squirming",
                "Vanilla", "Screeching", "Thrashing", "Selective", "Swift", "Soaring", "Mighty");

        List<String> suffixes = Arrays.asList("Flames", "Banisters", "Skulls", "Unicorns", "Souls", "Corpses",
                "Flannel", "Zombies", "Lampshades", "Scientists", "Ghosts", "Dude and His Merry Gang of Band Nerds",
                "Hunters", "Sirens", "Lozenges", "Stones", "Heads", "Hands", "Cerulean", "Lightning", "Blades",
                "Gang", "Homeboys", "Critics", "Emos", "Rebels", "Pirates", "Pumpkins", "Roses", "Demons", "Tractors",
                "Men", "Gals", "Riders", "Ray-Bans", "Trenchcoats", "Creepers", "Gravity", "Diamonds", "Mirrors",
                "Jefes", "Esperantos", "Crimson", "Wrappers", "José y los gauchos", "Heat", "Fever", "Wave", "Spell",
                "Spectacle", "Voices", "Group", "Fliers", "Homies", "Rum", "Wheels", "Brits", "Machines", "Assassination",
                "Flint", "Noises", "Perspiration", "Perpetrator", "Betrayed", "Wasslers", "Whirlpool", "Pistols",
                "Boulders", "Trinkets", "Rag Dolls", "Bazookas", "Popsicle", "Ice Cubes", "Banshees", "Frost", "Darkness",
                "Beat", "Freeze", "Kittens", "Superheroes", "Bagel", "Flaxseeds", "Children", "Love", "Equinox", "Life");

        String randomPrefix = prefixes.get((int) Math.floor(Math.random() * prefixes.size()));
        String randomSuffix = suffixes.get((int) Math.floor(Math.random() * suffixes.size()));

        return randomPrefix + " " + randomSuffix;


    }


}
