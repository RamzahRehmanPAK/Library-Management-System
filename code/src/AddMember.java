import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by Ramzah Rehman on 10/26/2016.
 */
public class AddMember {

    static ImagePanel mainPanel;
    static JTextField textMemberName;
    static JTextField textMemberPhoneNo;
    static JTextField textMembrAddress;
    static JButton addMember;
    static JLabel memberID =new JLabel();
    public static void createPanel(){

        try{

            Toolkit toolkit = Toolkit.getDefaultToolkit();

            BufferedImage image = ImageIO.read(new File("book.jpg"));//returns BufferedImage

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

            JLabel labelMemberName=new JLabel("Member Name");
            JLabel labelMemberPhoneno=new JLabel("Member Phone No");
            JLabel labelMemberAddress=new JLabel("Member Address");

            Color c=new Color(155,101,101);
            labelMemberName.setForeground(c);
            labelMemberAddress.setForeground(c);
            labelMemberPhoneno.setForeground(c);
            Font font= new Font("Harlow Solid Italic",Font.PLAIN,24);
            labelMemberName.setFont(font);
            labelMemberAddress.setFont(font);
            labelMemberPhoneno.setFont(font);


            memberID.setForeground(c);
            memberID.setFont(labelMemberName.getFont().deriveFont(15.0f));

            textMemberName=new JTextField();
            textMemberPhoneNo=new JTextField();
            textMembrAddress=new JTextField();

            Dimension d=new Dimension(150, 30);
            textMemberName.setPreferredSize(d);
            textMembrAddress.setPreferredSize(d);
            textMemberPhoneNo.setPreferredSize(d);


            GridBagConstraints gbc=new GridBagConstraints();

            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx=0;
            gbc.gridy=0;
            gbc.insets=new Insets(0,0,10,20);
            mainPanel.add(labelMemberName,gbc);

            gbc.gridx=0;
            gbc.gridy=1;
            mainPanel.add(labelMemberAddress,gbc);

            gbc.gridx=0;
            gbc.gridy=2;

            mainPanel.add(labelMemberPhoneno,gbc);

            gbc.insets=new Insets(0,0,10,0);

            gbc.gridx=1;
            gbc.gridy=0;

            mainPanel.add(textMemberName,gbc);

            gbc.gridx=1;
            gbc.gridy=1;

            mainPanel.add(textMembrAddress,gbc);

            gbc.gridx=1;
            gbc.gridy=2;

            mainPanel.add(textMemberPhoneNo,gbc);


            addMember =new JButton("Add");

            gbc.gridx=0;
            gbc.gridy=3;
            gbc.insets=new Insets(30,0,0,0);
            memberID.setVisible(false);
            mainPanel.add(memberID,gbc);

            gbc.gridx=2;
            gbc.gridy=3;
            //gbc.gridwidth=2; to expand it to multiple rows/columns
            gbc.insets=new Insets(30,0,0,0);
            mainPanel.add(addMember,gbc);





            registerComponent();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public static void showPanel(){

        textMemberName.setText("");
        textMemberPhoneNo.setText("");
        textMembrAddress.setText("");

        Driver.mainFrame.setContentPane(mainPanel);
        Driver.mainFrame.setVisible(true);
         memberID.setVisible(false);
    }
    public static void registerComponent(){


        ActionListener handler=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                   if(! textMemberName.getText().isEmpty()
                           && ! textMembrAddress.getText().isEmpty()
                           && ! textMemberPhoneNo.getText().isEmpty()){

                        if(      textMemberPhoneNo.getText().length()==11
                                &&  textMemberPhoneNo.getText().charAt(0)=='0'
                                &&  textMemberPhoneNo.getText().charAt(1)=='3'
                                &&   textMemberPhoneNo.getText().charAt(2) >= '0' &&
                                 textMemberPhoneNo.getText().charAt(2) <= '6'){
                            try {
                                CallableStatement statement=Driver.con.prepareCall("{call librarySystem.addMember(?,?,?,?)}");

                                statement.setString(1,textMemberName.getText());
                                statement.setString(2,textMemberPhoneNo.getText());
                                statement.setString(3,textMembrAddress.getText());
                                statement.registerOutParameter(4, Types.INTEGER);


                                int result=statement.executeUpdate();

                                System.out.println(result);
                                if(result==-1){
                                    JOptionPane.showMessageDialog(Driver.mainFrame,
                                            "Member with same name and phone no has already been added!");
                                }
                                else{
                                     memberID.setText("Member has ID "+ statement.getInt(4));
                                     memberID.setVisible(true);

                                }
                                 textMemberName.setText("");
                                 textMemberPhoneNo.setText("");
                                 textMembrAddress.setText("");
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }

                        }
                        else{
                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "Please enter a valid phone no!");
                             textMemberPhoneNo.setText("");
                        }

                    }
                    else{
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "Please enter all fields!");
                    }
                }
        };
         addMember.addActionListener(handler);



        FocusListener focusListner= new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                 memberID.setVisible(false);
            }
            @Override
            public void focusLost(FocusEvent e) {
                //Your code here
            }
        };
        textMemberName.addFocusListener(focusListner);
        textMemberPhoneNo.addFocusListener(focusListner);
        textMembrAddress.addFocusListener(focusListner);



        textMemberPhoneNo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textMemberPhoneNo.getText().length() >11 ) // limit to 4 characters
                    e.consume();
            }
        });
    }
}
