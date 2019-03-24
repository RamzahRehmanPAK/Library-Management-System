/**
 * Created by Ramzah Rehman on 10/28/2016.
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;

public class RemoveBook {

    static ImagePanel mainPanel;

    static JTextField textISBN;

    static JTextField textTitle;
    static JTextField textAuthor;
    static JTextField textQuantity;
    static JTextField textAvailable;
    static  JRadioButton all;
    static  JRadioButton some;
    static JTextField quant;

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

           textTitle=new JTextField();
            textAuthor=new JTextField();
           textQuantity=new JTextField();
            textAvailable=new JTextField();

            quant=new JTextField();
            textISBN=new JTextField();

            remove=new JButton("Remove");


             all = new JRadioButton("All Available");
             some = new JRadioButton("Some");
             all.setOpaque(false);
            some.setOpaque(false);

            //Group the radio buttons.
            ButtonGroup group = new ButtonGroup();
            group.add(all);
            group.add(some);


            Color c=new Color(50, 50, 50);
            textTitle.setForeground(c);
            textAvailable.setForeground(c);
            textAuthor.setForeground(c);
            textQuantity.setForeground(c);


            textAvailable.setEditable(false);
            textAuthor.setEditable(false);
            textTitle.setEditable(false);
            textQuantity.setEditable(false);


            Dimension d=new Dimension(100, 30);

            textAvailable.setPreferredSize(d);
            textAuthor.setPreferredSize(new Dimension(150,30));
            textQuantity.setPreferredSize(d);
            textTitle.setPreferredSize(d);

            quant.setPreferredSize(new Dimension(50, 30));
            textISBN.setPreferredSize(d);


            GridBagConstraints gbc=new GridBagConstraints();

            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx=0;
            gbc.gridy=0;
           // gbc.insets=new Insets(0,0,0,0);

            mainPanel.add(textISBN,gbc);

            gbc.gridx=1;
            gbc.gridy=0;
            mainPanel.add(all,gbc);

            gbc.gridx=2;
            gbc.gridy=0;
            mainPanel.add(some,gbc);


            gbc.gridx=3;
            gbc.gridy=0;
            mainPanel.add(quant,gbc);

            gbc.insets=new Insets(0,0,0,0);

            gbc.gridx=0;
            gbc.gridy=2;

            mainPanel.add(textTitle,gbc);

            gbc.gridx=0;
            gbc.gridy=3;

            mainPanel.add(textAuthor,gbc);

            gbc.gridx=0;
            gbc.gridy=4;

            mainPanel.add(textQuantity,gbc);

            gbc.gridx=0;
            gbc.gridy=5;

            mainPanel.add(textAvailable,gbc);


            gbc.insets=new Insets(0,0,0,0);

            gbc.gridx=3;
            gbc.gridy=6;

            mainPanel.add(remove,gbc);
            registerComponent();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public static void showPanel(){

        quant.setText("");
        quant.setVisible(false);
        textISBN.setText("");
        textTitle.setText("Title");
        textAuthor.setText("Author(s)");
        textQuantity.setText("Quantity");
        textAvailable.setText("Available");
        all.setSelected(true);

        Driver.mainFrame.setContentPane(mainPanel);
        Driver.mainFrame.setVisible(true);
    }

    public static void registerComponent(){


        ActionListener handler=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getSource()==remove){

                    if(!textISBN.getText().isEmpty()){

                        String temp=textISBN.getText();

                        for(int i=0;i<temp.length();i++){
                            if(temp.charAt(i)<'0' || temp.charAt(i)>'9'){

                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "ISBN should be in digits!");
                                textISBN.setText("");
                                return;
                            }
                        }
                        if(some.isSelected() & quant.getText().isEmpty()){
                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "Enter quantity!");
                            return;
                        }
                        if(some.isSelected() & !quant.getText().isEmpty()){
                            String temp2=quant.getText();
                            for(int i=0;i<temp2.length();i++){
                                if(temp2.charAt(i)<'0' || temp2.charAt(i)>'9'){
                                    JOptionPane.showMessageDialog(Driver.mainFrame,
                                            "Quantity should be a number!");
                                    quant.setText("");
                                    return;
                                }
                            }
                            if(Integer.parseInt( quant.getText())==0){
                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "Quantity should be greater than zero!");
                                quant.setText("");
                                return;
                            }
                        }
                        try {

                            PreparedStatement statement=Driver.con.prepareStatement("SELECT * from librarySystem.Book WHERE ID =?");

                            statement.setString(1,textISBN.getText());

                            ResultSet result=statement.executeQuery();

                            if(result.next()){

                                CallableStatement S=Driver.con.prepareCall("{call librarySystem.removeBook(?,?,?)}");

                                S.setString(1,textISBN.getText());
                                if(some.isSelected()){
                                    S.setInt(2,Integer.parseInt(quant.getText()));
                                }
                                else{
                                    S.setInt(2,-1);//-1 means all available
                                }
                                S.registerOutParameter(3,Types.INTEGER);
                                S.execute();

                                if(S.getInt(3)==0){
                                    JOptionPane.showMessageDialog(Driver.mainFrame,
                                            "Book not available in specified quantity!");
                                }
                                textISBN.setText("");
                                quant.setText("");



                            }
                            else{
                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "Book doesn't exist!");
                            }

                            textISBN.setText("");
                            quant.setText("");
                            textTitle.setText("Title");
                            textAuthor.setText("Author(s)");
                            textQuantity.setText("Quantity");
                            textAvailable.setText("Available");

                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "No Book chosen!");
                    }

                }
                if(e.getSource()==textISBN){

                    String temp=textISBN.getText();
                    if(temp.length()==0){
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "ISBN should not be empty!");
                        return;

                    }
                    for(int i=0;i<temp.length();i++){
                        if(temp.charAt(i)<'0' || temp.charAt(i)>'9'){

                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "ISBN should be in digits!");
                            textISBN.setText("");
                            return;
                        }
                    }
                    try {
                        PreparedStatement statement = Driver.con.prepareStatement("select count(*) from librarySystem.Book B where B.ID =?");

                        statement.setString(1,textISBN.getText());
                        ResultSet rs=statement.executeQuery();

                        rs.next();
                        int count=rs.getInt(1);
                        if(count !=1){
                            JOptionPane.showMessageDialog(Driver.mainFrame,
                                    "No such book exists!");
                            textISBN.setText("");
                            return;
                        }
                        else{
                            statement =Driver.con.prepareStatement("SELECT * from librarySystem.Book B where B.ID=? ");
                            statement.setString(1,textISBN.getText());

                            rs=statement.executeQuery();

                            rs.next();
                            textTitle.setText(rs.getString(2));
                            textQuantity.setText(String.valueOf(rs.getInt(3)));
                            textAuthor.setText(rs.getString(4));
                            textAvailable.setText(String.valueOf(rs.getInt(5)));

                        }

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                }

            }

        };
        remove.addActionListener(handler);
        textISBN.addActionListener(handler);



        FocusListener focusListner= new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {

                quant.setText("");
                textISBN.setText("");
                textTitle.setText("Title");
                textAuthor.setText("Author(s)");
                textQuantity.setText("Quantity");
                textAvailable.setText("Available");

            }
            @Override
            public void focusLost(FocusEvent e) {
            }

        };
        textISBN.addFocusListener(focusListner);

        some.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if(e.getStateChange()==1) {
                    quant.setVisible(true);
                    Driver.mainFrame.setVisible(true);
                }
                else {
                    quant.setVisible(false);
                    Driver.mainFrame.setVisible(true);
                }
            }
        });



        textISBN.getDocument().addDocumentListener(new DocumentListener() {
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
                if (textISBN.getText().equals("")){
                    quant.setText("");
                    textTitle.setText("Title");
                    textAuthor.setText("Author(s)");
                    textQuantity.setText("Quantity");
                    textAvailable.setText("Available");
                }
            }
        });


        quant.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (quant.getText().length() > 4 )
                    e.consume();
            }
        });

        textISBN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textISBN.getText().length() >13 ) // limit to 4 characters
                    e.consume();
            }
        });


    }

}

