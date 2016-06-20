/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package officecomunicator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author TOSHIBA
 */
public class CopyAndpaste implements Runnable {

    File cf;
    File pf;
    EditMyProfile ed;

    public CopyAndpaste(File c, File p, EditMyProfile e) {
        cf = c;
        pf = p;
        ed = e;
    }

    @Override
    public void run() {
        copypaste();
    }

    public void copypaste() {
        InputStream is = null;
        OutputStream os = null;

        ed.prog.setValue(0);
        ed.prog.setMaximum((int) cf.length());
        try {
            is = new FileInputStream(cf);
            os = new FileOutputStream(pf);
            byte[] buf = new byte[(int) cf.length()];
            for (int i = 0; i < buf.length; i++) {
                byte my[] = new byte[1];
                my[0] = buf[i];
                int l = is.read(my);
                os.write(my);
                ed.prog.setValue(i);
                int a = ((i + 1) * 100) / ed.prog.getMaximum();
                ed.prog.setString("Loading Image " + a +"%");
            }
            is.close();
            os.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error to load image.\n" + ex);
        }
    }
}
