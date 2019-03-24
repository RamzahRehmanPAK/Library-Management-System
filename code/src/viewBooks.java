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

class viewBooks {
    static ImagePanel mainPanel;
    static JTable table;
    static JScrollPane scrollPane;
    static JComboBox choice;
    static JTextField ISBN;
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


            //combo box

            String[] choiceString = {"All","Issued","Specific"};

//Create the combo box.
//Indices start at 0
            choice = new JComboBox(choiceString);



            ISBN=new JTextField();
            ISBN.setPreferredSize(new Dimension(70,25));


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx=0;
            gbc.gridy=0;
            gbc.insets=new Insets(0,0,20,10);
            mainPanel.add(choice,gbc);

            gbc.gridx=1;
            gbc.gridy=0;
            mainPanel.add(ISBN,gbc);


            gbc.gridx=1;
            gbc.gridy=1;
            gbc.insets=new Insets(0,0,0,0);
            mainPanel.add(scrollPane,gbc);

            registerComponent();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private static void  setRequiredDataInTable(String sql,String ISBN){

        try{
            PreparedStatement statement =Driver.con.prepareStatement(sql);
            if(ISBN!=""){
                statement.setString(1,ISBN);
            }
            ResultSet rs = statement.executeQuery();

            buildTableModel(rs,(DefaultTableModel) table.getModel());

            // set preferred column widths

            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(90);
            table.getColumnModel().getColumn(2).setPreferredWidth(60);
            table.getColumnModel().getColumn(3).setPreferredWidth(160);
            table.getColumnModel().getColumn(4).setPreferredWidth(60);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
    public  static  void showPanel(){

        choice.setSelectedIndex(0);
        ISBN.setText("");
        ISBN.setVisible(false);

        String sql="select * from librarySystem.Book";
        setRequiredDataInTable(sql,"");

        Driver.mainFrame.setContentPane(mainPanel);
        Driver.mainFrame.setVisible(true);
    }

    private static void registerComponent(){

        ActionListener handler=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getSource()==ISBN){
                    if(!ISBN.getText().isEmpty()) {

                        String temp = ISBN.getText();

                        for (int i = 0; i < temp.length(); i++) {
                            if (temp.charAt(i) < '0' || temp.charAt(i) > '9') {

                                JOptionPane.showMessageDialog(Driver.mainFrame,
                                        "ISBN should be in digits!");
                                ISBN.setText("");
                                return;
                            }
                        }
                        String sql="select * from librarySystem.Book B where B.ID=? ";
                        setRequiredDataInTable(sql,ISBN.getText());


                    }
                    else{
                        JOptionPane.showMessageDialog(Driver.mainFrame,
                                "Please Enter ISBN!");
                    }
                }
                if(e.getSource()==choice){

                    //all
                    if(choice.getSelectedIndex()==0){
                        ISBN.setText("");
                        ISBN.setVisible(false);

                        String sql="select * from librarySystem.Book";
                        setRequiredDataInTable(sql,"");

                    }
                    //Issued
                    else if(choice.getSelectedIndex()==1){
                        //index is 1
                        ISBN.setText("");
                        ISBN.setVisible(false);

                        String sql="select * from librarySystem.Book B where B.available<B.quantity";
                        setRequiredDataInTable(sql,"");

                    }
                    //2 i.e. specific
                    else{
                        ISBN.setText("");
                        ISBN.setVisible(true);
                    }
                    Driver.mainFrame.setVisible(true);
                }

            }
        };
        choice.addActionListener(handler);
        ISBN.addActionListener(handler);


        ISBN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (ISBN.getText().length() >13 ) // limit to 4 characters
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

