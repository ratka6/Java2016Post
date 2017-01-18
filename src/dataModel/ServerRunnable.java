package dataModel;

import javafx.application.Platform;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.function.Consumer;



public class ServerRunnable implements Runnable {

    private ServerRequest request;
    private Consumer<ServerResponse> parse;

    public ServerRunnable(ServerRequest request, Consumer<ServerResponse> parse) {
        this.request = request;
        this.parse = parse;
    }

    @Override
    public void run() {
        try {
            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket();
            byte[] dataIn = new byte[1024];
            byte[] dataOut = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            System.out.println("Client: " + request);
            dataOut = baos.toByteArray();
            socket.send(new DatagramPacket(dataOut, dataOut.length, serverAddress, 6000));
            oos.close();
            baos.close();

            byte[] data;
            DatagramPacket packet = new DatagramPacket(dataIn, dataIn.length, serverAddress, 6000);
            socket.receive(packet);
            data = packet.getData();
            System.out.println("out" + data);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            if (object instanceof ServerResponse) {
                System.out.println("Client: " + (ServerResponse) object);
            }
            ois.close();
            bais.close();

            if (object instanceof ServerResponse) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        parse.accept((ServerResponse) object);
                    }
                });
                //parse.accept((ServerResponse) object);
            }
        } catch (SocketException e) {
            System.out.println("Client Socket error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Client IOError: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Client Classnotfound: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
