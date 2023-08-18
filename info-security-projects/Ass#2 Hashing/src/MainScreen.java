import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame{

    public JPanel mainPanel = new JPanel();

    public JButton accessButton = new JButton();
    public JButton leaveMsgButton = new JButton();
    public JButton registerButton = new JButton();

    public JLabel mainLabel = new JLabel();

    public void accessButtonAction(AccessScreen accessScreen){
        this.accessButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainScreen.this.setVisible(false);
                accessScreen.setVisible(true);
            }
        } );
    }
    public void leaveMsgButtonAction(MessageRegisterScreen messageRegisterScreen) {
        this.leaveMsgButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    messageRegisterScreen.syncData();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                MainScreen.this.setVisible(false);
                messageRegisterScreen.setVisible(true);
            }
        });
    }
    public void registerButtonAction(RegisterScreen registerScreen) {
        this.registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainScreen.this.setVisible(false);
                registerScreen.setVisible(true);
            }
        });
    }

    public MainScreen() throws HeadlessException {
        this.setVisible(true);
        this.setContentPane(this.mainPanel);
        this.setSize(500,400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Main Page");

        this.mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.mainPanel.setLayout(new GridBagLayout());

        this.accessButton.setText("Access");
        this.accessButton.setFont(new Font("Arial", Font.BOLD, 20));


        this.leaveMsgButton.setText("Leave the  Message");
        this.leaveMsgButton.setFont(new Font("Arial", Font.BOLD, 20));

        this.registerButton.setText("Register");
        this.registerButton.setFont(new Font("Arial", Font.BOLD, 20));

        this.mainLabel.setText("Welcome to MessageBox");
        this.mainLabel.setFont(new Font("Arial", Font.BOLD, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(3,3,3,3);

        this.mainPanel.add(mainLabel,gbc);
        this.mainPanel.add(accessButton,gbc);
        this.mainPanel.add(leaveMsgButton,gbc);
        this.mainPanel.add(registerButton,gbc);

    }
}
