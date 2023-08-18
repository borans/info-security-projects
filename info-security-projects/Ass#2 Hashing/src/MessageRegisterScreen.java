import com.sun.istack.internal.NotNull;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

public class MessageRegisterScreen extends JFrame {

    public JPanel mainPanel = new JPanel();
    public JPanel mainPanel2 = new JPanel();
    public JPanel mainPanel3 = new JPanel();
    public JPanel mainPanel4 = new JPanel();

    public JLabel usernameLabel = new JLabel("Auth.username*");
    public JLabel passwordLabel = new JLabel("Password*");
    public JLabel confirmPasswordLabel = new JLabel("Confirm Password*");
    public JLabel codenameLabel = new JLabel("Message Codename*");
    public JLabel messageLabel = new JLabel("ENTER YOUR MESSAGE*");

    public JComboBox<String> userNameComboBox = new JComboBox<>();
    public JTextField passwordTextField = new JTextField();
    public JTextField confirmPasswordTextField = new JTextField();
    public JTextField codenameTextField = new JTextField();
    public JTextArea messageTextField = new JTextArea();

    public JButton createMessageButton = new JButton("Create Message");
    public JButton homeButton = new JButton("HOME");

    FileOP fileOP = new FileOP();

    public void homeButtonAction(MainScreen mainScreen){
        this.homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                MessageRegisterScreen.this.setVisible(false);
                mainScreen.setVisible(true);
            }
        });
    }

    public void createMessageButtonAction(MainScreen mainScreen,OPS ops){
        this.createMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    String s = ops.leaveMessage(MessageRegisterScreen.this.codenameTextField.getText(), MessageRegisterScreen.this.passwordTextField.getText(),
                            MessageRegisterScreen.this.confirmPasswordTextField.getText(), MessageRegisterScreen.this.messageTextField.getText(),
                            Objects.requireNonNull(MessageRegisterScreen.this.userNameComboBox.getSelectedItem()).toString());
                    if(!(s.equals(""))){
                        JOptionPane.showMessageDialog(MessageRegisterScreen.this, s, "Dialog",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    else{
                        MessageRegisterScreen.this.setVisible(false);
                        mainScreen.setVisible(true);
                        JOptionPane.showMessageDialog(mainScreen, "Message left successfully", "Dialog",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    GridBagConstraints gbc = new GridBagConstraints();

    public String[] strList = giveUsers();

    public void addToGrid(@NotNull java.awt.Component comp, int x, int y, GridBagConstraints gbc, JPanel panel){

        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(comp,gbc);
    }

    public String[] giveUsers() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        String[] users = new String[10];
        int idx=0;
        String decrypted = fileOP.decryptFile(Paths.get(fileOP.USER_PATH));
        String[] lines = decrypted.split("\n");
        for(String line : lines){
            String[] userAttr = line.split(" - ");
            users[idx] = userAttr[0];
            idx++;
        }
        return users;
    }


    public void syncData() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        this.strList = giveUsers();
        this.userNameComboBox.setModel(new DefaultComboBoxModel<String>(this.strList));
    }

    public MessageRegisterScreen() throws HeadlessException, IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        this.setVisible(false);
        this.setContentPane(this.mainPanel);
        this.setSize(700,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Register Form");

        this.mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));

        this.mainPanel2.setLayout(new GridBagLayout());
        this.mainPanel3.setLayout(new GridBagLayout());
        this.mainPanel4.setLayout(new GridBagLayout());
        this.mainPanel.add(this.mainPanel2);
        this.mainPanel.add(this.mainPanel3);
        this.mainPanel.add(this.mainPanel4);

        this.codenameTextField.setColumns(10);
        this.messageTextField.setColumns(30);
        this.messageTextField.setRows(10);
        this.messageTextField.setBorder(new BasicBorders.FieldBorder(Color.black,Color.black,Color.black,Color.black));
        JScrollPane scroll = new JScrollPane(this.messageTextField);
        scroll.setSize( 100, 100 );
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.confirmPasswordTextField.setColumns(10);
        this.passwordTextField.setColumns(10);
        this.userNameComboBox.setPreferredSize(new Dimension(115,20));

        this.gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        this.gbc.insets = new Insets(10,10,10,10);

        addToGrid(usernameLabel, 0, 0, this.gbc, this.mainPanel2);
        addToGrid(userNameComboBox, 1, 0, this.gbc, this.mainPanel2);
        addToGrid(passwordLabel, 0, 1, this.gbc, this.mainPanel2);
        addToGrid(passwordTextField, 1, 1, this.gbc, this.mainPanel2);
        addToGrid(confirmPasswordLabel, 2, 1, this.gbc, this.mainPanel2);
        addToGrid(confirmPasswordTextField, 3, 1, this.gbc, this.mainPanel2);
        addToGrid(codenameLabel, 0, 2, this.gbc, this.mainPanel2);
        addToGrid(codenameTextField, 1, 2, this.gbc, this.mainPanel2);

        this.gbc.gridwidth = 2;
        addToGrid(messageLabel, 0, 3, this.gbc, this.mainPanel2);
        addToGrid(scroll, 1, 3, this.gbc, this.mainPanel2);

        this.gbc.anchor = GridBagConstraints.CENTER;
        addToGrid(createMessageButton, 0, 4, this.gbc, this.mainPanel2);
        addToGrid(homeButton, 2, 4, this.gbc, this.mainPanel2);
    }
}
