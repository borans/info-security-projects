import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterScreen extends JFrame {

    public JPanel mainPanel = new JPanel();
    public JTextField username = new JTextField();
    public JLabel usernameLabel = new JLabel("Username: ");
    public JTextField password = new JTextField();
    public JLabel passwordLabel = new JLabel("Password: ");
    public JButton registerButton = new JButton("Register");
    public JButton homeButton = new JButton("HOME");

    public void registerButtonAction(MainScreen mainScreen , OPS ops){
        this.registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    if(!(ops.registerUser(RegisterScreen.this.username.getText(),RegisterScreen.this.password.getText()))){
                        JOptionPane.showMessageDialog(RegisterScreen.this, "Username already exists!", "Dialog",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        RegisterScreen.this.setVisible(false);
                        mainScreen.setVisible(true);
                        JOptionPane.showMessageDialog(mainScreen, "User registered successfully", "Dialog",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } );
    }

    public void homeButtonAction(MainScreen mainScreen){
        this.homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                RegisterScreen.this.setVisible(false);
                mainScreen.setVisible(true);
            }
        } );
    }


    public RegisterScreen() throws HeadlessException {
        this.setVisible(false);
        this.setContentPane(this.mainPanel);
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("User Register Page");

        this.mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(3,3,3,3);

        this.username.setColumns(10);
        this.password.setColumns(10);

        this.mainPanel.add(usernameLabel,gbc);
        this.mainPanel.add(username,gbc);
        this.mainPanel.add(passwordLabel,gbc);
        this.mainPanel.add(password,gbc);
        this.mainPanel.add(registerButton,gbc);
        this.mainPanel.add(homeButton,gbc);
    }

}
