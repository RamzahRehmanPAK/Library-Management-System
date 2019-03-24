
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

public class Return {
    static ImagePanel mainPanel;
    static JLabel labelmemberID;
    static JLabel labelbookISBN;
    static JTextField memberID;
    static JTextField bookISBN;
    static JLabel msg;
    static JButton ReturnBook;

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
            bookISBN=new JTextField();

            Dimension d=new Dimension(150, 30);
            memberID.setPreferredSize(d);
            bookISBN.setPreferredSize(d);


            ReturnBook=new JButton("ReturnBook");
            ReturnBook.setPreferredSize(d);

            labelmemberID=new JLabel("Member ID");
            labelbookISBN=new JLabel("Book ISBN");
            msg=new JLabel();


            Color c=new Color(200, 10, 130);
            labelmemberID.setForeground(c);
            labelbookISBN.setForeground(c);
            msg.setForeground(c);


            Font font= new Font("Harlow Solid Italic",Font.PLAIN,24);
            labelmemberID.setFont(font);
            labelbookISBN.setFont(font);
            msg.setFont(new Font("Harlow Solid Italic",Font.PLAIN,18));


            GridBagConstraints gbc=new GridBagConstraints();

            gbc.gridx=2;
            gbc.gridy=0;

            gbc.insets=new Insets(0,20,0,0);
            mainPanel.add(ReturnBook,gbc);

            gbc.gridx=0;
            gbc.gridy=1;
            gbc.insets=new Insets(0,0,0,10);
            mainPanel.add(labelmemberID,gbc);

            gbc.gridx=1;
            gbc.gridy=1;
            mainPanel.add(memberID,gbc);

            gbc.gridx=0;
            gbc.gridy=2;

            mainPanel.add(labelbookISBN,gbc);

            gbc.gridx=1;
            gbc.gridy=2;

            mainPanel.add(bookISBN,gbc);

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
        bookISBN.setText("");

        msg.setVisible(false);
        Driver.mainFrame.setContentPane(mainPanel);
        Driver.mainFrame.setVisible(true);

    }
    public static void registerComponent() {

        ActionListener handler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!memberID.getText().isEmpty() && !bookISBN.getText().isEmpty()){

                    String temp=memberID.getText();

                    for(int i=0;i<temp.length();i++){
                        if(temp.charAt(i)<'0' || temp.charAt(i)>'9'){

                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "ID should be an integer!");
                            memberID.setText("");
                            bookISBN.setText("");
                            return;
                        }
                    }

                    temp= bookISBN.getText();
                    for(int i=0;i<temp.length();i++){
                        if(temp.charAt(i)>'9' || temp.charAt(i)<'0'){
                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "ISBN must contain  digits!");
                            bookISBN.setText("");
                            memberID.setText("");
                            return;
                        }
                    }
                    try{
                        CallableStatement statement=Driver.con.prepareCall("{ call librarySystem.ReturnBook (?,?,?) }");
                        statement.setInt(1,Integer.parseInt(memberID.getText()));
                        statement.setString(2,bookISBN.getText());
                        statement.registerOutParameter(3, java.sql.Types.VARCHAR);
                        statement.execute();

                        String sqlmsg =statement.getString(3);
                        if(!sqlmsg.equals("correct")){
                            memberID.setText("");
                            bookISBN.setText("");
                            JOptionPane.showMessageDialog(Driver.mainFrame,sqlmsg);
                        }
                        else{
                            msg.setText("returned successfully");
                            memberID.setText("");
                            bookISBN.setText("");
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

        ReturnBook.addActionListener(handler);




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
        bookISBN.addFocusListener(focusListner);



        memberID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (memberID.getText().length() >4 ) // limit to 4 characters
                    e.consume();
            }
        });
        bookISBN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (bookISBN.getText().length() >13 ) // limit to 4 characters
                    e.consume();
            }
        });

    }
}