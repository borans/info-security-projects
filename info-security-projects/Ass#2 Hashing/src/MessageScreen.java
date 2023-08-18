import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageScreen extends JFrame {
    public JPanel mainPanel = new JPanel();

    public JTextArea textArea = new JTextArea();
    public JButton returnButton = new JButton("Return");

    GridBagConstraints gbc = new GridBagConstraints();

    public void returnButtonAction(AccessScreen accessScreen) {
        this.returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MessageScreen.this.setVisible(false);
                accessScreen.setVisible(true);
            }
        });
    }

    public MessageScreen() throws HeadlessException {

      

        this.setVisible(false);
        this.setContentPane(this.mainPanel);
        this.setSize(300,280);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Message");
        
        this.textArea.setEditable(false);

        this.returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(this.textArea);
        scroll.setSize( 100, 100 );
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        this.mainPanel.add(scroll);
        this.mainPanel.add(this.returnButton);
    }
    
     public void setContent(String t){
        this.textArea.setText(t);
    }
    
}
