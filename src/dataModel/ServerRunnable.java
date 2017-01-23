package dataModel;

import javafx.application.Platform;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;



public class ServerRunnable implements Runnable {

    private ServerRequest request;
    private Consumer<ServerResponse> parse;

    public ServerRunnable(ServerRequest request, Consumer<ServerResponse> parse) {
    	System.out.println("server request");
        this.request = request;
        this.parse = parse;
    }

    @Override
    public void run() {
        try {
        	System.out.println(request.requestName);
            Socket socket = new Socket("localhost", 6666);
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(request);
            System.out.println("Client: " + request);

            Object object = objectInputStream.readObject();
    		if (object instanceof ServerResponse) {
                System.out.println("Client: " + (ServerResponse) object);
            }
            if (object instanceof ServerResponse) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        parse.accept((ServerResponse) object);
                    }
                });
                //parse.accept((ServerResponse) object);
            }
    		objectInputStream.close();
    		objectOutputStream.close();
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
