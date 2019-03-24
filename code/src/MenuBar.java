import javax.management.JMException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Ramzah Rehman on 10/25/2016.
 */
public class MenuBar {
    static JMenuBar menuBar;
    static JMenu add;
    static JMenu remove;
    static JMenu view;
    static JMenu action;
    static JMenu option;

    static JMenuItem addMember;
    static JMenuItem addBook;
    static JMenuItem removeMember;
    static JMenuItem removeBook;
    static JMenuItem viewMember;
    static JMenuItem viewBook;
    static JMenuItem issue;
    static JMenuItem returnBook;
    static JMenuItem receiveFine;
    static JMenuItem logout;
    private static JMenuItem createMenuItem( JMenu menu, String sText, ImageIcon image, int acceleratorKey, String sToolTip ) {
        // Create the item
        JMenuItem menuItem;

        menuItem = new JMenuItem();

        // Add the item test
        menuItem.setText( sText );

        // Add the optional icon
        if( image != null )
            menuItem.setIcon( image );

        // Add the accelerator key
        if( acceleratorKey > 0 )
            menuItem.setMnemonic( acceleratorKey );

        // Add the optional tool tip text
        if( sToolTip != null )
            menuItem.setToolTipText( sToolTip );

        menu.add( menuItem );

        return menuItem;
    }
    public static void createMenuBar(){

        menuBar = new JMenuBar();
        add=new JMenu("Add");
        remove =new JMenu("Remove");
        view =new JMenu("View");
        action=new JMenu("Action");
        option=new JMenu("Option");

        menuBar.add(add);
        menuBar.add(action);
        menuBar.add(remove);
        menuBar.add(view);
        menuBar.add(option);


        addMember=createMenuItem(add,"Member",null,0,null);
        addBook=createMenuItem(add,"Book",null,0,null);


        removeMember=createMenuItem(remove,"Member",null,0,null);
        removeBook=createMenuItem(remove,"Book",null,0,null);

        viewMember=createMenuItem(view,"Members",null,0,null);
        viewBook=createMenuItem(view,"Books",null,0,null);


        issue=createMenuItem(action,"Issue book",null,0,null);
        issue.setMnemonic(KeyEvent.VK_I);

        receiveFine=createMenuItem(action,"Receive fine",null,0,null);
        returnBook=createMenuItem(action,"Return book",null,0,null);

        returnBook.setMnemonic(KeyEvent.VK_R);
        receiveFine.setMnemonic(KeyEvent.VK_F);


        logout=createMenuItem(option,"Log out",null,0,null);

      //  menuBar.setPreferredSize(  new Dimension(  (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),menuBar.getHeight()  )   );

        registerComponent();


    }
    public static void setMenuBar(){
        Driver.mainFrame.setJMenuBar(menuBar);
        Driver.mainFrame.setVisible(true);
    }
    private static void registerComponent(){
        ActionListener handler=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==MenuBar.addMember){
                    AddMember.showPanel();
                }
                if(e.getSource()==MenuBar.addBook){
                    AddBook.showPanel();
                }
                if(e.getSource()==MenuBar.logout){
                    Login.showPanel();
                    Driver.mainFrame.setJMenuBar(null);
                    Driver.mainFrame.setVisible(true);
                }
                if(e.getSource()==MenuBar.removeMember){
                    RemoveMember.showPanel();
                }
                if(e.getSource()==MenuBar.removeBook){
                    RemoveBook.showPanel();
                }
                if(e.getSource()==MenuBar.viewMember){
                    viewMembers.showPanel();
                }
                if(e.getSource()==MenuBar.viewBook){
                    viewBooks.showPanel();
                }
                if(e.getSource()==MenuBar.issue){
                    Issue.showPanel();
                }
                if(e.getSource()==MenuBar.returnBook){
                    Return.showPanel();
                }
                if(e.getSource()==MenuBar.receiveFine){
                    ReceiveFine.showPanel();
                }

            }

        };
        addMember.addActionListener(handler);
        addBook.addActionListener(handler);
        logout.addActionListener(handler);
        removeMember.addActionListener(handler);
        removeBook.addActionListener(handler);
        viewMember.addActionListener(handler);
        viewBook.addActionListener(handler);
        issue.addActionListener(handler);
        returnBook.addActionListener(handler);
        receiveFine.addActionListener(handler);

    }
}

