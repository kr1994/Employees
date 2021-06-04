package UI;

import WorkLog.FindTwoEmployees;
import WorkLog.WorkLogDto;
import WorkLog.WorkLogPair;
import WorkLog.WorkLogParser;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class FileSelectorWindow extends JPanel implements ActionListener {

    private static final String newline = "\n";
    JButton openButton;
    JTextArea log;
    JFileChooser fc;

    public FileSelectorWindow() {

        super(new BorderLayout());

        //Create the log first, because the action listener needs to refer to it.
        log = new JTextArea(15, 30);
        log.setMargin(new Insets(25, 25, 25, 25));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        openButton = new JButton("Open a File...", createImageIcon("images/Open16.gif"));
        openButton.addActionListener(this);

        //For layout purposes, put the button in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);

        //Add the button and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(FileSelectorWindow.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = openFile();
                WorkLogPair pairResult = getWorkLogPair(file);
                createTable(pairResult);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FileSelectorWindow.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void createTable(WorkLogPair pairResult) {
        //headers for the table
        String[] columns = new String[] {
                "Employee ID 1", "Employee ID 2", "Project ID", "Days Worked"
        };

        //actual data for the table in a 2d array
        Object[][] data = new Object[][] {
                {       pairResult.getEmployeeId1(),
                        pairResult.getEmployeeId2(),
                        pairResult.getProjectId(),
                        pairResult.getDuration()
                },
        };

        Table table = new Table(columns, data);
    }

    private WorkLogPair getWorkLogPair(File file) {
        List<WorkLogDto> workLogDtoList = WorkLogParser.readFile(file);
        WorkLogPair pairResult = FindTwoEmployees.findMostLongerCoworkers(workLogDtoList);
        return pairResult;
    }

    private File openFile() {
        File file = fc.getSelectedFile();
        log.append("Opening: " + file.getName() + "." + newline);
        if (file.exists()) {
            log.append("File: " + file.getName() + " has been successfully opened!" + newline);
        } else {
            log.append("There is a problem with file: " + file.getName() + " which couldn't be opened!" + newline);
        }
        return file;
    }

}