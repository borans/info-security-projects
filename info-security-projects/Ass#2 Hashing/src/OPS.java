import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

public class OPS {

    public final FileOP fileOP = new FileOP();
    public final Path pathUser = Paths.get(fileOP.USER_PATH);
    public final Path pathMsg = Paths.get(fileOP.MESSAGE_PATH);

    // create users for user database
    public boolean registerUser(String username, String password) throws Exception {
        if(checkUserExistance(username)){
            return false;
        }

        String passwordHash = hashed(password);
        String decryptedText = fileOP.decryptFile(pathUser);
        decryptedText = decryptedText + username + " - " + passwordHash + "\n";
        Files.write(pathUser,fileOP.encryptFile(decryptedText));
        return true;
    }


    /**
     *
     * @param pw password string that is to be hashed
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String hashed(String pw) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(pw.getBytes());
        //convert bytearray to signum
        BigInteger  sgn = new BigInteger(1, messageDigest);
        // convert to hex value
        String hash = sgn.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        return hash;
    }



    // -----------------------------MESSAGE ACCESS-------------------------------------------------------------------

    public String accessMessage(String mID, String messagePassword, String username, String userPassword) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String hashedMPassword = hashed(messagePassword);
        String hashedUPassword = hashed(userPassword);
        String content;
        if(checkUser(username, hashedUPassword)){
            content = showMessage(mID, hashedMPassword, username);
            if(content != null){
                return content;
            }else{
                return "Incorrect message code or password.";
            }
        }else{
            return "Incorrect username or password.";
        }
    }


    public String showMessage(String mID, String mPassw, String username) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        String decrypted = fileOP.decryptFile(pathMsg);
        String[] lines = decrypted.split("\n");
        for(String line : lines){
            String[] messageAttr = line.split(" - ");
            if(messageAttr[0].equals(mID) && messageAttr[1].equals(mPassw) && messageAttr[3].equals(username)){
                String plainMessageText = fileOP.decrypt(messageAttr[2]);
                return plainMessageText;
            }
        }
        return null;
    }



    // checks if user informations matches with the input strings.
    public boolean checkUser(String username, String password) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        String decrypted = fileOP.decryptFile(pathUser);
        String[] lines = decrypted.split("\n");
        for(String line : lines){
            String[] userAttr = line.split(" - ");
            if(userAttr[0].equals(username) && userAttr[1].equals(password)){
                return true;
            }
        }
        return false;
    }

    public boolean checkUserExistance(String username) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        String decrypted = fileOP.decryptFile(pathUser);
        String[] lines = decrypted.split("\n");
        for(String line : lines){
            String[] userAttr = line.split(" - ");
            if(userAttr[0].equals(username)){
                return true;
            }
        }
        return false;
    }


    //-----------------------------------------LEAVE MESSAGE TO THE SYSTEM------------------------------------------

    /**
     *
     * @param mID message codename
     * @param passwordAttmpt1 login password
     * @param passwordAttmpt2 login again
     * @param content message content
     * @param username authorized username
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public String leaveMessage(String mID, String passwordAttmpt1, String passwordAttmpt2, String content, String username) throws Exception {

        // check if the typed passwords are same
        if(!passwordAttmpt1.equals(passwordAttmpt2)){
            return "Warning. Passwords are not match.";
        }else{
            // check if id is distinct number in dataset.
            if(isDistinctID(mID)){
                String hashedPass = hashed(passwordAttmpt1);
                User authUser = createUser(username);
                //creating message object
                String decodeString = fileOP.encrypt(content);
                Message message = new Message(mID, decodeString, hashedPass, authUser);

                // adding new message to database
                addNewMessage(message);
                return "";
            }else{
                return "Warning. Please enter a different codename.";
            }
        }
    }

    public void addNewMessage(Message m) throws Exception {

        String decryptedMessage = fileOP.decryptFile(pathMsg);
        decryptedMessage += m.getMessage_id() + " - " + m.getPassword() + " - " + m.getContent() + " - " + m.getUser().getUsername() + "\n";
        byte[] encrypted = fileOP.encryptFile(decryptedMessage);
        Files.write(pathMsg,encrypted);
    }

    /**
     *
     * @param username
     * @return
     * @throws FileNotFoundException
     */
    public User createUser(String username) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        User user = null;
        String decrypted = fileOP.decryptFile(pathUser);
        String[] lines = decrypted.split("\n");
        for(String line : lines){
            String[] properties = line.split(" - ");
            if(properties[0].equals(username)){
                user = new User(username, properties[1]);
                break;
            }
        }
        return user;
    }


    /**
     *
     * @param mID typed id
     * @return
     */
    public boolean isDistinctID(String mID) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        String decrypted = fileOP.decryptFile(pathMsg);
        String[] lines = decrypted.split("\n");
        for(String line : lines){
            String[] properties = line.split(" - ");
            if(properties[0].equals(mID)){
                return false;
            }
        }
        return true;
    }
}
