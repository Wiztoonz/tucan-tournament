package com.tournament.mvp.service.impl;


import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.tournament.mvp.exception.CsvException;
import com.tournament.mvp.exception.CustomFileNotFoundException;
import com.tournament.mvp.exception.SamePlayerException;
import com.tournament.mvp.model.Game;
import com.tournament.mvp.model.Match;
import com.tournament.mvp.model.MatchDetail;
import com.tournament.mvp.model.Player;
import com.tournament.mvp.service.CSVService;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVServiceImpl implements CSVService {

    private final static char SEPARATOR = ';';

    @Override
    public List<Match> readCSVs(List<String> paths) {
        return paths.stream()
                .map(this::readGameMatches)
                .collect(Collectors.toList());
    }

    private Match readGameMatches(String path) {
        List<List<String>> csvData = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder().withSeparator(SEPARATOR).build();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(path)).withCSVParser(parser).build()) {
            String[] csvLine;
            while ((csvLine = reader.readNext()) != null) {
                csvData.add(Arrays.asList(csvLine));
            }
        } catch (CsvValidationException e) {
            throw new CsvException("Invalid CSV Data");
        } catch (IOException e) {
            throw new CustomFileNotFoundException("Invalid file paths: " + path);
        }
        Match match = csvDataToGameMatches(csvData);
        validateMatchPlayer(match);
        return match;
    }

    private Match csvDataToGameMatches(List<List<String>> csvData) {
        List<MatchDetail> matchDetails = new ArrayList<>();
        Game game = null;
        try {
            game = Game.valueOf(csvData.get(0).get(0));
        } catch (IllegalArgumentException e) {
            throw new CsvException("Invalid CSV");
        }
        for (List<String> data : csvData) {
            if (data.size() > 1) {
                Player player = Player.builder()
                        .playerName(data.get(0))
                        .nickname(data.get(1))
                        .number(data.get(2))
                        .teamName(data.get(3))
                        .build();

                List<Double> points = data.subList(4, data.size()).stream()
                        .map(Double::parseDouble)
                        .collect(Collectors.toList());

                MatchDetail matchDetail = MatchDetail.builder()
                        .player(player)
                        .points(points)
                        .build();

                matchDetails.add(matchDetail);
            }
        }
        Match match = new Match();
        match.setGame(game);
        match.setMatchDetails(matchDetails);
        return match;
    }

    private void validateMatchPlayer(Match match) {
        long nicknameCount = match.getMatchDetails().stream()
                .map(MatchDetail::getPlayer)
                .map(Player::getNickname)
                .distinct()
                .count();
        if (nicknameCount != match.getMatchDetails().size()) {
            throw new SamePlayerException("The game duplicates the player");
        }
    }

}
