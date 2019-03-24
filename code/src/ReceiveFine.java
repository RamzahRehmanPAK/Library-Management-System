
/**
 * Created by Ramzah Rehman on 10/24/2016.
 */
import jdk.nashorn.internal.codegen.types.Type;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class ReceiveFine {
    static ImagePanel mainPanel;
    static JLabel labelmemberID;
    static JTextField memberID;

    static JLabel labelFee;
    static JTextField fee;

    static JLabel msg;
    static JButton receiveFine;

    public static void createPanel() {
        try {

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            BufferedImage image = ImageIO.read(new File("bar.jpg"));//returns BufferedImage

            BufferedImage tmpImg = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = (Graphics2D) tmpImg.getGraphics();

            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            //set the transparency level in range 0.0f - 1.0f

            g2d.drawImage(image, 0, 0, null);

            image = tmpImg;

            mainPanel = new ImagePanel();
            mainPanel.setBackground(image);


            GridBagLayout mainPanelLayout = new GridBagLayout();

            mainPanel.setLayout(mainPanelLayout);

            memberID=new JTextField();
            fee=new JTextField();

            Dimension d=new Dimension(150, 30);
            memberID.setPreferredSize(d);
            fee.setPreferredSize(d);


            receiveFine=new JButton("Receive fine");
            receiveFine.setPreferredSize(d);

            labelmemberID=new JLabel("Member ID");
            labelFee=new JLabel("Amount");
            msg=new JLabel();


            Color c=new Color(200, 10, 130);
            labelmemberID.setForeground(c);
            labelFee.setForeground(c);
            msg.setForeground(c);


            Font font= new Font("Harlow Solid Italic",Font.PLAIN,24);
            labelmemberID.setFont(font);
            labelFee.setFont(font);
            msg.setFont(new Font("Harlow Solid Italic",Font.PLAIN,18));


            GridBagConstraints gbc=new GridBagConstraints();

            gbc.gridx=2;
            gbc.gridy=0;

            gbc.insets=new Insets(0,20,0,0);
            mainPanel.add(receiveFine,gbc);

            gbc.gridx=0;
            gbc.gridy=1;
            gbc.insets=new Insets(0,0,0,10);
            mainPanel.add(labelmemberID,gbc);

            gbc.gridx=1;
            gbc.gridy=1;
            mainPanel.add(memberID,gbc);

            gbc.gridx=0;
            gbc.gridy=2;
            mainPanel.add(labelFee,gbc);

            gbc.gridx=1;
            gbc.gridy=2;
            mainPanel.add(fee,gbc);


            gbc.gridx=0;
            gbc.gridy=0;
            gbc.gridwidth=2;
            gbc.insets=new Insets(0,0,0,0);
            mainPanel.add(msg,gbc);

            registerComponent();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void showPanel(){

        memberID.setText("");
        fee.setText("");
        msg.setText("");

        msg.setVisible(false);
        Driver.mainFrame.setContentPane(mainPanel);
        Driver.mainFrame.setVisible(true);

    }
    public static void registerComponent() {

        ActionListener handler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!memberID.getText().isEmpty() && !fee.getText().isEmpty()){

                    String temp=memberID.getText();

                    for(int i=0;i<temp.length();i++){
                        if(temp.charAt(i)<'0' || temp.charAt(i)>'9'){

                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "ID should be an integer!");
                            memberID.setText("");
                            fee.setText("");
                            return;
                        }
                    }

                    temp=fee.getText();

                    for(int i=0;i<temp.length();i++){
                        if(temp.charAt(i)<'0' || temp.charAt(i)>'9'){

                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "Amount should contain digits!");
                            memberID.setText("");
                            fee.setText("");
                            return;
                        }
                    }

                    try{
                        CallableStatement statement=Driver.con.prepareCall("{ call librarySystem.receiveFine (?,?,?) }");
                        statement.setInt(1,Integer.parseInt(memberID.getText()));
                        statement.setInt(2,Integer.parseInt(fee.getText()));
                        statement.registerOutParameter(3, java.sql.Types.VARCHAR);
                        statement.execute();

                        String sqlmsg =statement.getString(3);
                        if(!sqlmsg.equals("correct")){
                            memberID.setText("");
                            fee.setText("");

                            JOptionPane.showMessageDialog(Driver.mainFrame,sqlmsg);
                        }
                        else{
                            msg.setText("Paid successfully");
                            memberID.setText("");
                            fee.setText("");
                            msg.setVisible(true);
                            Driver.mainFrame.setVisible(true);
                        }
                    }
                    catch(SQLException q){
                        q.printStackTrace();
                    }

                }
                else{
                    JOptionPane.showMessageDialog(Driver.mainFrame,
                            "Please enter both fields!");
                }
            }
        };

        receiveFine.addActionListener(handler);




        FocusListener focusListner= new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                msg.setText("");
                msg.setVisible(false);
            }
            @Override
            public void focusLost(FocusEvent e) {
            }

        };
        memberID.addFocusListener(focusListner);
        fee.addFocusListener(focusListner);



        KeyListener keyListener=new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JTextField p=(JTextField) e.getSource();
                if (p.getText().length() >= 4 ) // limit to 3 characters
                    e.consume();
            }
        };
        fee.addKeyListener(keyListener);
        memberID.addKeyListener(keyListener);
    }
}