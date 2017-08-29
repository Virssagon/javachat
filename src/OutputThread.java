import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Philipp on 29.08.2017.
 */
public class OutputThread implements Runnable {

    Network network;
    Thread t;

    public OutputThread(Network network) {
        this.network = network;
    }

    public void run() {
        String s;
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                s = scanner.nextLine();
                network.dos.writeUTF(s);//Wenn ein Spielzug geschrieben wird, pausiere diesen thread, sobald er geschrieben wurde, weiter
                network.dos.flush();
                //System.out.println("print");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void start() {
        System.out.println("Starte OutputThread!");
        t = new Thread(this);
        t.start();
    }

}
