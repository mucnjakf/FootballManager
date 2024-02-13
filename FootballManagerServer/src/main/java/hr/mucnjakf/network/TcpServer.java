package hr.mucnjakf.network;

import hr.mucnjakf.core.Match;
import hr.mucnjakf.model.Team;
import hr.mucnjakf.model.TeamTable;
import hr.mucnjakf.rmi.ChatServer;
import hr.mucnjakf.utilities.xml.XmlUtilities;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class TcpServer {

    public static final String PROPERTIES_FILE = "E:\\dev\\FootballManager\\socket.properties";
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

    public static void acceptTeams() {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PROPERTIES.getProperty(TCP_PORT_TEAMS)))) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            List<Team> teams = new ArrayList<>();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                Team team = (Team) ois.readObject();

                teams.add(team);

                if (teams.size() == 2) {
                    new Thread(() -> {
                        Match.startMatch(teams);
                    }).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void acceptLeagueTableRequests() {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PROPERTIES.getProperty(TCP_PORT_LEAGUE_TABLE)))) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());
                new Thread(() -> sendLeagueTable(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendLeagueTable(Socket clientSocket) {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String message = dis.readUTF();
            System.out.println(message);

            List<TeamTable> leagueTable = new ArrayList<>();

            XmlUtilities xml = new XmlUtilities();
            xml.readFromXml(leagueTable);

            oos.writeObject(leagueTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


