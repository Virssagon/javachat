import java.io.IOException;

/**
 * Created by Philipp on 29.08.2017.
 */
public class InputThread implements Runnable {
    Network network;
    Thread t;

    public InputThread(Network network) {
        this.network = network;

    }

    public void run() {
        try {
            while (true) {
                network.evaluateInputStream();
                Thread.sleep(150);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Starte InputThread!");
        t = new Thread(this);
        t.start();
    }
}