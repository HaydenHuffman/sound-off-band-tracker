package com.haydenhuffman.soundoffbandtracker.service;

import com.haydenhuffman.soundoffbandtracker.domain.Artist;
import com.haydenhuffman.soundoffbandtracker.domain.Performance;
import com.haydenhuffman.soundoffbandtracker.domain.User;
import com.haydenhuffman.soundoffbandtracker.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ArtistService {
    private ArtistRepository artistRepository;
    private UserServiceImpl userService;
    public ArtistService(ArtistRepository artistRepository, UserServiceImpl userService) {
        this.artistRepository = artistRepository;
        this.userService = userService;
    }
    public Artist createNewArtist(Long userId, Artist artist) {
        User user = userService.findById(userId);
        user.getArtists().add(artist);
        artist.setName(createArtistName());
        artist.setUser(user);
        artist.setImage("/images/band.jpg");
        artistRepository.save(artist);
        return artist;

    }

    public Artist findById(Long artistId) {
        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        return artistOpt.orElse(new Artist());
    }

    public void save(Artist artist) {
        artistRepository.save(artist);
    }

    public void delete(Long artistId) {
        artistRepository.deleteById(artistId);
    }

    public Double calculateAggScore(Artist artist) {
        if (artist.getPerformances() == null || artist.getPerformances().isEmpty()) {
            return 0.0;
        }

        Double artistAggScore = calculateAverageRating(artist) * (1.1 - (1 / daysSinceLastPerformance(artist)));
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

    public Long daysSinceLastPerformance(Artist artist) {
        if (artist.getPerformances() == null || artist.getPerformances().isEmpty()) {
            return 0L;
        }

        return artist.getPerformances().stream()
                .filter(performance -> performance.getDate() != null)
                .max(Comparator.comparing(Performance::getDate))
                .map(Performance::getDate)
                .map(date -> ChronoUnit.DAYS.between(date, LocalDate.now()))
                .orElse(0L);
    }

    public void updateArtistAggScore(Artist artist) {
        double newScore = calculateAggScore(artist);
        artist.setAggScore(newScore);
        artistRepository.save(artist);
    }

    public String createArtistName(){
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

    public List<Artist> findTopArtists(Long userId) {
        User user = userService.findById(userId);
        List<Artist> topArtists = user.getArtists().stream()
                .filter(artist -> artist.getAggScore() != null)
                .sorted((a1, a2) -> a2.getAggScore().compareTo(a1.getAggScore()))
                .limit(5)
                .toList();
        return topArtists;
    }

}
