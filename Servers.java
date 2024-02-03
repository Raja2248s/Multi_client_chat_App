package New_chat_App;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Servers {
    private ServerSocket server;
    private List<ClientHandler> clients = new ArrayList<>();

    public Servers(int port) throws IOException {
        server = new ServerSocket(port);
        System.out.println("Server is ready to accept connections on port " + port);
   
        while (true) { 
            Socket socket = server.accept();
            System.out.println("New client connected");

            ClientHandler clientHandler = new ClientHandler(socket);
            clients.add(clientHandler);

            Thread clientThread = new Thread(clientHandler);
            clientThread.start();
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 7778; // You can change the port if needed
        System.out.println("This is server... Going to start the server");
        new Servers(port);
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader br;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        @Override
        public void run() {
            System.out.println("ClientHandler started for client: " + socket);
            try { 
                while (true) { 
                    String msg = br.readLine();
                    if (msg == null || msg.equals("exit")) {
                        socket.close();
                        clients.remove(this);
                        System.out.println("Client terminated the chat: " + socket);
                        break;
                    }

                    System.out.println("Received message from client: " + socket + ": " + msg);

                    // Broadcast the message to all connected clients
                    for (ClientHandler client : clients) {
                        if (client != this) {
                            client.sendMessage(msg);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
            out.flush();
        }
    }
}
