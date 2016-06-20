/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package officecomunicator;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author TOSHIBA
 */
public class ThreadSocket implements Runnable {

    public OfficeFrame frS;
    ServerSocket server;
    Socket socket;
    ObjectOutputStream os;
    ObjectInputStream is;
    InputStream isf;
    FileOutputStream fos;
    BufferedOutputStream bos;
    ThreadServer ts;
    String Ipaddress = null;

    public int getNumFile() {
        return numFile;
    }

    public void setNumFile(int numFile) {
        this.numFile = numFile;
    }
    public int numFile = 0;
    public String file[] = new String[5];

    public int getComplete() {
        return Complete;
    }

    public void setComplete(int Complete) {
        this.Complete = Complete;
    }
    public int Complete = 0;

    public ThreadSocket() {
    }

    public ThreadSocket(OfficeFrame fn, String ip) {
        frS = fn;
        Ipaddress = ip;
    }

    @Override
    public void run() {
        searchConnection();
    }

    public void searchConnection() {
        while (true) {
            if (Ipaddress == null) {
                try {
                    frS.ts.setIpaddress(Ipaddress);

                } catch (Exception ex) {
                    break;
                }
            }
            //frS.designer("Connecting.....\n", "Black");
            try {

                socket = new Socket(Ipaddress, 12345);

                //frS.output.setText(frS.output.getText() + "Connected to : " + socket.getInetAddress().getHostName() + "\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Incorrect IP Address.");
                //frS.designer("Problem!!!! Searching failed..... " + ex, "Black");
                //terminateConnection();
                //frS.setConnectAgain(1);
                //frS.th.stop();
                break;
            }

            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                is = new ObjectInputStream(socket.getInputStream());
                //os.writeObject("hello welcome to the server\n");
                //os.writeObject("A");
                //frS.setCheckC(0);
                os.writeObject("Req");
                os.writeObject(frS.name);
                String s = "" + InetAddress.getLocalHost();
                int vv = s.lastIndexOf("/");
                os.writeObject("" + s.substring(vv + 1));
                frS.ts.os[frS.ts.getCancle()] = os;
                frS.ts.is[frS.ts.getCancle()] = is;
                frS.ts.addAfterRequest();
                frS.designer("Request was sent.", "Black");
            } catch (Exception ex) {
                frS.designer("Problem!!!! in build stream. unable to connect with the server" + ex, "Blue");
                break;
            }
            break;
        }
    }

    public void terminateConnection() {
        try {
            server.close();
            socket.close();
            os.close();
            is.close();
        } catch (Exception ex) {
            frS.output.setText(frS.output.getText() + "Problem!!!! to disconnect(TerminateConnection). " + ex + "\n");
        }
    }
}
