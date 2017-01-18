package sample;

import dataModel.LoginData;
import dataModel.ServerRequest;
import dataModel.ServerResponse;
import dataModel.User;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by krzysiek on 07.01.2017.
 */
public class Server {

    private static LoginData loginData = new LoginData("aa", "aa");
    private static User user = User.fake();

    public static void main(String[] args) {
//        try {
//            DatagramSocket socket = new DatagramSocket(1234);
//            while(true) {
//                //odbior obiektu
//                byte bufA[] = new byte[256];
//                DatagramPacket packet = new DatagramPacket(bufA, bufA.length);
//                socket.receive(packet);
//                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bufA));
//                Object object = ois.readObject();
//                System.out.println(object);
//                //Wysylanie
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ObjectOutputStream oos = new ObjectOutputStream(baos);
//                ServerResponse response;
//                if (object instanceof ServerRequest) {
//                    ServerRequest request = (ServerRequest) object;
//                    if (request.requestName == "login") {
//                        LoginData data = (LoginData) request.object;
//                        if (data.id.equals(loginData.id) && data.password.equals(loginData.password)) {
//                            response = new ServerResponse("login", user);
//                            oos.writeObject(response);
//                        }
//                        else {
//                            response = new ServerResponse("login", null);
//                            response.error = "Błędne dane";
//                            oos.writeObject(response);
//                        }
//                    }
//                    else if (request.requestName == "register") {
//                        User data = (User) request.object;
//                            response = new ServerResponse("register", loginData);
//                            oos.writeObject(response);
//                    }
//                    bufA = baos.toByteArray();
//                    socket.send(new DatagramPacket(bufA, bufA.length, packet.getAddress(), packet.getPort()));
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        DatagramSocket socket = null;
        InetAddress inetAddress;

        try {
            socket = new DatagramSocket(6000);
            byte[] dataIn = new byte[1024];
            byte[] dataOut = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(dataIn, dataIn.length);
                socket.receive(packet);
                byte[] data = packet.getData();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object object = objectInputStream.readObject();
                if (object instanceof ServerRequest) {
                    System.out.println("Server: " + (ServerRequest)object);
                }
                byteArrayInputStream.close();
                objectInputStream.close();


                Thread.sleep(5000);

                ServerResponse response;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                if (object instanceof ServerRequest) {
                    ServerRequest request = (ServerRequest) object;
                    if (request.requestName.equals("login")) {
                        LoginData log = (LoginData) request.object;
                        if (log.id.equals(loginData.id) && log.password.equals(loginData.password)) {
                            response = new ServerResponse("login", user);
                            oos.writeObject(response);
                            System.out.println("Server: " + response);
                        } else {
                            response = new ServerResponse("login", null);
                            response.error = "Błędne dane";
                            oos.writeObject(response);
                            System.out.println("Server: " + response);
                        }
                    } else if (request.requestName.equals("register")) {
                        User u = (User) request.object;
                        response = new ServerResponse("register", loginData);
                        oos.writeObject(response);
                        System.out.println("Server: " + response);
                    }

                    dataOut = baos.toByteArray();
                    socket.send(new DatagramPacket(dataOut, dataOut.length, packet.getAddress(), packet.getPort()));
                }
                oos.close();
                baos.close();
            }
        } catch (SocketException e) {
            System.out.println("Server Socket error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Server IOError: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Server Classnotfound: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
