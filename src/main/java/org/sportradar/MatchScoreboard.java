package org.sportradar;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
public class MatchScoreboard {

    private final Map<String, Match> activeMatches = new HashMap<>();

    public String startNewMatch(final String homeTeam, final String awayTeam, final Instant startTime) {
        final String matchId = UUID.randomUUID().toString();
        final Match match = new Match(homeTeam, awayTeam, startTime);

        activeMatches.put(matchId, match);

        return matchId;
    }

    public void updateScore(final String matchId, final int homeScore, final int awayScore) {
        if (activeMatches.containsKey(matchId)) {
            final Match match = activeMatches.get(matchId);

            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
        }
    }

    public void finishMatch(final String matchId) {
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
