package hr.mucnjakf.network;

import hr.mucnjakf.model.Team;
import hr.mucnjakf.model.TeamTable;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TcpClient {

    public static final String PROPERTIES_FILE = "E:\\dev\\FootballManager\\socket.properties";
    public static final String HOST = "HOST";
    public static final String TCP_PORT_TEAMS = "TCP_PORT_TEAMS";
    public static final String TCP_PORT_LEAGUE_TABLE = "TCP_PORT_LEAGUE_TABLE";
    public static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendTeam(Team team) {
        try (Socket clientSocket = new Socket(PROPERTIES.getProperty(HOST), Integer.parseInt(PROPERTIES.getProperty(TCP_PORT_TEAMS)))) {
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(team);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<TeamTable> requestLeagueTable() {
        try (Socket clientSocket = new Socket(PROPERTIES.getProperty(HOST), Integer.parseInt(PROPERTIES.getProperty(TCP_PORT_LEAGUE_TABLE)))) {
            List<TeamTable> leagueTable = sendLeagueTableRequest(clientSocket);
            return leagueTable;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static List<TeamTable> sendLeagueTableRequest(Socket clientSocket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

        dos.writeUTF("REQUEST_LEAGUE_TABLE");

        List<TeamTable> leagueTable = (List<TeamTable>) ois.readObject();
        return leagueTable;
    }
}
