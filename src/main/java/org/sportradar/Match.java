package org.sportradar;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class Match {

    @EqualsAndHashCode.Include
    @NonNull
    private String homeTeam;
    @EqualsAndHashCode.Include
    @NonNull
    private String awayTeam;
    @EqualsAndHashCode.Include
    @NonNull
    private Instant startTime;

    private Integer homeScore;
    private Integer awayScore;
}
