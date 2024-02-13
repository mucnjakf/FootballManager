package hr.mucnjakf.rmi;

import hr.mucnjakf.network.UdpServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ChatServer {

    private static final int REGISTRY_PORT = 1099;
    private static final int CHAT_SERVICE_PORT = 0;
    public static final String SERVER_NAME = "server";

    private Registry registry;

    private ChatService chatService;

    private static UdpServer udpServer = new UdpServer();

    public ChatServer() {
        startUdpServer();
        publishChatServer();
    }

    private void startUdpServer() {
        udpServer.setDaemon(true);
        udpServer.start();
    }

    private void publishChatServer() {
        chatService = message -> udpServer.sendData("CHAT" + message);

        try {
            registry = LocateRegistry.createRegistry(REGISTRY_PORT);
            ChatService stub = (ChatService) UnicastRemoteObject.exportObject(chatService, CHAT_SERVICE_PORT);
            registry.rebind(SERVER_NAME, stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
