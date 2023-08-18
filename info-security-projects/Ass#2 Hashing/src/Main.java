import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        OPS ops = new OPS();

        MainScreen mainScreen = new MainScreen();
        AccessScreen accessScreen = new AccessScreen();
        MessageScreen messageScreen = new MessageScreen();
        MessageRegisterScreen messageRegisterScreen = new MessageRegisterScreen();
        RegisterScreen registerScreen = new RegisterScreen();

        mainScreen.accessButtonAction(accessScreen);
        mainScreen.registerButtonAction(registerScreen);
        mainScreen.leaveMsgButtonAction(messageRegisterScreen);

        registerScreen.registerButtonAction(mainScreen,ops);
        registerScreen.homeButtonAction(mainScreen);

        messageRegisterScreen.createMessageButtonAction(mainScreen, ops);
        messageRegisterScreen.homeButtonAction(mainScreen);

        accessScreen.checkBoxAction();
        accessScreen.resetButtonAction();
        accessScreen.viewButtonAction(messageScreen,ops);
        accessScreen.homeButtonAction(mainScreen);
        messageScreen.returnButtonAction(accessScreen);

    }
}
