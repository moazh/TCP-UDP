import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by EngMoazh on 4/11/17.
 */
public class TCP_Client {

    private static Socket clientSocket;
    private static DataOutputStream outToServer;
    private static String fileOutput = "textFileReceivedFromServer.txt";
    private static String command;
    private static String ipAddress;
    private static int portNumber;

    public static void main(String argv[]) throws Exception {

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

            try {

                clientSocket = new Socket(ipAddress, portNumber);
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(command + '\n');

            } catch (Exception e) {

                System.out.println("Some thing wrong happened, Please restart the server and client");
                return;
            }

            if (!command.equalsIgnoreCase("exit") && !command.equalsIgnoreCase("file")) {

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String response;
                response = inFromServer.readLine();
                System.out.println("RESPONSE: " + response + '\n');

            } else if (command.equalsIgnoreCase("file")) {

                long time1 = System.nanoTime();

                byte[] aByte = new byte[1];
                int bytesRead;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = clientSocket.getInputStream();
                FileOutputStream fos = new FileOutputStream(fileOutput);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bytesRead = is.read(aByte, 0, aByte.length);

                do {
                    baos.write(aByte);
                    bytesRead = is.read(aByte);
                } while (bytesRead != -1);

                bos.write(baos.toByteArray());
                bos.flush();
                bos.close();
                System.out.println("Output File Name: " + fileOutput);

                long time2 = System.nanoTime();
                long timeTaken = time2 - time1;
                System.out.println("Time taken for receiving and saving the file is: " + timeTaken + " ns");

            } else {
                reader2.close();
                isRunning = false;
            }

            clientSocket.close();
        }

    }

    public static String md5OfFileToString(File file) {
        try {
            InputStream fin = new FileInputStream(file);
            java.security.MessageDigest md5er = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int read;
            do {
                read = fin.read(buffer);
                if (read > 0) {
                    md5er.update(buffer, 0, read);
                }
            } while (read != -1);
            fin.close();
            byte[] digest = md5er.digest();
            if (digest == null) {
                return null;
            }
            String strDigest = "0x";
            for (int i = 0; i < digest.length; i++) {
                strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1).toUpperCase();
            }
            return strDigest;
        } catch (Exception e) {
            return null;
        }
    }
}
