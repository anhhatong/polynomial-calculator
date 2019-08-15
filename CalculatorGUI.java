import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.io.*;
/**
 * Represents the Graphical User Interface of the polynomial calculator.
 * @author Maddie Tong
 * @version CMPU102 special edition
 */
public class CalculatorGUI {
    /**
     * Runs the Graphical User Interface of the polynomial calculator by
     * creating a frame and a panel with a text field allowing the user to
     * enter an expression, a label inviting the user to write in the text
     * field, a text area displaying the results of calculation, and three 
     * buttons labeled "Evaluate", "Choose file", "Clear", and "Exit" 
     */
    public static void main(String[] args) {
        Calculator calc1 = new Calculator(); // Memory for interactive mode
        Calculator calc2 = new Calculator(); // Memory for file mode

        JFrame window = new JFrame("Polynomial Calculator");
        window.setSize(700,600);
        window.setLocationRelativeTo(null);

        JPanel content = new JPanel();
        window.setContentPane(content);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
        });

        JLabel label = new JLabel("Enter expression:");
        content.add(label);

        JTextField chooseMode = new JTextField(30);
        content.add(chooseMode);

        JButton evalButton = new JButton("Evaluate");
        content.add(evalButton);

        JTextArea tarea = new JTextArea(30,50);
        content.add(tarea);

        evalButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String s;
                    s = chooseMode.getText(); //get the expression that the user typed in
                    if(s.length() == 0)
                    return;
                    tarea.append(s);
                    tarea.append("\n");
                    s = calc1.calculate(s);
                    tarea.append("Result: "+s);
                    tarea.append("\n");
                }
            });

        JButton chooseFile = new JButton("Choose file");
        content.add(chooseFile);
        chooseFile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Text files","txt");
                    fc.setFileFilter(filter); // only take text files
                    int res = fc.showOpenDialog(window); // open folder for the user to choose file
                    if (res == JFileChooser.APPROVE_OPTION){
                        StringBuilder sb = new StringBuilder();
                        File file = fc.getSelectedFile();

                        try {
                            BufferedReader r = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line = r.readLine()) != null) {
                                tarea.append(line); 
                                tarea.append("\n");
                                
                                sb.append("Result: "+calc2.calculate(line));
                                sb.append("\n");
                                tarea.append(sb.toString());
                                sb = new StringBuilder(); // reset string for the next round 
                                
                            }
                        } catch (IOException ex) {
                            System.err.println(e);
                        }
                    }
                }
            });
        JButton clear = new JButton("Clear");
        content.add(clear);
        
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tarea.setText(null); //only clear the text area, not memory
            }
        });
        content.add(exitButton);
        window.setVisible(true);
        window.toFront();
    }
}