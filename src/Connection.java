import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

public class Connection extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket clientSocket;

	private static HashMap<Integer, Place> objetos = new HashMap<Integer, Place>();

	public Connection(Socket aClientSocket) {
		try {
			// inicializa variáveis
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());

			this.start(); // executa o método run numa thread separada

		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}

	public void run() {
		try {

			ObjectInputStream ois = new ObjectInputStream(in);// lê os dados do objeto
			Request request = (Request) ois.readObject();
			if (request.getType().equals("new")) {
				String objectID = String.valueOf(System.identityHashCode(request.getPlace()));
				objetos.put(Integer.parseInt(objectID), request.getPlace());
				out.writeUTF(objectID);
			} else if (request.getType().equals("invoke")) {
				if (objetos.containsKey(request.getObjectID())) {
					if (request.getMethod().equals("getLocality")) {
						out.writeUTF(objetos.get(request.getObjectID()).getLocality());
					} else if (request.getMethod().equals("getPostalCode")) {
						out.writeUTF(objetos.get(request.getObjectID()).getPostalCode());
					} else {
						out.writeUTF("invalid method");
					}
				} else {
					out.writeUTF("invalid objectid");
				}
			} else {
				out.writeUTF("invalid type");
			}
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				/* close failed */
			}
		}
	}
}