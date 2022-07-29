package com.tournament.mvp.service;

import com.tournament.mvp.model.Match;

import java.util.List;

public interface GameService {

    List<Match> playBasketball(List<Match> matches, int pointIndex, double... k);

    List<Match> playHandball(List<Match> matches, int pointIndex, double... k);

}
