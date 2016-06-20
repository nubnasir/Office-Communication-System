/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package officecomunicator;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author TOSHIBA
 */
public class ProImReceiver implements Runnable {

    /**
     * @param args the command line arguments
     */
    Socket sock;
    String fileName;
    OfficeFrame frS;
    ProfileViewer pv;
    int size;

    public ProImReceiver(Socket s, OfficeFrame of) {
        sock = s;
        frS = of;
    }

    @Override
    public void run() {
        try {
            byte[] mybytearray = new byte[size];
            InputStream is = sock.getInputStream();
            FileOutputStream fos = new FileOutputStream("C://OCS(Kotha)//" + frS.getLogInUser() + "//Garbage//" + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            pv.prog.setValue(0);
            pv.prog.setMaximum(size);
            for (int i = 0; i < mybytearray.length; i++) {
                byte my[] = new byte[1];
                my[0] = mybytearray[i];
                int bytesRead = is.read(my);
                bos.write(my);
                pv.prog.setValue(i + 1);
                int a = ((i + 1) * 100) / size;
                pv.prog.setString("Loading Image: " + a + "%");
            }
            bos.close();
            pv.imlebel.setIcon(new ImageIcon("C://OCS(Kotha)//" + frS.getLogInUser() + "//Garbage//" + fileName));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error" + ex);
        }

    }

    public void receiveFile(int s, String f, ProfileViewer p) {
        // TODO code application logic here
        fileName = f;
        pv = p;
        size = s;
    }
}
