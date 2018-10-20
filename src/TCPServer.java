import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static void main(String args[]) {
		try {
			int serverPort = 7896;
			ServerSocket listenSocket = new ServerSocket(serverPort);
			while (true) {
				Socket clientSocket = listenSocket.accept(); // bloqueia à espera de uma conexão
				Connection c = new Connection(clientSocket); // processa o pedido
			}
		} catch (IOException e) {
			System.out.println("Listen :" + e.getMessage());
		}
	}
}