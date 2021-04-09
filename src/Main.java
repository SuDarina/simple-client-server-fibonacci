import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        if(args[0].equals("-s")){
            int port = Integer.parseInt(args[2]);
            try(ServerSocket server = new ServerSocket(port,  50, InetAddress.getByName(args[1]))) {
                while (true) {
                    Socket socket = server.accept();
                    new Thread(() -> {
                        try (
                                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        ){
                            while(true) {
                                String request = reader.readLine();
                                if(request.equals("quit")){
                                    reader.close();
                                    writer.close();
                                    socket.close();
                                    return;
                                }
                                writer.write(String.valueOf(findFibonacci(Integer.parseInt(request))));
                                writer.newLine();
                                writer.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        } if (args[0].equals("-c")){
            String host = args[1];
            int port = Integer.parseInt(args[2]);
            Scanner scanner = new Scanner(System.in);
            try(Socket client_socket = new Socket(host, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream()));
            ) {
                System.out.println("connected to server");
                while (true) {
                    int reloop = 0;
                    String s = "";
                    do {
                        try {
                            s = scanner.nextLine();
                            if (s.equals("")) {
                                writer.write("quit");
                                writer.newLine();
                                writer.flush();
                                client_socket.close();
                                System.exit(0);
                            }
                            Integer.parseInt(s);
                            reloop++;
                        } catch (Exception e) {
                            System.out.println("Incorrect input, please try again!");
                        }
                    }
                    while (reloop == 0);
                    writer.write(s);
                    writer.newLine();
                    writer.flush();
                    String answer = reader.readLine();
                    System.out.println(answer);
                }
            } catch (Exception e){
                System.out.println("oops... something goes wrong, please wait");
                e.printStackTrace();
            }

        }

    }
    private static int findFibonacci(int num) {
        int n0 = 1;
        int n1 = 1;
        int n2 = -1;
        if(num < 3){
            return 1;
        }
        for(int i = 3; i <= num; i++){
            n2 = n1 + n0;
            n0 = n1;
            n1 = n2;
        }
        return n2;
    }
}
