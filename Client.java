package New_chat_App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;


public class Client extends JFrame  {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
   private JTextArea messageArea = new JTextArea();
   private JTextField messageInput = new JTextField();
   private Font font = new Font("",Font.PLAIN,20);
   private Font header_font = new Font("Arial",Font.BOLD,35);
   Font  f4 = new Font(Font.SANS_SERIF,  Font.BOLD, 35);
   Font  f2 = new Font(Font.SANS_SERIF,  Font.PLAIN, 20);

    public Client(String serverAddress, int serverPort) {
        try {
            System.out.println("Sending Request to server..");
            socket = new Socket(serverAddress, serverPort);
            System.out.println("Connection Done");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

              createGUI();
              handleEvents();
              
            startReading();
            //startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
      private void handleEvents(){

        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e){
    
            }
            @Override
            public void keyPressed(KeyEvent e) {
            } 
             
            @Override
            public void keyReleased(KeyEvent e) {
                 if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    String contentToSend =  messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                 }
            }
        });
      }

    public void createGUI(){

        this.setTitle("Chat Application");
        this.setSize(500 , 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(f4);
        messageArea.setFont(f2);
        messageInput.setFont(font);

       Color inputBackGroundColor = new Color(204, 203, 184);
       Color areaBackGroundColor = new Color(228,225,225);
       Color headingBackgroundColor = new Color(186, 47, 130); // Red color (adjust as needed)
            

       heading.setBackground(headingBackgroundColor);
       heading.setOpaque(true);
        messageArea.setBackground(areaBackGroundColor);

        LineBorder inputBorder = new LineBorder(Color.BLACK, 2); // Adjust the color and thickness as needed

        messageInput.setBorder(inputBorder);
        messageInput.setBackground(inputBackGroundColor);
        messageInput.setPreferredSize(new Dimension(150, 50));
        ImageIcon originalIcon =  new ImageIcon("A:/Project/New_chat_App/chat (1).png");
        
        heading.setForeground(Color.white);
        JScrollPane jscrollpane = new JScrollPane(messageArea);
        jscrollpane.getViewport().setOpaque(false);
        heading.setIcon(originalIcon);
        heading.setHorizontalTextPosition(SwingConstants.RIGHT);
        heading.setHorizontalAlignment(SwingConstants.LEFT);
        heading.setVerticalTextPosition(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

       messageArea.setEditable(false);
       messageInput.setHorizontalAlignment(SwingConstants.LEFT);

       this.setLayout(new BorderLayout());

       this.add(heading , BorderLayout.NORTH);
       this.add(jscrollpane , BorderLayout.CENTER);
       this.add(messageInput , BorderLayout.SOUTH);


        this.setVisible(true);
    }

    // ...
public void startReading() {
    Runnable r1 = () -> {
        System.out.println("Reader Started");
        try {
            while (true) {
                String msg = br.readLine();
                if (msg.equals("exit")) {
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this, "Server Terminated the message");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                } else {
                    if (msg.startsWith("Server: ")) {
                        // Message from the server
                        messageArea.append(msg + "\n");
                    } else {
                        // Message from another client
                        messageArea.append("Other Client: " + msg + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
    new Thread(r1).start();
}
// ...

  
    public void startWriting() {
        System.out.println("Writer Started...");
        Runnable r2 = () -> {
            try {
                while (true) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                    out.println(content);
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) { 
        Scanner sc = new Scanner(System.in); 
        System.out.print("Enter Ip address : ");
        String  serverAddress = sc.next(); // Replace with the server's IP address or hostname
        int serverPort = 7778; // Make sure this matches the server's port
        System.out.println("This is Client...");
        new Client(serverAddress, serverPort);
    }
}
