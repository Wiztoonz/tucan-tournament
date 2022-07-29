package com.tournament.mvp.service.impl;

import com.tournament.mvp.exception.CustomFileNotFoundException;
import com.tournament.mvp.exception.DifferentGamesException;
import com.tournament.mvp.model.Match;
import com.tournament.mvp.model.MatchDetail;
import com.tournament.mvp.model.Player;
import com.tournament.mvp.model.TeamWinner;
import com.tournament.mvp.service.CSVService;
import com.tournament.mvp.service.GameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceImplTest {

    private static CSVService csvService;
    private static GameService gameService;

    private static String FILE_PATH = "../mvp/src/test/resources/";

    private static final String FILE1 = "file1.csv";
    private static final String FILE4 = "file4.csv";
    private static final String FILE10 = "file10.csv";
    private static final String FILE11 = "file11.csv";

    private static List<String> basketball;
    private static List<String> handball;
    private static List<String> different;

    private List<Match> matches;
    private List<Match> processedGameMatches;

    @BeforeAll
    public static void init() {
        csvService = new CSVServiceImpl();
        gameService = new GameServiceImpl();
        basketball = List.of(FILE_PATH + FILE1);
        handball = List.of(FILE_PATH + FILE4);
        different = List.of(FILE_PATH + FILE10, FILE_PATH + FILE11);
    }

    @Test
    void playHasMVPBasketball() {
        matches = csvService.readCSVs(basketball);
        processedGameMatches = gameService.playBasketball(matches, 0, 2, 1, 1);
        Player player = processedGameMatches.get(0).getMatchDetails().stream()
                .map(MatchDetail::getPlayer)
                .filter(Player::isMVP)
                .findFirst()
                .get();

        assertTrue(player.isMVP());
    }

    @Test
    void basketballTeamA_NotWonAndMvpDidNotReceiveAdditionalPoints() {
        matches = csvService.readCSVs(basketball);
        processedGameMatches = gameService.playBasketball(matches, 0, 2, 1, 1);
        Match notProcessedGameMatch = matches.get(0);
        Match processedGameMatch = processedGameMatches.get(0);
        TeamWinner teamWinner = processedGameMatch.getTeamWinner();

        Player mvpPlayer = processedGameMatches.get(0).getMatchDetails().stream()
                .map(MatchDetail::getPlayer)
                .filter(Player::isMVP)
                .findFirst()
                .get();

        Player player = notProcessedGameMatch.getMatchDetails().stream()
                .map(MatchDetail::getPlayer)
                .filter(playerDetail ->
                        playerDetail.getNickname().equals(mvpPlayer.getNickname()))
                .findFirst()
                .get();


        assertNotEquals(mvpPlayer.getTeamName(), teamWinner.getTeamName());
        assertEquals(mvpPlayer.getRatingPoints(), player.getRatingPoints());
    }

    @Test
    void playHasMVPHandball() {
        matches = csvService.readCSVs(handball);
        processedGameMatches = gameService.playHandball(matches, 0, 2, 1);
        Player player = processedGameMatches.get(0).getMatchDetails().stream()
                .map(MatchDetail::getPlayer)
                .filter(Player::isMVP)
                .findFirst()
                .get();

        assertTrue(player.isMVP());
    }

    @Test
    void handballTeamB_isWonAndMvpDidReceiveAdditionalPoints() {
        matches = csvService.readCSVs(handball);
        Match match = matches.get(0);
        MatchDetail featureMVP = match.getMatchDetails().get(1);
        List<Double> points = featureMVP.getPoints();
        double goalMade = points.get(0) * 2;
        double goalReceived = points.get(1) * 1;
        double rating = goalMade - goalReceived;
        processedGameMatches = gameService.playHandball(matches, 0, 2, 1);
        Match processedGameMatch = processedGameMatches.get(0);
        TeamWinner teamWinner = processedGameMatch.getTeamWinner();

        Player mvpPlayer = processedGameMatch.getMatchDetails().stream()
                .map(MatchDetail::getPlayer)
                .filter(Player::isMVP)
                .findFirst()
                .get();


        assertEquals(mvpPlayer.getTeamName(), teamWinner.getTeamName());
        assertEquals(mvpPlayer.getRatingPoints(), rating + 10);
    }

    @Test
    void testReadDifferentGamesUseBasketball() {
        DifferentGamesException differentGamesException = assertThrows(DifferentGamesException.class, () -> {
            matches = csvService.readCSVs(different);
            processedGameMatches = gameService.playBasketball(matches, 0, 2, 1, 1);
        }, "The service works only with the game of BASKETBALL. The service implementation can't handle HANDBALL");
        assertEquals("The service works only with the game of BASKETBALL. The service implementation can't handle HANDBALL", differentGamesException.getMessage());
    }

    @Test
    void testReadDifferentGamesUseHandball() {
        DifferentGamesException differentGamesException = assertThrows(DifferentGamesException.class, () -> {
            matches = csvService.readCSVs(different);
            processedGameMatches = gameService.playHandball(matches, 0, 2, 1);
        }, "The service works only with the game of HANDBALL. The service implementation can't handle BASKETBALL");
        assertEquals("The service works only with the game of HANDBALL. The service implementation can't handle BASKETBALL", differentGamesException.getMessage());

    }

}