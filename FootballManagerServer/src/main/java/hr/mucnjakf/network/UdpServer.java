package hr.mucnjakf.network;

import hr.mucnjakf.utilities.convert.ByteUtilities;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;

public class UdpServer extends Thread {

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

    private LinkedBlockingDeque<Object> dataCollection = new LinkedBlockingDeque<>();

    public void sendData(Object data) {
        dataCollection.add(data);
    }

    @Override
    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            while (true) {
                if (!dataCollection.isEmpty()) {
                    Object data = dataCollection.getFirst();
                    dataCollection.clear();

                    byte[] dataBytes;
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                         ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                        oos.writeObject(data);
                        oos.flush();

                        dataBytes = baos.toByteArray();
                    }

                    int length = dataBytes.length;
                    byte[] dataNumberOfBytes = ByteUtilities.intToByteArray(length);

                    InetAddress groupAddress = InetAddress.getByName(PROPERTIES.getProperty(GROUP));
                    DatagramPacket packet = new DatagramPacket(dataNumberOfBytes, dataNumberOfBytes.length, groupAddress, Integer.parseInt(PROPERTIES.getProperty(UDP_PORT)));
                    serverSocket.send(packet);

                    packet = new DatagramPacket(dataBytes, dataBytes.length, groupAddress, Integer.parseInt(PROPERTIES.getProperty(UDP_PORT)));
                    serverSocket.send(packet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
