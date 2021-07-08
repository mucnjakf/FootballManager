package hr.mucnjakf.main;

import hr.mucnjakf.network.TcpServer;
import hr.mucnjakf.rmi.ChatServer;

public class MainApp {

    public static void main(String[] args) {
        ChatServer cs = new ChatServer();
        new Thread(TcpServer::acceptTeams).start();
        new Thread(TcpServer::acceptLeagueTableRequests).start();
    }
}
