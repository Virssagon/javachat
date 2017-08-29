import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Philipp on 14.08.2017.
 */
@SuppressWarnings("Duplicates")
public class Network {

    private String ip = "localhost";
    private String player1 = "Saru";
    private String player2 = "tjoboo";
    private int port = 42000;
    public Socket socket;
    public DataOutputStream dos;
    public DataInputStream dis;
    private ServerSocket serverSocket;

    private boolean accepted = false;
    private boolean yourTurn;
    private boolean isServer = true;


    public Network() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bitte gib deine IP ein: ");//valid ip check
        ip = scanner.nextLine();
        //System.out.println("Bitte gib deinen Spielernamen ein: ");
        //name = scanner.nextLine();
        if (!connect()) initializeServer();
    }

    public void evaluateInputStream() throws IOException {
        String s;
        try {
            if (isAccepted() && (dis.available() != 0)) {
                Thread.sleep(100);
                if(dis.available() == 4){
                    Integer tmp = dis.readInt(); // hier muss der zug passieren, danach return um raus zu kommen
                    return;
                }
                s = dis.readUTF();
                //System.out.println("DIS_CHECK: " + s);
                if (Objects.equals((s.length() != 0 ? s.substring(0, 1).toLowerCase() : s), "!")) {
                    switch (s) {
                        case "!kick":
                            System.out.println("Du wurdest aus der Sitzung geworfen!");
                            break;
                        case "!userDisconnected":
                            if (getisServer()) {
                                System.out.println("Der Client hat die Verbindung getrennt!");
                            } else {
                                System.out.println("Der Server hat die Verbindung getrennt!");
                            }
                            break;
                        default:
                            System.out.println("Unbekannter Befehl!");
                            break;
                    }
                } else {
                    if (getisServer()) {
                        System.out.println(player2 + ": " + s);
                    } else {
                        System.out.println(player1 + ": " + s);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("MyErrorMSG: Fehler beim pausieren des Thread!");
            e.printStackTrace();
        }
    }


    public void listenForServerRequest() {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            accepted = true;
            System.out.println("Ein Client hat sich verbunden!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        try {
            socket = new Socket(ip, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            accepted = true;
        } catch (IOException e) {
            System.out.println("Verbindung zu Adresse: " + ip + ":" + port + " konnte nicht hergestellt werden. | Starte Server!");
            return false;
        }
        System.out.println("Verbindung zum Server erfolgreich hergestellt!");
        yourTurn = false;
        isServer = false;
        return true;
    }

    public void initializeServer() {
        try {
            serverSocket = new ServerSocket(port, 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        yourTurn = true;
    }

    public void disconnectPlayerFromServer(int i) {//Was soll passieren wenn der Gegner disconnected? automatisch nach neuem suchen? neues spiel? startbildschirm?
        try {
            serverSocket.close();
            switch (i) {
                case 1:
                    System.out.println("Spieler wurde gekickt!");
                    break;
                case 2:
                    System.out.println("Der Gegner hat die Verbindung getrennt!");
                    break;
            }
            initializeServer();
            System.out.println("Warte auf neuen Spieler... ");
            listenForServerRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isAccepted() {
        return accepted;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void swapTurn() {
        yourTurn = !yourTurn;
    }

    public boolean getisServer() {
        return isServer;
    }
}
