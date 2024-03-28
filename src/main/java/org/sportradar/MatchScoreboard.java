package org.sportradar;

import org.sportradar.exception.DuplicateCountryException;
import org.sportradar.exception.InvalidCountryNameException;
import org.sportradar.validator.MatchValidator;
import org.sportradar.validator.ValidationUtils;

import java.time.Instant;
import java.util.*;

public class MatchScoreboard {

    private final Map<String, Match> activeMatches = new HashMap<>();

    public String startNewMatch(final String homeTeam, final String awayTeam, final Instant startTime) throws InvalidCountryNameException, DuplicateCountryException {
        ValidationUtils.requireNonNull(homeTeam, awayTeam, startTime);
        MatchValidator.getInstance().validateCountries(homeTeam, awayTeam);

        final String matchId = UUID.randomUUID().toString();
        final Match match = new Match(homeTeam, awayTeam, startTime);

        activeMatches.put(matchId, match);

        return matchId;
    }

    public void updateScore(final String matchId, final Integer homeScore,  final Integer awayScore) {
        ValidationUtils.requireNonNull(matchId, homeScore, awayScore);

        if (activeMatches.containsKey(matchId)) {
            final Match match = activeMatches.get(matchId);

            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
        }
    }

    public void finishMatch(final String matchId) {
        ValidationUtils.requireNonNull(matchId);

        activeMatches.remove(matchId);
    }

    public List<Match> getMatchesSummary() {
        return activeMatches.values().stream()
                .sorted(Comparator.comparingInt((Match m) -> m.getHomeScore() + m.getAwayScore())
                        .reversed()
                        .thenComparing(Match::getStartTime, Comparator.reverseOrder()))
                .toList();
    }
}
