import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>
 * This class contains the graphical interface and the front-end components
 * of the project. It contains a Project object that contains all the 
 * back-end functionality. 
 *
 * @author Fernando Rios Chavez
 * @see JButton
 * @see JLabel
 * @see JTextField
 * @see ImageIcon
 * @see JFrame
 * @see Project
 */
public class GUI {

    public JButton chooseFileButton, processStringButton;
    public JLabel fileChosenLabel, acceptedStringLabel, backgroundLabel, stitchLabel;
    public JTextField stringTextField;
    public ImageIcon backgroundImage, acceptedStitchImage, rejectedStitchImage;
    private JFrame frame;

    /**
     * The project object that contains all functionality. 
     */
    private Project project;

    public GUI(){

        project = new Project();
        backgroundImage = new ImageIcon(new ImageIcon("images/Fondo.jpg").getImage().getScaledInstance(400, 400, Image.SCALE_DEFAULT));
        acceptedStitchImage = new ImageIcon(new ImageIcon("images/acceptedstitch.png").getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));
        rejectedStitchImage = new ImageIcon(new ImageIcon("images/rejectedstitch.png").getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                
                /**
                  * The panel contains all the graphical components. 
                  */
                Panel panel = new Panel();
                frame = new JFrame("Fernando Rios Chávez A01020706");
                
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
                frame.add(panel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        });
    }

    /**
     * <p> When performed, will run the project's readFile method, and
     * display the file path in a label in the GUI
     * @see Project
     */
    public class FileListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            project.readFile();
            fileChosenLabel.setText(project.file.toString());
        }
    }

    /**
     * <p> If there is a file selected, will perform the top-down function
     * on the string in the text field area. Otherwise, it will run the project's
     * readFile method. 
     * @see Project
     */
    public class AcceptListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(project.file != null){
                if(project.topDown(stringTextField.getText())){
                    acceptedStringLabel.setText("Accepted");
                    acceptedStringLabel.setForeground(Color.blue);
                    stitchLabel.setIcon(acceptedStitchImage);
                }
                else{
                    acceptedStringLabel.setText("Rejected");
                    acceptedStringLabel.setForeground(Color.red);
                    stitchLabel.setIcon(rejectedStitchImage);
                }
            }
            else{
                project.readFile();
                fileChosenLabel.setText(project.file.toString());
            }
        } 
    }

    /**
     * <p> This class contains all the graphical components and will initailize
     * them with its constructor. The positions and listener will be implemented 
     * in this constructor.  
     * @see FileListener
     * @see AcceptListener
     * @see JLayeredPane
     */
    public class Panel extends JLayeredPane {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public Panel() {
            backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setBounds(0,0,400,400);

            chooseFileButton = new JButton("Choose File");
            chooseFileButton.setFont(new Font(chooseFileButton.getFont().getName(), Font.PLAIN, 14));
            chooseFileButton.addActionListener(new FileListener());
            chooseFileButton.setBounds(120, 20, 160, 30);
            
            fileChosenLabel = new JLabel("File chosen: None", SwingConstants.CENTER);
            fileChosenLabel.setBounds(40, 50, 320, 30);
            fileChosenLabel.setFont(new Font(fileChosenLabel.getFont().getName(), Font.PLAIN, 11));

            stringTextField = new JTextField(240);
            stringTextField.setBounds(80, 80, 240, 30);
            stringTextField.setFont(new Font(stringTextField.getFont().getName(), Font.PLAIN, 12));
            
            processStringButton = new JButton("Verify String");
            processStringButton.addActionListener(new AcceptListener());
            processStringButton.setBounds(120, 120, 160, 30);
            processStringButton.setFont(new Font(processStringButton.getFont().getName(), Font.PLAIN, 14));

            acceptedStringLabel = new JLabel("",SwingConstants.CENTER);
            acceptedStringLabel.setBounds(150, 160, 100, 30);
            acceptedStringLabel.setFont(new Font(acceptedStringLabel.getFont().getName(), Font.PLAIN, 16));
            

            stitchLabel = new JLabel();
            stitchLabel.setBounds(110, 200, 180, 180);

            add(chooseFileButton, 1);
            add(fileChosenLabel, 1);
            add(stringTextField, 1);
            add(processStringButton, 1);
            add(acceptedStringLabel, 1);
            add(stitchLabel, 1);
            add(backgroundLabel, -1000);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400, 400);
        }
     
    }
}





    
