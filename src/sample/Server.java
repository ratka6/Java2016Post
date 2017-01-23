package sample;

import dataModel.*;
import dataModel.Package;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ServerSocket serverSocket = null;
    	ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(6666);
            System.out.println("tu1");
            while (true) {
                System.out.println("tu2");
                Socket socket = serverSocket.accept();
                executorService.execute(new ServerTask(socket));
            }
        }
        catch (SocketException e) {
            System.out.println("Server Socket error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Server IOError: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
			executorService.shutdown();
		}
    }
         
}
