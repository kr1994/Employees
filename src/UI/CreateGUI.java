package UI;

import javax.swing.*;

public class CreateGUI {

    /**
     * Create the GUI and show it.
     * For thread safety, this method should be invoked from the  event dispatch thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Browse a file: ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new FileSelectorWindow());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setSize(450, 300);
        frame.setLocationRelativeTo(null);
    }
}
