import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sportradar.Match;
import org.sportradar.MatchScoreboard;
import org.sportradar.exception.DuplicateCountryException;
import org.sportradar.exception.InvalidCountryNameException;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatchScoreboardTest {
    private MatchScoreboard scoreboard;

    @BeforeEach
    public void setUp() {
        scoreboard = new MatchScoreboard();
    }

    @Test
    public void testStartNewMatch() throws Exception {
        scoreboard.startNewMatch("Poland", "United States", Instant.now());
        assertEquals(1, scoreboard.getMatchesSummary().size());
    }

    @Test
    public void testStartNewMatchWithNullParameters() {
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startNewMatch("HomeTeam", "AwayTeam", null));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startNewMatch("HomeTeam", null, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startNewMatch(null, "AwayTeam", Instant.now()));
        assertEquals(0, scoreboard.getMatchesSummary().size());
    }

    @Test
    public void testStartNewMatchWithWrongCountryName() {
        assertThrows(InvalidCountryNameException.class, () -> scoreboard.startNewMatch("Poland", "Super Earth", Instant.now()));
        assertThrows(InvalidCountryNameException.class, () -> scoreboard.startNewMatch("Narnia", "Poland", Instant.now()));
        assertEquals(0, scoreboard.getMatchesSummary().size());
    }

    @Test
    public void testStartNewMatchWithSameCountryName() {
        assertThrows(DuplicateCountryException.class, () -> scoreboard.startNewMatch("Poland", "Poland", Instant.now()));
        assertEquals(0, scoreboard.getMatchesSummary().size());
    }

    @Test
    public void testUpdateScore() throws Exception {
        final String matchId = scoreboard.startNewMatch("Poland", "United States", Instant.now());
        scoreboard.updateScore(matchId, 1, 0);
        Match match = scoreboard.getMatchesSummary().get(0);
        assertEquals(1, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    public void testUpdateScoreWithNullParameters() throws Exception {
        final String matchId = scoreboard.startNewMatch("Poland", "United States", Instant.now());
        assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore(matchId, 1, null));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore(matchId, null, 0));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore(null, 1, 0));
    }

    @Test
    public void testFinishMatch() throws Exception {
        final String matchId = scoreboard.startNewMatch("Poland", "United States", Instant.now());
        scoreboard.finishMatch(matchId);
        assertTrue(scoreboard.getMatchesSummary().isEmpty());
    }

    @Test
    public void testFinishMatchWithNullParameters() throws Exception {
        scoreboard.startNewMatch("Poland", "United States", Instant.now());
        assertThrows(IllegalArgumentException.class, () -> scoreboard.finishMatch(null));
        assertFalse(scoreboard.getMatchesSummary().isEmpty());
    }

    @Test
    public void testMatchesSummaryOrder() throws Exception {
        final String match1 = scoreboard.startNewMatch("Poland", "United States", Instant.now());
        scoreboard.updateScore(match1, 2, 2); // Total score 4

        final String match2 = scoreboard.startNewMatch("France", "Finland", Instant.now());
        scoreboard.updateScore(match2, 3, 1); // Total score 4 but newer

        final String match3 = scoreboard.startNewMatch("Brazil", "Estonia", Instant.now());
        scoreboard.updateScore(match3, 1, 0); // Total score 1

        List<Match> summary = scoreboard.getMatchesSummary();
        assertEquals("France", summary.get(0).getHomeTeam());
        assertEquals("Poland", summary.get(1).getHomeTeam());
        assertEquals("Brazil", summary.get(2).getHomeTeam());
    }
}
