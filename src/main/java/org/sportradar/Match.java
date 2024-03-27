package org.sportradar;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Match {

    @EqualsAndHashCode.Include
    private String homeTeam;
    @EqualsAndHashCode.Include
    private String awayTeam;
    @EqualsAndHashCode.Include
    private Instant startTime;

    private Integer homeScore;
    private Integer awayScore;
}
