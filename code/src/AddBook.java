import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.NumberFormat;

/**
 * Created by Ramzah Rehman on 10/26/2016.
 */
public class AddBook {

    static ImagePanel mainPanel;
    static JTextField textTitle;
    static JTextField textAuthors;
    static JTextField textQuantity;
    static JTextField textISBN;
    static JButton addBook;
    static JLabel msg =new JLabel();
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

            JLabel labeltitle=new JLabel("Book title");
            JLabel labelAuthor=new JLabel("Authors");
            JLabel labelQuantity=new JLabel("Quantity");
            JLabel labelISBN=new JLabel("ISBN");


            Dimension d=new Dimension(150, 30);

            Color c=new Color(155,101,101);
            labeltitle.setForeground(c);
            labelAuthor.setForeground(c);
            labelQuantity.setForeground(c);
            labelISBN.setForeground(c);

            Font font= new Font("Harlow Solid Italic",Font.PLAIN,24);
            labelAuthor.setFont(font);
            labeltitle.setFont(font);
           labelQuantity.setFont(font);
            labelISBN.setFont(font);

            msg.setForeground(c);
            msg.setPreferredSize(d);
            msg.setFont(new Font("Calibri (Body)",Font.PLAIN,18));

            textTitle=new JTextField();
            textAuthors=new JTextField();
            textQuantity=new JTextField();
            textISBN=new JTextField();



            textTitle.setPreferredSize(d);
            textISBN.setPreferredSize(d);
            textAuthors.setPreferredSize(d);
            textQuantity.setPreferredSize(d);


            GridBagConstraints gbc=new GridBagConstraints();

            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx=0;
            gbc.gridy=0;
            gbc.insets=new Insets(0,0,10,20);
            mainPanel.add(labeltitle,gbc);


            gbc.gridx=0;
            gbc.gridy=1;
            mainPanel.add(labelISBN,gbc);

            gbc.gridx=0;
            gbc.gridy=2;

            mainPanel.add(labelAuthor,gbc);

            gbc.gridx=0;
            gbc.gridy=3;

            mainPanel.add(labelQuantity,gbc);


            gbc.insets=new Insets(0,0,10,0);

            gbc.gridx=1;
            gbc.gridy=0;

            mainPanel.add(textTitle,gbc);

            gbc.gridx=1;
            gbc.gridy=1;

            mainPanel.add(textISBN,gbc);

            gbc.gridx=1;
            gbc.gridy=2;

            mainPanel.add(textAuthors,gbc);

            gbc.gridx=1;
            gbc.gridy=3;

            mainPanel.add(textQuantity,gbc);


            addBook=new JButton("Add");

            gbc.gridx=1;
            gbc.gridy=4;
            gbc.insets=new Insets(30,0,0,0);
            msg.setVisible(false);
            mainPanel.add(msg,gbc);

            gbc.gridx=2;
            gbc.gridy=4;
            //gbc.gridwidth=2; to expand it to multiple rows/columns
            gbc.insets=new Insets(30,0,0,0);
            mainPanel.add(addBook,gbc);
            registerComponent();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void showPanel(){

        textTitle.setText("");
        textAuthors.setText("");
        textQuantity.setText("");
        textISBN.setText("");

        msg.setVisible(false);

        Driver.mainFrame.setContentPane(mainPanel);
        Driver.mainFrame.setVisible(true);

    }
    public static void registerComponent(){

        textQuantity.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textQuantity.getText().length() >4 )
                    e.consume();
            }
        });

        textISBN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textISBN.getText().length() >13 )
                    e.consume();
            }
        });


        FocusListener focusListner= new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                msg.setVisible(false);
            }
            @Override
            public void focusLost(FocusEvent e) {
                //Your code here
            }
        };


        textTitle.addFocusListener(focusListner);
        textAuthors.addFocusListener(focusListner);
        textQuantity.addFocusListener(focusListner);
        textISBN.addFocusListener(focusListner);

        ActionListener handler=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(! textTitle.getText().isEmpty()
                        && ! textAuthors.getText().isEmpty()
                        && ! textQuantity.getText().isEmpty()
                        && ! textISBN.getText().isEmpty()){

                    if( textISBN.getText().length()!=13){
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "ISBN must contain exactly 13 digits!");
                         textISBN.setText("");
                        return;

                    }
                    else {
                        String temp= textISBN.getText();
                        for(int i=0;i<temp.length();i++){
                            if(temp.charAt(i)>'9' || temp.charAt(i)<'0'){
                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "ISBN must contain  digits!");
                                 textISBN.setText("");
                                return;
                            }
                        }

                    }
                    String temp=textQuantity.getText();
                    for(int i=0;i<temp.length();i++){
                        if(temp.charAt(i)<'0' || temp.charAt(i)>'9'){
                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "Quantity should be a number!");
                            textQuantity.setText("");

                            return;
                        }
                    }
                    if(Integer.parseInt( textQuantity.getText())==0){
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "Quantity should be greater than zero!");
                         textQuantity.setText("");
                        return;
                    }

                    else{

                        try {
                            CallableStatement statement=Driver.con.prepareCall("{call librarySystem.addBook(?,?,?,?)}");

                            statement.setString(1, textTitle.getText());
                            statement.setString(2,  textISBN.getText());
                            statement.setString(3,  textAuthors.getText());
                            statement.setInt(4,Integer.parseInt( textQuantity.getText()));


                            int result=statement.executeUpdate();

                            System.out.println(result);
                            if(result==-1){
                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "Book with same ISBN has already been added!");
                            }
                            else {
                                 msg.setText("Book added!");
                                 msg.setVisible(true);
                            }

                            textTitle.setText("");
                            textISBN.setText("");
                            textAuthors.setText("");
                            textQuantity.setText("");

                        }
                        catch(SQLException p ){
                            p.printStackTrace();
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(Driver.mainFrame,
                            "Please enter all fields!");
                }
            }
        };

         addBook.addActionListener(handler);

    }
}
