import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sportradar.Match;
import org.sportradar.MatchScoreboard;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchScoreboardTest {
    private MatchScoreboard scoreboard;

    @BeforeEach
    public void setUp() {
        scoreboard = new MatchScoreboard();
    }

    @Test
    public void testStartNewMatch() {
        scoreboard.startNewMatch("HomeTeam", "AwayTeam", Instant.now());
        assertEquals(1, scoreboard.getMatchesSummary().size());
    }

    @Test
    public  void testUpdateScore() {
        final String matchId = scoreboard.startNewMatch("HomeTeam", "AwayTeam", Instant.now());
        scoreboard.updateScore(matchId, 1, 0);
        Match match = scoreboard.getMatchesSummary().get(0);
        assertEquals(1, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    public void testFinishMatch() {
        final String matchId = scoreboard.startNewMatch("HomeTeam", "AwayTeam", Instant.now());
        scoreboard.finishMatch(matchId);
        assertTrue(scoreboard.getMatchesSummary().isEmpty());
    }

    @Test
    public void testMatchesSummaryOrder() {
        final String match1 = scoreboard.startNewMatch("Team1", "Team2", Instant.now());
        scoreboard.updateScore(match1, 2, 2); // Total score 4

        final String match2 = scoreboard.startNewMatch("Team3", "Team4", Instant.now());
        scoreboard.updateScore(match2, 3, 1); // Total score 4 but newer

        final String match3 = scoreboard.startNewMatch("Team5", "Team6", Instant.now());
        scoreboard.updateScore(match3, 1, 0); // Total score 1

        List<Match> summary = scoreboard.getMatchesSummary();
        assertEquals("Team3", summary.get(0).getHomeTeam());
        assertEquals("Team1", summary.get(1).getHomeTeam());
        assertEquals("Team5", summary.get(2).getHomeTeam());
    }
}
