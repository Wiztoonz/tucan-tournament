package com.tournament.mvp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    private Game game;
    private List<MatchDetail> matchDetails;
    private TeamWinner teamWinner;
    private List<TeamPointDetail> teamPointsDetail;

}
