import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;

public class Connection extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket clientSocket;

	private static HashMap<Integer, Place> hashPlaces = new HashMap<Integer, Place>();

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
			ObjectInputStream ois = new ObjectInputStream(in);

			Request req = (Request) ois.readObject();

			out.writeUTF(processRequest(req)); // envia a mensagem (resposta) ao cliente
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("CLASS:" + e.getMessage());
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				/* close failed */
			}
		}
	}

	private String processRequest(Request req) {
		String returnValue = null;
		switch (req.getType()) {
		case "new":
			Integer objectID = System.identityHashCode(req.getPlace());
			hashPlaces.put(objectID, req.getPlace());
			returnValue = objectID.toString();
			break;
		case "invoke":
			Place p = hashPlaces.get(req.getObjectID());
			if (p == null) {
				returnValue = "invalid objectid";
				break;
			}
			try {
				returnValue = (String) Place.class.getMethod(req.getMethod()).invoke(hashPlaces.get(req.getObjectID()));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				returnValue = "invalid method";
			}
			
			break;
		default:
			returnValue = "invalid type";
		}
		return returnValue;
	}
}
