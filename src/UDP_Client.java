import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Created by EngMoazh on 4/11/17.
 */
public class UDP_Client {


    private static String fileOutput = "textFileReceivedFromServer.txt";
    private static String command;
    private static String ipAddress;
    private static int portNumber;

    public static void main(String args[]) throws Exception {


        Scanner reader = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("Please enter the IP ADDRESS");
        ipAddress = reader.nextLine();

        System.out.println("Please enter the PORT NUMBER");
        portNumber = reader.nextInt();


        while (isRunning) {

            System.out.println("Please choose one of the following actions:\n - Ping\n - Timestamp\n - File\n - Exit");
            Scanner reader2 = new Scanner(System.in);
            command = reader2.nextLine();

            byte[] request;
            byte[] receiveData;

            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(ipAddress);

            request = command.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(request, request.length, IPAddress, portNumber);
            clientSocket.send(sendPacket);

            if (!command.equalsIgnoreCase("file")) {
                if (command.equalsIgnoreCase("ping")) {
                    receiveData = new byte[command.getBytes().length];

                } else if (command.equalsIgnoreCase("timestamp")) {
                    receiveData = new byte[19];
                } else if (command.equalsIgnoreCase("exit")) {

                    reader2.close();
                    isRunning = false;
                    return;
                } else {
                    receiveData = new byte[29];
                }

                DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(response);
                String response2 = new String(response.getData());
                System.out.println("RESPONSE: " + response2 + '\n');

            } else if (command.equalsIgnoreCase("file")) {

                long time1 = System.nanoTime();

                receiveData = new byte[3204];
                DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);
                DataInputStream din = new DataInputStream(new ByteArrayInputStream(response.getData(),
                        response.getOffset(), response.getLength()));

                FileOutputStream fos = new FileOutputStream(fileOutput);
                clientSocket.receive(response);

                int bytesRead;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bytesRead = din.read(receiveData, 0, receiveData.length);

                do {
                    baos.write(receiveData);
                    bytesRead = din.read(receiveData);
                } while (bytesRead != -1);

                bos.write(baos.toByteArray());
                bos.flush();
                bos.close();
                System.out.println("Output File Name: " + fileOutput);

                long time2 = System.nanoTime();
                long timeTaken = time2 - time1;
                System.out.println("Time taken for receiving and saving the file is: " + timeTaken + " ns");
            }

            clientSocket.close();
        }
    }
}
