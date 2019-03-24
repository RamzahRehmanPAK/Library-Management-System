/**
 * Created by Ramzah Rehman on 10/25/2016.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by Ramzah Rehman on 10/23/2016.
 */
class Login {
    static ImagePanel mainPanel;
    static JButton buttonLogin;
    static JTextField textFieldUsename;
    static JTextField textFieldPassword;

    public static void createPanel() {
        try {

            Toolkit toolkit = Toolkit.getDefaultToolkit();

            Image image = ImageIO.read(new File("library.jpg"));//returns BufferedImage

            mainPanel = new ImagePanel();
            mainPanel.setBackground(image);

            GridBagLayout mainPanelLayout = new GridBagLayout();

            mainPanel.setLayout(mainPanelLayout);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;


            JPanel login = new JPanel();
            Color c = new Color(211, 211, 211, 0x88);
            login.setBackground(c);


            mainPanel.add(login, gbc);


            buttonLogin = new JButton("Log In");
            buttonLogin.setPreferredSize(new Dimension(300, 30));

            textFieldUsename = new JTextField();
            textFieldPassword = new JPasswordField();

            textFieldUsename.setPreferredSize(new Dimension(150, 30));
            textFieldPassword.setPreferredSize(new Dimension(150, 30));

            JLabel labelUsename = new JLabel("Username");
            JLabel labelPassword = new JLabel("Password");
            labelUsename.setForeground(new Color(225, 204, 153));
            labelPassword.setForeground(new Color(225, 204, 153));
            labelUsename.setFont(labelUsename.getFont().deriveFont(20.0f));
            labelPassword.setFont(labelUsename.getFont().deriveFont(20.0f));


            GridBagLayout loginLayout = new GridBagLayout();
            login.setLayout(loginLayout);

            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.insets = new Insets(15, 30, 15, 0);

            gbc.gridx = 0;
            gbc.gridy = 0;

            login.add(labelUsename, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;


            login.add(labelPassword, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;


            gbc.insets = new Insets(15, -180, 15, 30);

            login.add(textFieldUsename, gbc);


            gbc.gridx = 1;
            gbc.gridy = 1;

            login.add(textFieldPassword, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.insets = new Insets(15, 30, 15, 30);

            login.add(buttonLogin, gbc);


            /*GroupLayout.SequentialGroup a=layout.createSequentialGroup();


            layout.setHorizontalGroup(a);


            a.addComponent(btn1);

            GroupLayout.SequentialGroup b=layout.createSequentialGroup();
            a.addGroup(b);
             GroupLayout.ParallelGroup d= layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            b.addGroup(d);
            d.addComponent(btn2);
            d.addComponent(btn3);



            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addComponent(btn1)
                    .addComponent(btn2)
                    .addComponent(btn3)
            );
            login.setLayout(layout);
            */
            registerComponent();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public static void registerComponent(){

        ActionListener loginListener= new ActionListener(){
            @Override
           public void actionPerformed(ActionEvent e){

                if(Login.textFieldUsename.getText().isEmpty() || Login.textFieldPassword.getText().isEmpty()){
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "Username or password not entered.");
                    }
                    else{
                        try {
                            CallableStatement statement=Driver.con.prepareCall("{call librarySystem.checkLogin(?,?,?)}");
                            statement.setString(1,Login.textFieldUsename.getText());
                            statement.setString(2,Login.textFieldPassword.getText());
                            statement.registerOutParameter(3, Types.INTEGER);

                            statement.execute();
                            int valid;
                            valid=statement.getInt(3);
                            if(valid==1){

                                Issue.showPanel();
                                MenuBar.setMenuBar();
                                Login.textFieldUsename.setText("");
                                Login.textFieldPassword.setText("");


                            }
                            if(valid==0){
                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "Username or password is invalid. Try again.");
                                Login.textFieldUsename.setText("");
                                Login.textFieldPassword.setText("");
                            }


                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }

                    }
                }

        };
        buttonLogin.addActionListener(loginListener);



        textFieldUsename.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textFieldUsename.getText().length() >30 ) // limit to 4 characters
                    e.consume();
            }
        });

        textFieldPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textFieldPassword.getText().length() >30 ) // limit to 4 characters
                    e.consume();
            }
        });


    }
    public  static  void showPanel(){
        Driver.mainFrame.setContentPane(mainPanel);
        Driver.mainFrame.setVisible(true);

    }
}

