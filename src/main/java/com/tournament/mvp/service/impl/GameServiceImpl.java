package com.tournament.mvp.service.impl;

import com.tournament.mvp.exception.DifferentGamesException;
import com.tournament.mvp.model.*;
import com.tournament.mvp.service.GameService;

import java.util.*;
import java.util.stream.Collectors;

public class GameServiceImpl implements GameService {

    @Override
    public List<Match> playBasketball(List<Match> matches, int pointIndex, double... k) {
        validateMatches(matches, Game.BASKETBALL);
        for (Match match : matches) {
            List<MatchDetail> matchDetails = match.getMatchDetails();
            for (MatchDetail matchDetail : matchDetails) {
                double scoredPoints = matchDetail.getPoints().get(0) * k[0];
                double rebounds = matchDetail.getPoints().get(1) * k[1];
                double assists = matchDetail.getPoints().get(2) * k[2];
                double rating = scoredPoints + rebounds + assists;
                matchDetail.getPlayer().setRatingPoints(rating);
            }
            calculateRating(match, matchDetails, pointIndex);
        }
        return matches;
    }

    @Override
    public List<Match> playHandball(List<Match> matches, int pointIndex, double... k) {
        validateMatches(matches, Game.HANDBALL);
        for (Match match : matches) {
            List<MatchDetail> matchDetails = match.getMatchDetails();
            for (MatchDetail matchDetail : matchDetails) {
                double goalMade = matchDetail.getPoints().get(0) * k[0];
                double goalReceived = matchDetail.getPoints().get(1) * k[1];
                double rating = goalMade - goalReceived;
                matchDetail.getPlayer().setRatingPoints(rating);
            }
            calculateRating(match, matchDetails, pointIndex);
        }
        return matches;
    }

    private void calculateRating(Match match, List<MatchDetail> matchDetails, int pointIndex) {
        Player player = setMvp(match.getMatchDetails());
        TeamWinner teamWinner = teamWinner(match, matchDetails, pointIndex);
        match.setTeamWinner(teamWinner);
        matchDetails.forEach(detail -> {
            Player detailPlayer = detail.getPlayer();
            if (detailPlayer.equals(player)) {
                if (detailPlayer.isMVP() && detailPlayer.getTeamName().equals(teamWinner.getTeamName())) {
                    detail.getPlayer().setRatingPoints(detailPlayer.getRatingPoints() + 10.0);
                }
            }
        });
    }

    private TeamWinner teamWinner(Match match, List<MatchDetail> matchDetails, int pointIndex) {
        List<String> teamsList = matchDetails.stream()
                .map(MatchDetail::getPlayer)
                .map(Player::getTeamName)
                .collect(Collectors.toList());

        List<List<Double>> pointsList = matchDetails.stream()
                .map(MatchDetail::getPoints)
                .collect(Collectors.toList());


        List<TeamPoint> teamPointsList = new ArrayList<>();
        int i = 0;
        for (String teamName : teamsList) {
            TeamPoint teamPoint = TeamPoint.builder()
                    .teamName(teamName)
                    .build();
            List<Double> points = pointsList.get(i);
            teamPoint.setPoints(points);
            teamPointsList.add(teamPoint);
            i++;
        }

        Map<String, List<TeamPoint>> teamPoints = teamPointsList.stream().collect(Collectors.groupingBy(TeamPoint::getTeamName));

        List<TeamWinner> teamPointsSumList = new ArrayList<>();
        for (Map.Entry<String, List<TeamPoint>> entry : teamPoints.entrySet()) {
            String teamName = entry.getKey();
            List<TeamPoint> team = entry.getValue();
            double scoredPointSum = 0;
            for (TeamPoint teamPoint : team) {
                scoredPointSum += teamPoint.getPoints().get(pointIndex);
            }
            teamPointsSumList.add(new TeamWinner(teamName, scoredPointSum));
        }

        List<TeamPointDetail> teamPointDetails = teamPointsSumList.stream().
                map(teamPointsSumDetail ->
                        new TeamPointDetail(teamPointsSumDetail.getTeamName(), teamPointsSumDetail.getPoints()))
                .collect(Collectors.toList());
        match.setTeamPointsDetail(teamPointDetails);


        return teamPointsSumList.stream().max(Comparator.comparing(TeamWinner::getPoints)).orElseThrow();
    }

    private Player setMvp(List<MatchDetail> matchDetails) {
        Player mvp = matchDetails.stream()
                .map(MatchDetail::getPlayer)
                .max(Comparator.comparing(Player::getRatingPoints))
                .orElseThrow();
        for (MatchDetail matchDetail : matchDetails) {
            if (matchDetail.getPlayer().equals(mvp)) {
                matchDetail.setPlayer(mvp);
                matchDetail.getPlayer().setMVP(true);
            }
        }
        return mvp;
    }

    private void validateMatches(List<Match> matches, Game game) {
        List<Game> games = matches.stream().map(Match::getGame).collect(Collectors.toList());
        for (Game nextGame : games) {
            if (!nextGame.equals(game)) {
                throw new DifferentGamesException(String.format("The service works only with the game of %s. The service implementation can't handle %s", game, nextGame));
            }
        }
    }

}
