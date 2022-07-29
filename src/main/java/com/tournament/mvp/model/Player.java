package com.tournament.mvp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    private String playerName;
    private String nickname;
    private String number;
    private String teamName;
    private double ratingPoints;
    private boolean isMVP;

}
