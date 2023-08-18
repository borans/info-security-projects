import com.sun.istack.internal.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AccessScreen extends JFrame {

    public JPanel mainPanel = new JPanel();
    public JPanel mainPanel2 = new JPanel();
    public JPanel mainPanel3 = new JPanel();

    public JLabel codeNameLabel = new JLabel("Message Codename");
    public JLabel msgPasswordLabel = new JLabel("Message Password");
    public JLabel usernameLabel = new JLabel("Username");
    public JLabel passwordLabel = new JLabel("User Password");

    public JTextField codeNameTextField = new JTextField();
    public JTextField msgPasswordTextField = new JTextField();
    public JTextField usernameTextField = new JTextField();
    public JPasswordField passwordTextField = new JPasswordField();

    GridBagConstraints gbc = new GridBagConstraints();
    GridBagConstraints gbc2 = new GridBagConstraints();

    public JCheckBox checkBox = new JCheckBox("Show Password");

    public JButton viewButton = new JButton("VIEW");
    public JButton resetButton = new JButton("RESET");
    public JButton homeButton = new JButton("HOME");

    public void viewButtonAction(MessageScreen messageScreen, OPS ops){
        this.viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String t = null;
                try {
                    t = ops.accessMessage(AccessScreen.this.codeNameTextField.getText(), AccessScreen.this.msgPasswordTextField.getText(),
                            AccessScreen.this.usernameTextField.getText(), new String(AccessScreen.this.passwordTextField.getPassword()));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if((t.equals("Incorrect message code or password.")) ||
                        (t.equals("Incorrect username or password.")) ){
                    JOptionPane.showMessageDialog(AccessScreen.this, t, "Dialog",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    messageScreen.setContent(t);
                    AccessScreen.this.setVisible(false);
                    messageScreen.setVisible(true);
                }
            }
        } );
    }

    public void resetButtonAction() {
        this.resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AccessScreen.this.codeNameTextField.setText("");
                AccessScreen.this.msgPasswordTextField.setText("");
                AccessScreen.this.passwordTextField.setText("");
                AccessScreen.this.usernameTextField.setText("");
            }
        });
    }

    public void homeButtonAction(MainScreen mainScreen) {
        this.homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AccessScreen.this.setVisible(false);
                mainScreen.setVisible(true);
            }
        });
    }

    public void checkBoxAction() {
        this.checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
                    AccessScreen.this.passwordTextField.setEchoChar((char) 0);
                } else {
                    AccessScreen.this.passwordTextField.setEchoChar((Character) UIManager.get("PasswordField.echoChar"));
                }
                ;
            }
        });
    }


    public void addToGrid(@NotNull java.awt.Component comp, int x, int y, GridBagConstraints gbc, JPanel panel){

        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(comp,gbc);
    }

    public AccessScreen() throws HeadlessException {

        this.setVisible(false);
        this.setContentPane(this.mainPanel);
        this.setSize(300,280);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Message View");

        //this.mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        this.mainPanel2.setLayout(new GridBagLayout());
        this.mainPanel3.setLayout(new GridBagLayout());

        this.mainPanel.add(this.mainPanel2);
        this.mainPanel.add(this.mainPanel3);

        this.gbc.anchor = GridBagConstraints.CENTER;
        this.gbc.insets = new Insets(3,3,3,3);
        this.gbc2.insets = new Insets(3,3,3,3);
        this.gbc2.anchor = GridBagConstraints.NORTH;


        this.codeNameTextField.setColumns(10);
        this.msgPasswordTextField.setColumns(10);
        this.usernameTextField.setColumns(10);
        this.passwordTextField.setColumns(10);

        addToGrid(codeNameLabel, 0, 0, this.gbc, this.mainPanel2);
        addToGrid(codeNameTextField, 1, 0, this.gbc, this.mainPanel2);
        addToGrid(msgPasswordLabel, 0, 1, this.gbc, this.mainPanel2);
        addToGrid(msgPasswordTextField, 1, 1, this.gbc, this.mainPanel2);
        addToGrid(usernameLabel, 0, 2, this.gbc, this.mainPanel2);
        addToGrid(usernameTextField, 1, 2, this.gbc, this.mainPanel2);
        addToGrid(passwordLabel, 0, 3, this.gbc, this.mainPanel2);
        addToGrid(passwordTextField, 1, 3, this.gbc, this.mainPanel2);
        addToGrid(checkBox, 1, 4, this.gbc, this.mainPanel2);

        addToGrid(viewButton, 0, 0, this.gbc2, this.mainPanel3);
        addToGrid(resetButton, 1, 0, this.gbc2, this.mainPanel3);
        this.gbc2.gridwidth = GridBagConstraints.REMAINDER;
        addToGrid(homeButton, 0, 1, this.gbc2, this.mainPanel3);

    }
}
