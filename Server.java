import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Server
 */
public class Server {

  private Socket socket = null;
  private String request = "";
  private DataInputStream in = null;
  private DataOutputStream out = null;
  private ServerSocket serverSocket = null;

  public Server(int port) {
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Server Started Successfuly.");
      System.out.println("Waiting for a client.");
      socket = serverSocket.accept();
      System.out.println("Recieved a connection.");

      in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      out = new DataOutputStream(socket.getOutputStream());

    } catch (IOException e) {
      System.out.println(e);
      System.exit(1);
    }
  }

  private void listen() {
    while (!request.equals("done")) {
      try {
        request = in.readUTF();
        System.out.println("[Server] recieved a request: " + request);
      } catch (IOException i) {
        System.out.println(i);
      }

      if (request.contains("GET") || request.contains("PUT")
          || request.contains("DELETE")) {

        String status = "";

        File db = new File("data.txt");
        Scanner reader = null;
        try {
          reader = new Scanner(db);
        } catch (FileNotFoundException e) {
          System.out.println(e);
          System.exit(1);
        }
        try {

          String[] request_splits = request.split(" ");
          String method = request_splits[0];

          if (method.equals("GET")) {

            while (reader.hasNextLine()) {
              String db_line = reader.nextLine();
              if (request_splits.length != 3) {
                out.writeUTF("[Server] Missing arguments: GET [METHOD] [USERNAME]");
                continue;
              }
              String submethod = request_splits[1];
              String requested_val = request_splits[2];

              String[] row = db_line.split(":");
              String name = row[0];
              String salary = row[1];
              String position = row[2];

              if (name.equals(requested_val)) {
                if (submethod.equals("user")) {
                  out.writeUTF("[Server] User found in the Database.\n");
                  System.out.println("Request handled.");
                  status = "";
                  break;
                } else if (submethod.equals("salary")) {
                  out.writeUTF("[Server] " + name + " salary is: " + salary + "\n");
                  System.out.println("Request handled.");
                  status = "";
                  break;
                } else if (submethod.equals("position")) {
                  out.writeUTF("[Server] " + name + " position is: " + position + "\n");
                  System.out.println("Request handled.");
                  status = "";
                  break;
                }
              } else {
                status = "GET Failed";
                continue;
              }
            }
          } else if (method.equals("PUT")) {
            if (request_splits.length != 4) {
              out.writeUTF("[Server] Missing arguments: PUT [USERNAME] [SALARY] [POSITION]");
              continue;
            }
            String name = request_splits[1];
            String salary = request_splits[2];
            String position = request_splits[3];
            FileWriter writer = new FileWriter("data.txt", true);
            writer.write(name + ":" + salary + ":" + position + "\n");
            out.writeUTF("[Server] New user has been added Successfuly.");
            writer.close();

          } else if (method.equals("DELETE")) {
            String targetName = request_splits[1];
            File inputFile = new File("data.txt");
            File tempFile = new File("temp.txt");

            try {
              Scanner d_reader = new Scanner(inputFile);
              BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

              String currentLine = "";

              while (d_reader.hasNextLine()) {
                currentLine = d_reader.nextLine();
                if (currentLine.contains(targetName)) {
                  continue; // skip lines with "john"
                }
                writer.write(currentLine);
                writer.newLine();
              }

              d_reader.close();
              writer.close();
            } catch (IOException e) {
              e.printStackTrace();
            }

            // Replace the original file with the updated one
            if (inputFile.delete()) {
              tempFile.renameTo(inputFile);
              out.writeUTF("[Server] " + targetName + " has been deleted Successfuly.");
            } else {
              System.out.println("Failed to update the file.");
              out.writeUTF("[Server] Failed to delete " + targetName);
              continue;
            }

          } else if (method.equals("exit")) {
            continue;
          }

          if (status.equals("GET Failed")) {
            out.writeUTF("[Server] Couldn't find user in the Database.\n");
          }
        } catch (IOException e) {
          System.out.println(e);
        }
        reader.close();
      } else {
        try {
          if (request.equals("exit")) {
            out.writeUTF("[Server] Disconnected");
            out.flush();
            cleanup();
            return;
          }
          System.out.println("Use a valid method: GET, PUT or DELETE");
          out.writeUTF("Use a valid method.\n");
          out.flush();
        } catch (IOException e) {
          System.out.println(e);
        }
      }
    }

    cleanup();

  }

  private void cleanup() {
    try {
      socket.close();
      serverSocket.close();
      in.close();
      out.close();
    } catch (IOException e) {
      System.out.println(e);
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    Server s = new Server(5000);
    s.listen();
  }
}
