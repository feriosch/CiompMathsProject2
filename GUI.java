import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GUI {

    public JButton chooseFileButton, topDownString;
	public JLabel JLfondo;
    public JLabel fileChosenLabel, acceptedStringLabel;
    public JTextField stringTextField;
    public ImageIcon backgroundImage;
    private JFrame frame;
    private Project project;
    
    public GUI(){

        project = new Project();
        
        backgroundImage = new ImageIcon(new ImageIcon("images/Fondo.jpg").getImage().getScaledInstance(600, 700, Image.SCALE_DEFAULT));

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                frame = new JFrame("Fernando Rios Chávez A01020706");
                
                
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                chooseFileButton = new JButton("Choose File");
                chooseFileButton.addActionListener(new FileListener());
                chooseFileButton.setBounds(40, 30, 150, 30);
            }
        });

        

    }

    public class FileListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            project.readFile();
        }
    }

    public class AcceptListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(project.file != null){
                //project.topDown(p)
            }
        } 
    }
    
}