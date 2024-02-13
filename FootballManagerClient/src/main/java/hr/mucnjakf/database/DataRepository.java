package hr.mucnjakf.database;

import hr.mucnjakf.model.Player;
import hr.mucnjakf.model.Team;
import hr.mucnjakf.utilities.serialization.SerializationUtilities;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {

    public static List<Player> getPlayersFromCsv() {
        List<Player> players = new ArrayList<>();
        String pathToFile =
                "E:\\dev\\FootballManager\\FootballManagerClient\\database\\players.csv";

        try (BufferedReader br =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(pathToFile), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                Player player = new Player(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5],
                        Integer.parseInt(data[6]));

                players.add(player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }

    public static Player getPlayerFromCsv(int id) {
        List<Player> players = getPlayersFromCsv();

        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    public static List<Team> getTeamsFromSaveFolder() {
        File directoryPath = new File("E:\\dev\\FootballManager\\FootballManagerClient\\save");
        File[] filesList = directoryPath.listFiles();

        List<Team> teams = new ArrayList<>();
        Team team = new Team();

        if (filesList != null && filesList.length > 0) {
            for (File file : filesList) {
                try {
                    team = (Team) SerializationUtilities.read(file);
                    teams.add(team);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return teams;
    }
}