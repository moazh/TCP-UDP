import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by EngMoazh on 4/11/17.
 */
public class TCP_Server {

    public static void main(String argv[]) throws Exception {
        String request;
        String fileToSend = "text.txt";
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while (true) {

            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            request = inFromClient.readLine();

            if (request.equalsIgnoreCase("ping")) {
                outToClient.writeBytes("Pong" + '\n');
            } else if (request.equalsIgnoreCase("timestamp")) {
                outToClient.writeBytes(new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(Calendar.getInstance().getTime()) + '\n');

            } else if (request.equalsIgnoreCase("file")) {

                File myFile = new File(fileToSend);
                byte[] mybytearray = new byte[(int) myFile.length()];

                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);

                bis.read(mybytearray, 0, mybytearray.length);
                outToClient.write(mybytearray, 0, mybytearray.length);
                outToClient.flush();
                outToClient.close();
                connectionSocket.close();

            } else {
                outToClient.writeBytes("this command is not supported" + '\n');
            }

        }
    }
}
