package hr.mucnjakf.network;

import hr.mucnjakf.controller.MatchController;
import hr.mucnjakf.model.Team;
import hr.mucnjakf.utilities.convert.ByteUtilities;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;
import java.util.Properties;

public class UdpClient extends Thread {

    public static final String PROPERTIES_FILE = "E:\\dev\\FootballManager\\socket.properties";
    public static final String UDP_PORT = "UDP_PORT";
    public static final String GROUP = "GROUP";
    public static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MatchController controller;

    public UdpClient(MatchController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try (MulticastSocket clientSocket = new MulticastSocket(Integer.parseInt(PROPERTIES.getProperty(UDP_PORT)))) {
            InetAddress groupAddress = InetAddress.getByName(PROPERTIES.getProperty(GROUP));
            clientSocket.joinGroup(groupAddress);

            while (true) {
                byte[] teamNumberOfBytes = new byte[4];
                DatagramPacket packet = new DatagramPacket(teamNumberOfBytes, teamNumberOfBytes.length);
                clientSocket.receive(packet);

                int length = ByteUtilities.byteArrayToInt(teamNumberOfBytes);
                byte[] teamBytes = new byte[length];

                packet = new DatagramPacket(teamBytes, teamBytes.length);
                clientSocket.receive(packet);

                try (ByteArrayInputStream bais = new ByteArrayInputStream(teamBytes);
                     ObjectInputStream ois = new ObjectInputStream(bais)) {
                    Object data = ois.readObject();
                    Platform.runLater(() -> handleReceivedData(data));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleReceivedData(Object data) {
        if (data instanceof List) {
            controller.showTeams((List<Team>) data);
        } else if (data instanceof String) {
            if (((String) data).contains("CHAT")) {
                String chatData = ((String) data).substring(4);
                controller.showMessage(chatData);
            } else {
                controller.showEvent((String) data);
            }
        }
    }
}
