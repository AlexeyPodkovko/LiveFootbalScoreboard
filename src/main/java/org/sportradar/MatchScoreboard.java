package org.sportradar;

import org.sportradar.exception.DuplicateCountryException;
import org.sportradar.exception.InvalidCountryNameException;
import org.sportradar.validator.MatchValidator;
import org.sportradar.validator.ValidationUtils;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code MatchScoreboard} class manages sports matches, including starting new matches,
 * updating scores, finishing matches, and providing a summary of matches in progress. It uses
 * a thread-safe approach with {@code ConcurrentHashMap} to ensure safe access and modification
 * of match data across multiple threads.
 *
 * <p>Each match is identified by a unique ID and contains information about the home and away teams,
 * as well as the current score and start time. This class provides methods to manipulate and query
 * this data in a thread-safe manner.</p>
 *
 * <p>It relies on {@code MatchValidator} for validating country names and ensuring that match
 * teams do not duplicate. Also, it uses {@code ValidationUtils} for null checks, adhering to
 * a fail-fast approach to avoid illegal states.</p>
 *
 * @author Alexey Podkovko
 */
public class MatchScoreboard {

    private final Map<String, Match> activeMatches = new ConcurrentHashMap<>();

    /**
     * Starts a new match with the given team names and start time. Validates the countries and
     * ensures they are not duplicates before adding the match to the active matches list.
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @param startTime The start time of the match.
     * @return A unique match ID for the newly started match.
     * @throws InvalidCountryNameException If the country names do not pass validation.
     * @throws DuplicateCountryException If the home and away team names are the same.
     */
    public String startNewMatch(final String homeTeam, final String awayTeam, final Instant startTime) throws InvalidCountryNameException, DuplicateCountryException {
        ValidationUtils.requireNonNull(homeTeam, awayTeam, startTime);
        MatchValidator.getInstance().validateCountries(homeTeam, awayTeam);

        final String matchId = UUID.randomUUID().toString();
        final Match match = new Match(homeTeam, awayTeam, startTime);

        activeMatches.put(matchId, match);

        return matchId;
    }

    /**
     * Updates the score for a match identified by the given match ID.
     *
     * @param matchId The unique identifier of the match.
     * @param homeScore The new score for the home team.
     * @param awayScore The new score for the away team.
     * @throws IllegalArgumentException If any of the arguments are null.
     */
    public void updateScore(final String matchId, final Integer homeScore,  final Integer awayScore) {
        ValidationUtils.requireNonNull(matchId, homeScore, awayScore);

        if (activeMatches.containsKey(matchId)) {
            final Match match = activeMatches.get(matchId);

            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
        }
    }

    /**
     * Finishes a match by removing it from the list of active matches based on the given match ID.
     *
     * @param matchId The unique identifier of the match to finish.
     * @throws IllegalArgumentException If the matchId is null.
     */
    public void finishMatch(final String matchId) {
        ValidationUtils.requireNonNull(matchId);

        activeMatches.remove(matchId);
    }

    /**
     * Provides a summary of all active matches, sorted by total score in descending order.
     * In case of a tie, matches are further sorted by their start time in reverse order.
     *
     * @return A list of {@code Match} objects representing the active matches.
     */
    public List<Match> getMatchesSummary() {
        return activeMatches.values().stream()
                .sorted(Comparator.comparingInt((Match m) -> m.getHomeScore() + m.getAwayScore())
                        .reversed()
                        .thenComparing(Match::getStartTime, Comparator.reverseOrder()))
                .toList();
    }
}
