/**
 * Created by Ramzah Rehman on 10/30/2016.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.util.Vector;

class viewMembers {
    static ImagePanel mainPanel;
    static JTable table;
    static JScrollPane scrollPane;
    static JComboBox choice;
    static JTextField ID;
    public static void createPanel() {
        try {

            Toolkit toolkit = Toolkit.getDefaultToolkit();

            BufferedImage image = ImageIO.read(new File("hugeLibrary.jpg"));//returns BufferedImage

            BufferedImage tmpImg = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = (Graphics2D) tmpImg.getGraphics();

            g2d.setComposite(AlphaComposite.SrcOver.derive(0.8f));
            //set the transparency level in range 0.0f - 1.0f

            g2d.drawImage(image, 0, 0, null);

            image = tmpImg;

            mainPanel = new ImagePanel();
            mainPanel.setBackground(image);


            GridBagLayout mainPanelLayout = new GridBagLayout();

            mainPanel.setLayout(mainPanelLayout);



            table = new JTable();

            table.setDefaultEditor(Object.class, null);  //not editable

            table.setShowGrid(false); //hide table lines

            //table.setIntercellSpacing(new Dimension(0, 0));

            table.setOpaque(false);
            table.setForeground(new Color(0,0,0));
            table.setBackground(new Color(215,174,222,0x88));

            table.getTableHeader().setOpaque(false);
            table.getTableHeader().setBackground(new Color(215,174,222,0x88));
            table.getTableHeader().setForeground(new Color(0,0,0));

            //table.setPreferredSize(new Dimension(20,20));
            scrollPane=new JScrollPane(table);

            //scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());


            String[] choiceString = {"All","Specific"};
            choice = new JComboBox(choiceString);

            ID=new JTextField();
            ID.setPreferredSize(new Dimension(70,25));


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;


            gbc.gridx=0;
            gbc.gridy=0;
            gbc.insets=new Insets(0,0,20,10);
            mainPanel.add(choice,gbc);

            gbc.gridx=1;
            mainPanel.add(ID,gbc);

            gbc.gridx=1;
            gbc.gridy=1;
            gbc.insets=new Insets(0,0,0,0);
            mainPanel.add(scrollPane,gbc);

            registerComponent();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void  setRequiredDataInTable(String sql,int ID){

        try{
            PreparedStatement statement =Driver.con.prepareStatement(sql);
            if(ID!=-1){
                statement.setInt(1,ID);
            }
            ResultSet rs = statement.executeQuery();

            /*if (rs.isBeforeFirst() ) {
                //means it has some records
            }*/

            buildTableModel(rs,(DefaultTableModel) table.getModel());

            // set preferred column widths

            table.getColumnModel().getColumn(0).setPreferredWidth(30);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(50);
            table.getColumnModel().getColumn(3).setPreferredWidth(90);
            table.getColumnModel().getColumn(4).setPreferredWidth(130);
            table.getColumnModel().getColumn(5).setPreferredWidth(90);

        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
    public  static  void showPanel(){

        choice.setSelectedIndex(0);
        ID.setText("");
        ID.setVisible(false);

        String sql="select * from librarySystem.Member";
        setRequiredDataInTable(sql,-1);

        Driver.mainFrame.setContentPane(mainPanel);
        Driver.mainFrame.setVisible(true);

    }

    private static void registerComponent(){

        ActionListener handler=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getSource()==ID){
                    if(!ID.getText().isEmpty()) {

                        String temp = ID.getText();

                        for (int i = 0; i < temp.length(); i++) {
                            if (temp.charAt(i) < '0' || temp.charAt(i) > '9') {

                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "ID should be a number!");
                                ID.setText("");
                                return;
                            }
                        }
                        String sql="select * from librarySystem.Member M where M.ID=? ";
                        setRequiredDataInTable(sql,Integer.parseInt(ID.getText()));

                    }
                    else{
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "Please Enter member ID!");
                    }
                }
                if(e.getSource()==choice){

                    //all
                    if(choice.getSelectedIndex()==0){
                        ID.setText("");
                        ID.setVisible(false);

                        String sql="select * from librarySystem.Member";
                        setRequiredDataInTable(sql,-1);

                    }
                    //1 i.e. specific
                    else{
                        ID.setText("");
                        ID.setVisible(true);
                    }
                    Driver.mainFrame.setVisible(true);
                }

            }
        };
        choice.addActionListener(handler);
        ID.addActionListener(handler);

        ID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (ID.getText().length() >4 ) // limit to 4 characters
                    e.consume();
            }
        });
    }

    private static void  buildTableModel(ResultSet rs,DefaultTableModel tableModel) //source http://stackoverflow.com/questions/10620448/most-simple-code-to-populate-jtable-from-resultset
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();

        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

                tableModel.setDataVector(data,columnNames);

    }
}

