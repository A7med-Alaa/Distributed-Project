import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Client
 */
public class Client {

  private String request = "";
  private String response = "";
  private Socket socket = null;
  private BufferedReader user_input = null;
  private DataInputStream in = null;
  private DataOutputStream out = null;

  public Client(String ip, int port) {
    System.out.println("[Client] Available methods: GET, PUT and DELETE.");
    try {
      socket = new Socket(ip, port);
      user_input = new BufferedReader(new InputStreamReader(System.in));
      out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
      in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

    } catch (IOException e) {
      System.out.println(e);
      System.exit(1);
    }
  }

  public void connect() {
    while (!request.equals("exit")) {
      try {
        System.out.print("[Client] Enter your request: ");
        request = user_input.readLine();
        if (request.equals("done")) {
          System.out.print("\n");
          out.writeUTF(request);
          out.flush();
          response = in.readUTF();
          cleanup();
          return;
        }
        System.out.print("\n");
        out.writeUTF(request);
        out.flush();
        response = in.readUTF();
        System.out.println(response);
      } catch (IOException e) {
        System.out.println(e);
        System.exit(1);
      }
    }
    cleanup();
  }

  private void cleanup() {
    try {
      socket.close();
      user_input.close();
      out.close();
      in.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public static void main(String[] args) {
    Client c = new Client("127.0.0.1", 5000);
    c.connect();
  }
}
