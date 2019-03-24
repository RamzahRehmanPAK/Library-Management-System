
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;

public class RemoveMember {

    static ImagePanel mainPanel;

    static JTextField textID;
    static JTextField textMemberName;
    static JTextField textMemberPhoneNo;
    static JTextField textMembrAddress;
    static JTextField textMembrFine;
    static JTextField textMembrIssue;

    static JButton remove;

    public static void createPanel(){

        try{

            Toolkit toolkit = Toolkit.getDefaultToolkit();

            BufferedImage image = ImageIO.read(new File("foo.jpg"));//returns BufferedImage

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


            textMemberName=new JTextField("");
            textMemberPhoneNo=new JTextField("");
            textMembrAddress=new JTextField("");
            textMembrFine=new JTextField("");
            textMembrIssue=new JTextField("");
            textID=new JTextField("");

            remove=new JButton("Remove");

            Color c=new Color(50, 50, 50);
            textMemberName.setForeground(c);
            textMembrAddress.setForeground(c);
            textMemberPhoneNo.setForeground(c);
            textMembrFine.setForeground(c);
            textMembrIssue.setForeground(c);


            textMemberName.setEditable(false);
            textMembrAddress.setEditable(false);
            textMemberPhoneNo.setEditable(false);
            textMembrFine.setEditable(false);
            textMembrIssue.setEditable(false);


            Dimension d=new Dimension(100, 30);
            textMemberName.setPreferredSize(d);
            textMembrAddress.setPreferredSize(new Dimension(150,30));
            textMemberPhoneNo.setPreferredSize(d);
            textMembrIssue.setPreferredSize(d);
            textMembrFine.setPreferredSize(d);
            textID.setPreferredSize(d);


            GridBagConstraints gbc=new GridBagConstraints();

            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx=1;
            gbc.gridy=0;
            gbc.insets=new Insets(0,20,0,0);
            mainPanel.add(textID,gbc);

            gbc.insets=new Insets(0,0,0,0);

            gbc.gridx=0;
            gbc.gridy=1;

            mainPanel.add(textMemberName,gbc);

            gbc.gridx=0;
            gbc.gridy=2;

            mainPanel.add(textMembrAddress,gbc);

            gbc.gridx=0;
            gbc.gridy=3;

            mainPanel.add(textMemberPhoneNo,gbc);

            gbc.gridx=0;
            gbc.gridy=4;

            mainPanel.add(textMembrIssue,gbc);

            gbc.gridx=0;
            gbc.gridy=5;

            mainPanel.add(textMembrFine,gbc);

            gbc.insets=new Insets(0,20,0,0);

            gbc.gridx=1;
            gbc.gridy=6;

            mainPanel.add(remove,gbc);




            registerComponent();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public static void showPanel(){

        textID.setText("");
        textMemberName.setText("Name");
        textMemberPhoneNo.setText("Phone No");
        textMembrAddress.setText("Address");
        textMembrIssue.setText("Issued Books");
        textMembrFine.setText("Fine");


        Driver.mainFrame.setContentPane(mainPanel);
        Driver.mainFrame.setVisible(true);
    }

    public static void registerComponent(){


        ActionListener handler=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getSource()==remove){
                    if(!textID.getText().isEmpty()){
                        String temp=textID.getText();

                        for(int i=0;i<temp.length();i++){
                            if(temp.charAt(i)<'0' || temp.charAt(i)>'9'){

                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "ID should be an integer!");
                                textID.setText("");
                                return;
                            }
                        }

                        try {
                            PreparedStatement statement=Driver.con.prepareStatement("DELETE FROM librarySystem.Member WHERE ID =?");

                            int ID=Integer.parseInt(textID.getText());

                            statement.setInt(1,ID);

                            int result=statement.executeUpdate();

                            if(result==1){
                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "Member removed!");

                            }
                            else{
                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "Member doesn't exist!");

                            }
                            textID.setText("");
                            textMemberName.setText("Name");
                            textMemberPhoneNo.setText("Phone No");
                            textMembrAddress.setText("Address");
                            textMembrIssue.setText("Issued Books");
                            textMembrFine.setText("Fine");

                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "No Member chosen!");
                    }

                }
                if(e.getSource()==textID){

                    String temp=textID.getText();
                    if(temp.length()==0){
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "ID should not be empty!");
                        return;

                    }
                    for(int i=0;i<temp.length();i++){
                        if(temp.charAt(i)<'0' || temp.charAt(i)>'9'){

                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "ID should be an integer!");
                            textID.setText("");
                            return;
                        }
                    }
                    try {
                        PreparedStatement statement = Driver.con.prepareStatement("select count(*) from librarySystem.Member M where M.ID =?");

                        statement.setInt(1,Integer.parseInt(textID.getText()));
                        ResultSet rs=statement.executeQuery();

                        rs.next();
                        int count=rs.getInt(1);
                        if(count !=1){
                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "Member does not exist!");
                            textID.setText("");
                            return;
                        }
                        else{
                            statement =Driver.con.prepareStatement("SELECT * from librarySystem.Member M where M.ID=? ");
                            statement.setInt(1,Integer.parseInt(textID.getText()));
                            rs=statement.executeQuery();

                            rs.next();
                            textMemberName.setText(rs.getString(2));
                            textMembrAddress.setText(rs.getString(5));
                            textMemberPhoneNo.setText(rs.getString(4));
                            textMembrFine.setText(String.valueOf(rs.getInt(3)));
                            textMembrIssue.setText(String.valueOf(rs.getInt(6)));

                        }

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }


                }

            }

        };
        remove.addActionListener(handler);
        textID.addActionListener(handler);



        FocusListener focusListner= new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {

                textID.setText("");
                textMemberName.setText("Name");
                textMemberPhoneNo.setText("Phone No");
                textMembrAddress.setText("Address");
                textMembrIssue.setText("Issued Books");
                textMembrFine.setText("Fine");

            }
            @Override
            public void focusLost(FocusEvent e) {
            }

        };
        textID.addFocusListener(focusListner);




        textID.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {
                if (textID.getText().equals("")){
                    textMemberName.setText("Name");
                    textMemberPhoneNo.setText("Phone No");
                    textMembrAddress.setText("Address");
                    textMembrIssue.setText("Issued Books");
                    textMembrFine.setText("Fine");
                }
            }
        });



        textID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textID.getText().length() >4 ) // limit to 4 characters
                    e.consume();
            }
        });

    }

}
