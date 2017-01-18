package sample;

import dataModel.*;
import dataModel.Package;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by krzysiek on 07.01.2017.
 */
public class Server {

    private static LoginData loginData = new LoginData("aa", "aa");
    private static User user = User.fake();
    private static ArrayList<PackageStatus> statuses;

    public static Double getRandom() {
        Random r = new Random();
        return 10.0 + (1000.0 - 10.0) * r.nextDouble();
    }

    public static void main(String[] args) {


        statuses = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i % 4 == 0) {
                PackageStatus status = new PackageStatus(""+i, "canceled");
                Package pack = new Package(getRandom(), getRandom(), getRandom(), getRandom(), getRandom());
                status.setPack(pack);
                statuses.add(status);
            }
            else {
                PackageStatus status = new PackageStatus(""+i, "delivered");
                Package pack = new Package(getRandom(), getRandom(), getRandom(), getRandom(), getRandom());
                status.setPack(pack);
                statuses.add(status);
            }
        }

        System.out.println(statuses);

        DatagramSocket socket = null;
        InetAddress inetAddress;

        try {
            socket = new DatagramSocket(6000);
            byte[] dataIn = new byte[8096];
            byte[] dataOut;
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
                ServerResponse response;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                if (object instanceof ServerRequest) {
                    ServerRequest request = (ServerRequest) object;

                    //Login - dodaj bledy
                    if (request.requestName.equals("login")) {
                        LoginData log = (LoginData) request.object;
                        if (log.id.equals(loginData.id) && log.password.equals(loginData.password)) {
                            response = new ServerResponse(request.requestName, user);
                            oos.writeObject(response);
                            System.out.println("Server: " + response);
                        } else {
                            response = new ServerResponse(request.requestName, null);
                            response.error = "Błędne dane";
                            oos.writeObject(response);
                        }

                    //Register - dodaj bledy
                    } else if (request.requestName.equals("register")) {
                        User u = (User) request.object;
                        response = new ServerResponse(request.requestName, loginData);
                        oos.writeObject(response);
                    }

                    //PackagesInfo - dodaj bledy
                    else if (request.requestName.equals("packagesInfo")) {
                        response = new ServerResponse(request.requestName, statuses);
                        oos.writeObject(response);
                    }

                    //OrderCourier - dodaj bledy
                    else if (request.requestName.equals("orderCourier")) {
                        response = new ServerResponse(request.requestName, null);
                        oos.writeObject(response);
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
        }


    }
}
