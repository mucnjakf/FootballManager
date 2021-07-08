package hr.mucnjakf.core;

import hr.mucnjakf.model.Team;
import hr.mucnjakf.model.TeamTable;
import hr.mucnjakf.network.UdpServer;
import hr.mucnjakf.rmi.ChatServer;
import hr.mucnjakf.utilities.xml.XmlUtilities;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Match {

    private static final Random rnd = new Random();

    public static final int frameRate = 500;
    public static final int halfTimeTimeout = 3000;

    private static UdpServer udpServer = new UdpServer();

    public static void startMatch(List<Team> teams) {
        startUdpServer();

        udpServer.sendData(teams);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        simulateGame(teams);
    }

    private static void simulateGame(List<Team> teams) {
        Team homeTeam = teams.get(0);
        Team awayTeam = teams.get(1);

        int homeTeamGoals = 0;
        int awayTeamGoals = 0;

        int r1 = rnd.nextInt(homeTeam.getPlayers().size());
        int r2 = rnd.nextInt(awayTeam.getPlayers().size());

        udpServer.sendData("Match started");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        udpServer.sendData("First half");

        try {
            for (int i = 1; i <= 90; ) {
                udpServer.sendData(i + ". minute - " + getRandomPlayerAttEventPassive(homeTeam.getName(), homeTeam.getPlayers().get(r1).getLastName()));
                r1 = rnd.nextInt(homeTeam.getPlayers().size());
                Thread.sleep(frameRate);
                i++;

                udpServer.sendData(i + ". minute - " + getRandomPlayerAttEventPassive(homeTeam.getName(), homeTeam.getPlayers().get(r1).getLastName()));
                r1 = rnd.nextInt(homeTeam.getPlayers().size());
                Thread.sleep(frameRate);
                i++;

                String homeTeamAggroEvent = getRandomPlayerAttEventAggressive(homeTeam.getName(), homeTeam.getPlayers().get(r1).getLastName());
                if (homeTeamAggroEvent.contains("GOAL")) {
                    homeTeamGoals++;
                }

                halfTimeEvent(i);

                udpServer.sendData(i + ". minute - " + homeTeamAggroEvent);
                r1 = rnd.nextInt(homeTeam.getPlayers().size());
                Thread.sleep(frameRate);
                i++;

                udpServer.sendData(i + ". minute - " + getRandomPlayerAttEventPassive(awayTeam.getName(), awayTeam.getPlayers().get(r2).getLastName()));
                r2 = rnd.nextInt(awayTeam.getPlayers().size());
                Thread.sleep(frameRate);
                i++;

                udpServer.sendData(i + ". minute - " + getRandomPlayerAttEventPassive(awayTeam.getName(), awayTeam.getPlayers().get(r2).getLastName()));
                r2 = rnd.nextInt(awayTeam.getPlayers().size());
                Thread.sleep(frameRate);
                i++;

                String awayTeamAggroEvent = getRandomPlayerAttEventAggressive(awayTeam.getName(), awayTeam.getPlayers().get(r2).getLastName());
                if (awayTeamAggroEvent.contains("GOAL")) {
                    awayTeamGoals++;
                }

                udpServer.sendData(i + ". minute - " + awayTeamAggroEvent);
                r2 = rnd.nextInt(awayTeam.getPlayers().size());
                Thread.sleep(frameRate);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        saveTeamsToLeagueTable(homeTeam, awayTeam, homeTeamGoals, awayTeamGoals);

        udpServer.sendData("Full time");
    }

    private static void saveTeamsToLeagueTable(Team currentHomeTeam, Team currentAwayTeam, int homeTeamGoals, int awayTeamGoals) {
        List<TeamTable> teamTables = new ArrayList<>();

        XmlUtilities xmlRead = new XmlUtilities();
        xmlRead.readFromXml(teamTables);

        TeamTable homeTeamTable = new TeamTable();
        TeamTable awayTeamTable = new TeamTable();

        for (TeamTable tt : teamTables) {
            if (tt.getName().equals(currentHomeTeam.getName())) {
                homeTeamTable = tt;
            } else if (tt.getName().equals(currentAwayTeam.getName())) {
                awayTeamTable = tt;
            }
        }

        teamTables.remove(homeTeamTable);
        teamTables.remove(awayTeamTable);

        homeTeamTable.setGamesPlayed(homeTeamTable.getGamesPlayed() + 1);
        homeTeamTable.setGoalsFor(homeTeamTable.getGoalsFor() + homeTeamGoals);
        homeTeamTable.setGoalsAgainst(homeTeamTable.getGoalsAgainst() + awayTeamGoals);

        awayTeamTable.setGamesPlayed(awayTeamTable.getGamesPlayed() + 1);
        awayTeamTable.setGoalsFor(awayTeamTable.getGoalsFor() + awayTeamGoals);
        awayTeamTable.setGoalsAgainst(awayTeamTable.getGoalsAgainst() + homeTeamGoals);

        if (homeTeamGoals > awayTeamGoals) {
            homeTeamTable.setWins(homeTeamTable.getWins() + 1);
            homeTeamTable.setPoints(homeTeamTable.getPoints() + 3);

            awayTeamTable.setLosses(awayTeamTable.getLosses() + 1);
        } else if (homeTeamGoals < awayTeamGoals) {
            homeTeamTable.setLosses(homeTeamTable.getLosses() + 1);

            awayTeamTable.setWins(awayTeamTable.getWins() + 1);
            awayTeamTable.setPoints(awayTeamTable.getPoints() + 3);
        } else {
            homeTeamTable.setDraws(homeTeamTable.getDraws() + 1);
            homeTeamTable.setPoints(homeTeamTable.getPoints() + 1);

            awayTeamTable.setDraws(awayTeamTable.getDraws() + 1);
            awayTeamTable.setPoints(awayTeamTable.getPoints() + 1);
        }

        teamTables.add(homeTeamTable);
        teamTables.add(awayTeamTable);

        teamTables.sort(Comparator.comparing(TeamTable::getPoints).reversed());

        XmlUtilities xmlWrite = new XmlUtilities();
        xmlWrite.writeToXml(teamTables);
    }

    private static void halfTimeEvent(int i) throws InterruptedException {
        if (i == 45) {
            udpServer.sendData("Half time break");
            Thread.sleep(halfTimeTimeout);
            udpServer.sendData("Second half");
        }
    }

    private static String getRandomPlayerAttEventAggressive(String team, String player) {
        ArrayList<String> events = new ArrayList<String>() {
            {
                add(team + " - " + player + " shoots the ball off target.");
                add(team + " - " + player + " shoots the ball off target.");
                add(team + " - " + player + " shoots the ball off target.");
                add(team + " - " + player + " shoots the ball off target.");
                add(team + " - " + player + " shoots the ball on target.");
                add(team + " - " + player + " shoots the ball on target.");
                add(team + " - " + player + " shoots the ball on target.");
                add(team + " - " + player + " shoots the ball and scores. GOAL!");
                add(team + " - " + player + " was offside.");
            }
        };

        int r = rnd.nextInt(events.size());

        return events.get(r);
    }

    private static String getRandomPlayerAttEventPassive(String team, String player) {
        ArrayList<String> events = new ArrayList<String>() {
            {
                add(team + " - " + player + " passes the ball.");
                add(team + " - " + player + " passes the ball.");
                add(team + " - " + player + " crosses the ball.");
            }
        };

        int r = rnd.nextInt(events.size());

        return events.get(r);
    }

    private static void startUdpServer() {
        udpServer.setDaemon(true);
        udpServer.start();
    }
}
