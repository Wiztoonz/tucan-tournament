package com.tournament.mvp.service;

import com.tournament.mvp.model.Match;

import java.util.List;

public interface CSVService {

    List<Match> readCSVs(List<String> paths);

}
