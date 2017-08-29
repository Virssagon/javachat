import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Philipp on 29.08.2017.
 */
public class main {
    static Network network = new Network();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!network.isAccepted() && network.getisServer()) {
            network.listenForServerRequest();
        }
        InputThread input = new InputThread(network);
        input.start();
        OutputThread output = new OutputThread(network);
        output.start();
        while(true){
            try{
                Thread.sleep(10000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
