package sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import dataModel.LoginData;
import dataModel.ServerRequest;
import dataModel.ServerResponse;
import dataModel.User;

public class ServerTask implements Runnable {
	
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private DBConnector dbConnector;

	public ServerTask(Socket socket) {
			this.socket = socket;
            System.out.println("tu3");
            dbConnector = new DBConnector();
			InputStream is;
			try {
				OutputStream out = socket.getOutputStream();
				objectOutputStream = new ObjectOutputStream(out);
				is = socket.getInputStream();
				objectInputStream = new ObjectInputStream(is);		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	@Override
	public void run() {		
		System.out.println("lolol");

		try {
            System.out.println("tu4");
			Object object = objectInputStream.readObject();
			if (object instanceof ServerRequest) {
		      System.out.println("Server: " + (ServerRequest)object);
		    }
		ServerResponse response;
		ServerRequest request = (ServerRequest) object;
		if (request.requestName.equals("login")) {
		    LoginData log = (LoginData) request.object;
		    response = dbConnector.loginCheck(log);
		    System.out.println(response.toString());
		    objectOutputStream.writeObject(response);
		} else if (request.requestName.equals("register")) {
		    User u = (User) request.object;
//		    response = new ServerResponse(request.requestName, loginData);
//			objectOutputStream.writeObject(response);
		}
		
		//PackagesInfo - dodaj bledy
		else if (request.requestName.equals("packagesInfo")) {
//		    response = new ServerResponse(request.requestName, statuses);
//			objectOutputStream.writeObject(response);
		}
		//OrderCourier - dodaj bledy
		else if (request.requestName.equals("orderCourier")) {
		    response = new ServerResponse(request.requestName, null);
			objectOutputStream.writeObject(response);
		}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}



//  ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
//ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//Object object = objectInputStream.readObject();
//if (object instanceof ServerRequest) {
//  System.out.println("Server: " + (ServerRequest)object);
//}
//byteArrayInputStream.close();
//objectInputStream.close();
//ServerResponse response;
//ByteArrayOutputStream baos = new ByteArrayOutputStream();
//ObjectOutputStream oos = new ObjectOutputStream(baos);
//if (object instanceof ServerRequest) {
//  ServerRequest request = (ServerRequest) object;
//
//  //Login - dodaj bledy
//  if (request.requestName.equals("login")) {
//      LoginData log = (LoginData) request.object;
//      if (log.id.equals(loginData.id) && log.password.equals(loginData.password)) {
//          response = new ServerResponse(request.requestName, user);
//          oos.writeObject(response);
//          System.out.println("Server: " + response);
//      } else {
//          response = new ServerResponse(request.requestName, null);
//          response.error = "Błędne dane";
//          oos.writeObject(response);
//      }
//
//  //Register - dodaj bledy
//  } else if (request.requestName.equals("register")) {
//      User u = (User) request.object;
//      response = new ServerResponse(request.requestName, loginData);
//      oos.writeObject(response);
//  }
//
//  //PackagesInfo - dodaj bledy
//  else if (request.requestName.equals("packagesInfo")) {
//      response = new ServerResponse(request.requestName, statuses);
//      oos.writeObject(response);
//  }
//
//  //OrderCourier - dodaj bledy
//  else if (request.requestName.equals("orderCourier")) {
//      response = new ServerResponse(request.requestName, null);
//      oos.writeObject(response);
//  }
//
//  dataOut = baos.toByteArray();
//  socket.send(new DatagramPacket(dataOut, dataOut.length, packet.getAddress(), packet.getPort()));
//}
//oos.close();
//baos.close();
//}
