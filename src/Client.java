import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        CLI cli = new CLI();
        System.out.println("Клиент запустился.");

        new Thread() {
            public void run() {
                try {
                    cli.readMSG();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    cli.sendMSG();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

class CLI {
    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out= null;
    BufferedReader console = null;
    String userMSG, serverMSG;

    public CLI() throws IOException {
        socket = new Socket("localhost",2222);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);
        console = new BufferedReader(new InputStreamReader(System.in));
    }

    void sendMSG() throws IOException {
        while (true) {
            if ((userMSG = console.readLine()) != null) {
                out.println(userMSG);
                if (userMSG.equalsIgnoreCase("close") || userMSG.equalsIgnoreCase("exit")) break;
            }
        }
    }

    void readMSG() throws IOException {
        while (true) {
            if ((serverMSG = in.readLine()) != null){
                System.out.println(serverMSG);
            }
        }
    }

    void close() throws IOException {
        out.close();
        in.close();
        console.close();
        socket.close();
    }
}
