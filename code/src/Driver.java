/**
 * Created by Ramzah Rehman on 10/22/2016.
 */

import javafx.scene.paint.*;

import java.awt.*;
import java.awt.Color;
import java.sql.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
public class Driver {
    static Connection con;
    static JFrame mainFrame;
    public static void main(String[] args) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-ND3EINK\\SQLEXPRESS;databaseName=librarySystem;integratedSecurity=true");


            Toolkit toolkit = Toolkit.getDefaultToolkit();

           mainFrame = new JFrame("Library Management  System");

            mainFrame.setBounds((int) toolkit.getScreenSize().getWidth()/8 , (int) toolkit.getScreenSize().getHeight()/8, (int) toolkit.getScreenSize().getWidth()*7/10 , (int) toolkit.getScreenSize().getHeight()*7/10 );


            MenuBar.createMenuBar();
            Login.createPanel();
            Issue.createPanel();
            Return.createPanel();
            ReceiveFine.createPanel();
            AddMember.createPanel();
            AddBook.createPanel();
            RemoveMember.createPanel();
            RemoveBook.createPanel();
            viewMembers.createPanel();
            viewBooks.createPanel();

            Login.showPanel();
            //Issue.showPanel();


            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);//always at the end

        } catch (Exception e) {
            e.printStackTrace();

        }



    }

}