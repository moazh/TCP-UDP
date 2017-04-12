import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by EngMoazh on 4/11/17.
 */
public class UDP_Server {

    public static void main(String args[]) throws Exception {

        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] request = new byte[1024];
        byte[] response;
        String fileToSend = "text.txt";
        String strResponse;

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(request, request.length);
            serverSocket.receive(receivePacket);
            String request2 = new String(receivePacket.getData());
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            System.out.println(request2);

            request2 = request2.toLowerCase();
            if (request2.startsWith("ping")) {
                strResponse = "Pong";
                response = strResponse.getBytes();

            } else if (request2.startsWith("timestamp")) {
                strResponse = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(Calendar.getInstance().getTime());
                response = strResponse.getBytes();

            } else if (request2.startsWith("file")) {

                File myFile = new File(fileToSend);
                response = new byte[(int) myFile.length()];

                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(response, 0, response.length);


                int i = 0;
                while (fis.available() != 0) {
                    response[i] = (byte) fis.read();
                    i++;
                }
                fis.close();

            } else {

                strResponse = "this command is not supported";
                response = strResponse.getBytes();
            }

            DatagramPacket sendPacket = new DatagramPacket(response, response.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}
