package com.tournament;

import com.tournament.mvp.model.Match;
import com.tournament.mvp.service.CSVService;
import com.tournament.mvp.service.GameService;
import com.tournament.mvp.service.impl.CSVServiceImpl;
import com.tournament.mvp.service.impl.GameServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String FILE_PATH = "../mvp/src/main/resources/";
    // Basketball games
    private static final String FILE1 = "file1.csv";
    private static final String FILE2 = "file2.csv";
    private static final String FILE3 = "file3.csv";

    // Handball games
    private static final String FILE4 = "file4.csv";
    private static final String FILE5 = "file5.csv";
    private static final String FILE6 = "file6.csv";

    public static void main(String[] args) {

        // List of files where csv files is basketball games
        List<String> basketballGameCSVsList = new ArrayList<>();
        basketballGameCSVsList.add(FILE_PATH + FILE1);
        basketballGameCSVsList.add(FILE_PATH + FILE2);
        basketballGameCSVsList.add(FILE_PATH + FILE3);

        // List of files where csv files is handball games
        List<String> handballGameCSVsList = new ArrayList<>();
        handballGameCSVsList.add(FILE_PATH + FILE4);
        handballGameCSVsList.add(FILE_PATH + FILE5);
        handballGameCSVsList.add(FILE_PATH + FILE6);

        // Service for reading CSV
        CSVService csvService = new CSVServiceImpl();
        // Get basketball game data
        List<Match> basketballMatches = csvService.readCSVs(basketballGameCSVsList);
        // Get handball game data
        List<Match> handballMatches = csvService.readCSVs(handballGameCSVsList);

        // Service where implements different games
        GameService gameService = new GameServiceImpl();

        System.out.println("BASKETBALL");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println();

        // basketball
        List<Match> playedBasketballMatches = gameService.playBasketball(basketballMatches, 0, 2, 1, 1);

        // if the MPV player's team won the game, the extra points were already awarded
        for (Match playedBasketballMatch : playedBasketballMatches) {
            System.out.println(playedBasketballMatch);
            System.out.println();
            System.out.println();
        }

        System.out.println("HANDBALL");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println();

        // handball
        List<Match> playedHandballMatches = gameService.playHandball(handballMatches, 0, 2, 1);

        // if the MPV player's team won the game, the extra points were already awarded
        for (Match playedHandballMatch : playedHandballMatches) {
            System.out.println(playedHandballMatch);
            System.out.println();
            System.out.println();
        }

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------------");

    }

}
