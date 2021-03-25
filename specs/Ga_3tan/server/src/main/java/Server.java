import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = null;
        PrintWriter writer = null;
        ServerSocket s = null;
        Socket clientSocket = null;

        try {
            s = new ServerSocket(8080);
            System.out.println("waiting for connexion");
            clientSocket = s.accept();
            System.out.println("connected to " + clientSocket.getInetAddress());

            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(clientSocket.getOutputStream(), false, StandardCharsets.UTF_8);

            String[] message = reader.readLine().split(" ", 3);

            while (!message[0].equals("STOP")) {
                if (message[0].equals("HELP")) {
                    writer.println("Supported operation : ADD, SUB, MULT");
                } else {
                    int result;
                    try {
                        if (message.length == 3) {
                            result = switch (message[0]) {
                                case "ADD" -> Integer.parseInt(message[1]) + Integer.parseInt(message[2]);
                                case "MULT" -> Integer.parseInt(message[1]) * Integer.parseInt(message[2]);
                                case "SUB" -> Integer.parseInt(message[1]) - Integer.parseInt(message[2]);
                                default -> throw new IllegalArgumentException();
                            };
                        } else {
                            throw new IllegalArgumentException();
                        }
                        writer.println("RESULT " + result);
                    } catch (Exception e) {
                        writer.println("ERROR");
                    }
                }
                writer.flush();
                message = reader.readLine().split(" ", 3);
            }
            System.out.println("disconnected from " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            assert reader != null;
            reader.close();
            assert writer != null;
            writer.close();
            clientSocket.close();
            s.close();
        }

    }
}
