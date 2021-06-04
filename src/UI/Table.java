package UI;

import javax.swing.*;

public class Table extends JFrame {

    private String[] columns;
    private Object[][] data;

    public Table(String[] columns, Object[][] data) {
        this.columns = columns;
        this.data = data;
        //create table with data
        JTable table = new JTable(data, columns);
        //add the table to the frame
        this.add(new JScrollPane(table));
        this.setTitle("Employees");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setSize(450, 300);
        this.setLocationRelativeTo(null);
    }

}
