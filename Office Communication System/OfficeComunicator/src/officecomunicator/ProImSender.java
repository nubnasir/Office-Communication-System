/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package officecomunicator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author TOSHIBA
 */
public class ProImSender implements Runnable {

    /**
     * @param args the command line arguments
     */
    public String fileName;
    ServerSocket servsock;
    Socket sock;
    OutputStream os = null;

    public ProImSender(Socket s) {
        sock = s;
    }

    @Override
    public void run() {
        File myFile = new File(fileName);
        try {
            byte[] mybytearray = new byte[(int) myFile.length()];
            //System.out.println("" + mybytearray.length);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
            bis.read(mybytearray, 0, mybytearray.length);
            for (int i = 0; i < mybytearray.length; i++) {
                byte my[] = new byte[1];
                my[0] = mybytearray[i];
                bis.read(my);
                os = sock.getOutputStream();
                os.write(my);
            }
            //os.flush();
            bis.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error2: " + ex);
        }

    }

    public void sendFile(String msg) {
        // TODO code application logic here
        fileName = msg;
    }
}
