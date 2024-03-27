package org.sportradar;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class MatchScoreboard {

    public String startNewMatch(final String homeTeam, final String awayTeam, final Instant startTime) {
        return null;
    }

    public void updateScore(final String matchId, final int homeScore, final int awayScore) {
    }

    public void finishMatch(final String matchId) {
    }

    public List<Match> getMatchesSummary() {

        return null;
    }
}
