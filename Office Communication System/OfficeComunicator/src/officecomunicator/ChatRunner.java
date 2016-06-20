/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package officecomunicator;

import java.awt.Color;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.Date;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLDocument;

/**
 *
 * @author TOSHIBA
 */
public class ChatRunner implements Runnable {

    ObjectInputStream is;
    public OfficeFrame frS;
    public int i = 0;
    public int addr;
    public ObjectOutputStream os;
    SocketFile sockf;
    public mgs mg;
    MyTicTocLan mtl;

    public ChatRunner(ObjectOutputStream ost, ObjectInputStream ist, OfficeFrame frSt, int num) {
        os = ost;
        is = ist;
        frS = frSt;
        addr = num;
    }

    @Override
    public void run() {
        while (true) {
            //try {
            try {
                String m = null;

                m = "" + is.readObject();
                if ("YES".equals(m)) {
                    m = "" + is.readObject();
                    frS.ts.friendName[frS.ts.getCancle() - 1] = m;
                    designer(m + " Is accepted your request.", "Green");

                    if (frS.dontSave == 0) {
                        if (JOptionPane.showConfirmDialog(null, "Save " + m + "'s Account?") < 1) {
                            os.writeObject("SendIP");
                            frS.savedFriendFile();
                            frS.savedName.write(m + "\n");
                            frS.closeSavedFriendFile();
                            frS.openAndReadNames();
                            frS.SavedListManeger();
                        }
                    }
                    frS.ListManeger();
                } else if ("SendIP".equals(m)) {
                    String s = "" + InetAddress.getLocalHost();
                    int vv = s.lastIndexOf("/");
                    os.writeObject("" + s.substring(vv + 1));
                } else if ("NO".equals(m)) {
                    m = "" + is.readObject();
                    frS.ts.friendName[frS.ts.getCancle() - 1] = m;
                    designer(m + " Is cancled your request.", "Red");
                    frS.ListManeger();
                } else if ("dfound".equals(m)) {
                    os.writeObject(frS.name);
                    frS.ts.deleteFriend();
                } else if (frS.name.equals(m)) {
                    os.writeObject("dfound");
                    frS.setDeleteFriend("" + is.readObject());
                    frS.ts.deleteMe();
                } else if ("Send".equals(m)) {
                    try {
                        File f = new File(frS.getFilePath());
                        frS.ts.os[addr].writeObject("" + frS.getFileName());
                        frS.ts.os[addr].writeObject("" + f.length());
                        //sf = new ServerFile(frS.getFilePath(), frS.ts.server, frS.ts.socket);
                        frS.ts.busy[addr] = 3;
                        frS.ts.sf[addr].sendFile(frS.getFilePath(), frS.fsf);
                        //new Thread(frS.ts.sf[addr]).start();
                        frS.ts.sf[addr].run();
                        frS.ts.busy[addr] = 4;
                    } catch (Exception ex) {
                    }
                } else if ("Rcv".equals(m)) {
                    os.writeObject("Send");
                    String name = "" + is.readObject();
                    int size = Integer.parseInt("" + is.readObject());
                    frS.ts.busy[addr] = 2;
                    frS.ts.sockf[addr].receiveFile(size, name);
                    //new Thread(frS.ts.sockf[addr]).start();
                    frS.ts.sockf[addr].run();
                    frS.ts.busy[addr] = 4;
                    frS.designer("You have just received a file from " + frS.ts.friendName[addr], "Green");
                } else if ("Remove".equals(m)) {
                    frS.setDeleteFriend(frS.ts.friendName[addr]);
                    frS.ts.deleteMe();
                } else if ("View".equals(m)) {
                    ProfileViewer pv = new ProfileViewer();

                    pv.nameLebel.setText("" + is.readObject());
                    pv.emailLebel.setText("" + is.readObject());
                    pv.birthLebel.setText("" + is.readObject());
                    pv.studyLebel.setText("" + is.readObject());
                    pv.workLebel.setText("" + is.readObject());
                    pv.aboutLebel.setText("" + is.readObject());
                    pv.setVisible(true);
                    String name = "" + is.readObject();
                    if (!"none".equals(name)) {
                        int size = Integer.parseInt("" + is.readObject());
                        //ProImReceiver pir = new ProImReceiver(frS.tsp.socket, frS, pv);
                        frS.ts.busy[addr] = 1;
                        frS.ts.pir[addr].receiveFile(size, name, pv);
                        //new Thread(frS.ts.pir[addr]).start();
                        frS.ts.pir[addr].run();
                        frS.ts.busy[addr] = 4;
                    } else {
                        pv.imlebel.setIcon(new ImageIcon(getClass().getResource("default.png")));
                    }

                } else if (m.startsWith("Profile")) {
                    if (m.endsWith(frS.name)) {
                        os.writeObject("View");
                        Scanner input = new Scanner(new File("C://OCS(Kotha)//" + frS.getLogInUser() + "//My Profile.doc"));

                        String asa = "" + input.nextLine();

                        os.writeObject(input.nextLine());
                        os.writeObject(input.nextLine());
                        os.writeObject(input.nextLine());
                        os.writeObject(input.nextLine());
                        os.writeObject(input.nextLine());

                        String about = "";
                        while (input.hasNext()) {
                            about += input.nextLine() + "\n";
                        }
                        os.writeObject(about);
                        input.close();


                        try {
                            if (!"none".equals(asa)) {
                                File f = new File("C://OCS(Kotha)//" + frS.getLogInUser() + "//My Picture//" + asa);
                                frS.ts.os[addr].writeObject("" + asa);
                                frS.ts.os[addr].writeObject("" + f.length());
                                //sf = new ServerFile(frS.getFilePath(), frS.ts.server, frS.ts.socket);
                                //ProImSender pis = new ProImSender(f.getPath(), frS.ts.server, frS.ts.socket);
                                frS.ts.busy[addr] = 1;
                                frS.ts.pis[addr].sendFile(f.getPath());
                                //new Thread(frS.ts.pis[addr]).start();
                                frS.ts.pis[addr].run();
                                frS.ts.busy[addr] = 4;
                            } else {
                                os.writeObject("" + asa);
                            }
                        } catch (Exception ex) {
                        }
                    }
                } else if ("Req".equals(m)) {
                    m = "" + is.readObject();
                    if (JOptionPane.showConfirmDialog(null, m + " Wants to connect you as a friend\n Accept?") < 1) {
                        frS.ts.friendName[frS.ts.getCancle()] = m;
                        //frS.ListManeger();
                        os.writeObject("YES");

                        os.writeObject(frS.name);
                        if (JOptionPane.showConfirmDialog(null, "Save " + m + "'s Account?") < 1) {
                            //os.writeObject("SendIP");
                            frS.savedFriendFile();
                            frS.savedName.write(m + "\n");
                            frS.closeSavedFriendFile();
                            frS.openAndReadNames();
                            frS.SavedListManeger();
                        }
                        //os.writeObject(frS.name + " Is accepted your request.");
                        frS.ts.os[frS.ts.getCancle()] = os;
                        frS.ts.is[frS.ts.getCancle()] = is;
                        //frS.ts.addAfterRequest();
                        frS.ts.cancle++;
                        designer("You are now friend with " + m, "Green");
                        frS.ListManeger();
                        //frS.ts.cancle++;
                    } else {
                        frS.ts.friendName[frS.ts.getCancle()] = m;
                        os.writeObject("NO");
                        frS.setDeleteFriend(m);
                        frS.ts.os[frS.ts.getCancle()] = os;
                        frS.ts.is[frS.ts.getCancle()] = is;
                        frS.ts.cancle++;
                        os.writeObject(frS.name);

                        frS.ListManeger();
                        frS.ts.setMsg(frS.getDeleteFriend());
                        frS.ts.writer();
                    }
                } else if ("GameAPlay".equals(m)) {
                    if (JOptionPane.showConfirmDialog(null, frS.ts.friendName[addr] + "\ninvites you to play TicTacToe Lan with him.\nDo you want to play?") < 1) {
                        os.writeObject("GameAPlayYes");
                        frS.ts.gameText = 2;
                        mtl = new MyTicTocLan(frS);
                        frS.ts.setGamePlayPlayer(frS.ts.friendName[addr]);
                        mtl.vs.setText("Vs " + frS.ts.friendName[addr]);
                        try {
                            mtl.ind2.setIcon(new ImageIcon("C:/Program Files/Nasir CSE JnU/Office Communication System(Kotha)/Icon/ar.gif"));
                        } catch (Exception ex) {
                        }
                        mtl.buttonTextsetter = "O";
                        mtl.setVisible(true);
                    } else {
                        os.writeObject("GameAPlayNo");
                    }
                } else if ("GameAPlayYes".equals(m)) {
                    frS.ts.gameText = 1;
                    mtl = new MyTicTocLan(frS);
                    frS.ts.setGamePlayPlayer(frS.ts.friendName[addr]);
                    mtl.vs.setText("Vs " + frS.ts.friendName[addr]);
                    try {
                        mtl.ind1.setIcon(new ImageIcon("C:/Program Files/Nasir CSE JnU/Office Communication System(Kotha)/Icon/ar.gif"));
                    } catch (Exception ex) {
                    }
                    mtl.buttonTextsetter = "X";
                    mtl.setVisible(true);

                } else if ("GameAPlayNo".equals(m)) {
                    designer(frS.ts.friendName[addr] + " refused to Play TicTacToe Lan.", "Black");
                } else if (m.startsWith("Play")) {
                    if (mtl.isVisible()) {
                        String bv = m.substring(4);
                        if ("1".equals(bv)) {
                            if (frS.ts.gameText == 1) {
                                mtl.jButton1.setText("O");
                                mtl.jButton1.setBackground(Color.GREEN);
                            } else {
                                mtl.jButton1.setText("X");
                                mtl.jButton1.setBackground(Color.red);
                            }
                            mtl.reminder1 = 1;
                            mtl.setWinner();
                        } else if ("2".equals(bv)) {
                            if (frS.ts.gameText == 1) {
                                mtl.jButton2.setText("O");
                                mtl.jButton2.setBackground(Color.GREEN);
                            } else {
                                mtl.jButton2.setText("X");
                                mtl.jButton2.setBackground(Color.red);
                            }
                            mtl.reminder2 = 1;
                            mtl.setWinner();
                        } else if ("3".equals(bv)) {
                            if (frS.ts.gameText == 1) {
                                mtl.jButton3.setText("O");
                                mtl.jButton3.setBackground(Color.GREEN);
                            } else {
                                mtl.jButton3.setText("X");
                                mtl.jButton3.setBackground(Color.red);
                            }
                            mtl.reminder3 = 1;
                            mtl.setWinner();
                        } else if ("4".equals(bv)) {
                            if (frS.ts.gameText == 1) {
                                mtl.jButton4.setText("O");
                                mtl.jButton4.setBackground(Color.GREEN);
                            } else {
                                mtl.jButton4.setText("X");
                                mtl.jButton4.setBackground(Color.red);
                            }
                            mtl.reminder4 = 1;
                            mtl.setWinner();
                        } else if ("5".equals(bv)) {
                            if (frS.ts.gameText == 1) {
                                mtl.jButton5.setText("O");
                                mtl.jButton5.setBackground(Color.GREEN);
                            } else {
                                mtl.jButton5.setText("X");
                                mtl.jButton5.setBackground(Color.red);
                            }
                            mtl.reminder5 = 1;
                            mtl.setWinner();
                        } else if ("6".equals(bv)) {
                            if (frS.ts.gameText == 1) {
                                mtl.jButton6.setText("O");
                                mtl.jButton6.setBackground(Color.GREEN);
                            } else {
                                mtl.jButton6.setText("X");
                                mtl.jButton6.setBackground(Color.red);
                            }
                            mtl.reminder6 = 1;
                            mtl.setWinner();
                        } else if ("7".equals(bv)) {
                            if (frS.ts.gameText == 1) {
                                mtl.jButton7.setText("O");
                                mtl.jButton7.setBackground(Color.GREEN);
                            } else {
                                mtl.jButton7.setText("X");
                                mtl.jButton7.setBackground(Color.red);
                            }
                            mtl.reminder7 = 1;
                            mtl.setWinner();
                        } else if ("8".equals(bv)) {
                            if (frS.ts.gameText == 1) {
                                mtl.jButton8.setText("O");
                                mtl.jButton8.setBackground(Color.GREEN);
                            } else {
                                mtl.jButton8.setText("X");
                                mtl.jButton8.setBackground(Color.red);
                            }
                            mtl.reminder8 = 1;
                            mtl.setWinner();
                        } else if ("9".equals(bv)) {
                            if (frS.ts.gameText == 1) {
                                mtl.jButton9.setText("O");
                                mtl.jButton9.setBackground(Color.GREEN);
                            } else {
                                mtl.jButton9.setText("X");
                                mtl.jButton9.setBackground(Color.red);
                            }
                            mtl.reminder9 = 1;
                            mtl.setWinner();
                        } else if ("Again".equals(bv)) {
                            mtl.playA();
                        }
                        mtl.myTurn = 0;
                    } else {
                        os.writeObject("NotPlaying");
                    }
                } else if ("NotPlaying".equals(m)) {
                    mtl.setVisible(false);
                    designer(frS.ts.friendName[addr] + " is quit from Game.", "Black");
                } else if (m.startsWith("CCC")) {
                    if (frS.alert.isSelected()) {
                        try {
                            mg.setVisible(false);
                        } catch (Exception ex) {
                        }
                        mg = new mgs();
                        mg.setVisible(true);
                        designer(m.substring(3), "Black");
                    } else {
                        designer(m.substring(3), "Black");
                    }
                }

                /*} catch (Exception ex) {
                //frS.setDis(0);
                frS.designer("Problem!!!! to receive message from student. " + ex, "red");
                //frS.t2.stop();
                return;
                }*/
                frS.repaint();

            } catch (Exception ex) {
                //frS.setDis(0);
                //frS.designer("Problem!!!! to receive message from student. 1" + ex, "red");
                frS.setDeleteFriend(frS.ts.friendName[addr]);
                frS.setRemoveFriendGroup(frS.getDeleteFriend());
                frS.ts.removeFriendFromGroup();
                frS.ts.deleteMe();
                //frS.t1.stop();
                return;
            }
        }
    }

    public void designer(String line, String col) {
        try {
            frS.text_panel_html_kit.insertHTML((HTMLDocument) frS.output.getDocument(), frS.output.getDocument().getLength(), "<font size=8 color=" + col + ">" + line + "</font><font size=8 color=" + col + ">" + "</font>", 0, 0, null);
            //frS.sR.recode.setText(frS.sR.recode.getText() + line + "\n");
        } catch (Exception ex) {
        }
    }
}
