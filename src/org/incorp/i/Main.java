package org.incorp.i;

//import org.omg.CORBA.DataOutputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        Socket clientSocket = null;
        Scanner scanner = new Scanner(System.in);
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("Server START");
            clientSocket = serverSocket.accept();
            System.out.println("User onlyn: " + clientSocket.getRemoteSocketAddress());
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            Thread threadReader = new Thread(() -> {
                try {
                    while (true) {

                        outputStream.writeUTF(scanner.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threadReader.setDaemon(true);
            threadReader.start();
            while (true) {
                String str = inputStream.readUTF();
                if (str.equals("/close")) {
                    System.out.println("User offlyne");
                    outputStream.writeUTF("/close");
                    break;
                } else {
                    System.out.println("User " + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
