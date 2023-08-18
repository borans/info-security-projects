import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileOP {
    //command line argument input filename
    String inputFileName;

    //command line argument output filename
    String outputFileName;

    //command line argument key filename
    String keyFileName;

    //command line argument if encryption write as enc is decryption write as dec
    String isEncryption;

    //command line argument if it is 3DES or DES
    String desType;

    //command line argument mode type
    String modeType;

    //initialization of the file operations and the parsing of the command string
    public FileOP(String[] args) {
        String encryptionOrDecryption = "dec";
        if(args[1].equals("-e")){
            encryptionOrDecryption = "enc";
        }
        this.fileMaker(args[3], args[5], args[8], encryptionOrDecryption, args[6], args[7]);
    }

    public void fileMaker(String inputFileName, String outputFileName, String keyFileName,
                          String isEncryption, String desType, String modeType) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.keyFileName = keyFileName;
        this.isEncryption = isEncryption;
        this.desType = desType;
        this.modeType = modeType;
        new File(outputFileName);
    }

    //adding padding to the plaintext
    public byte[] addPadding(byte[] array){

        //remainder of the plaintext
        int remainder = array.length % 8;
        //if it is 0 the padding is not necessary
        if(remainder == 0){
            return array;
        }
        //byte array initialization is equal to 0x00 valued byte array
        byte[] newArrayWithPadding = new byte[array.length + 8 - remainder];
        //0x80 byte added to end of the array
        newArrayWithPadding[array.length] = (byte) 0x80;
        //remaining part of the plain text is coppied
        System.arraycopy(array, 0, newArrayWithPadding, 0, array.length);
        return newArrayWithPadding;
    }

    public byte[] removePadding(byte[] array){

        int remainder;
        //find the index of the padding starting point
        for(remainder = 0; remainder < 8 ; remainder++){
            if(array[array.length - 1 - remainder] == (byte) 0x80 ){
                break;
            }
        }
        //if the other bytes are equal to 0x00
        for(int j = 0; j < remainder ; j++){
            if(array[array.length - 1 - j] != (byte) 0x00){
                return array;
            }
        }
        byte[] newArrayWithoutPadding = new byte[array.length - remainder - 1 ];
        //delete the padding bytes at the end
        System.arraycopy(array, 0, newArrayWithoutPadding, 0, array.length - remainder - 1);
        return newArrayWithoutPadding;
    }


    public long finalizeOperation() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException{

        byte[] plainTextOrCipherText = Files.readAllBytes(Paths.get(this.inputFileName));
        plainTextOrCipherText = addPadding(plainTextOrCipherText);

        //if encryption the encrypted byte array
        //if decryption the decrypted byte array
        byte[] output;

        //to read key file
        File file = new File(this.keyFileName);
        Scanner sc = new Scanner(file);
        String keyText = sc.nextLine();
        byte[] IVOrNonce = keyText.split(" - ")[0].getBytes();
        if(this.modeType.equals("CTR")){
            IVOrNonce = keyText.split(" - ")[2].getBytes();
        }
        String key = keyText.split(" - ")[1];

        //execution time
        long start = System.nanoTime();

        //the method is called by the FileOP Object's attributes

        //if DES
        Method method = DES.class.getDeclaredMethod(this.isEncryption.charAt(0) + this.modeType, byte[].class, String.class, byte[].class);
        //if 3DES
        if(this.desType.equals("3DES")){
            method = TripleDES.class.getDeclaredMethod(this.isEncryption.charAt(0) + this.modeType, byte[].class, String.class, String.class, byte[].class);
            output = (byte[]) (byte[]) method.invoke(this, plainTextOrCipherText, key, key, IVOrNonce );
            //padding removal
            output = removePadding(output);
            //decrypted or encrypted tex is written
            Files.write(Paths.get(this.outputFileName), output);
            long elapsedTime = System.nanoTime() - start;
            //time returned
            return elapsedTime;
        }
        output = (byte[]) method.invoke(this, plainTextOrCipherText, key, IVOrNonce );
        //padding removal
        output = removePadding(output);
        //decrypted or encrypted tex is written
        Files.write(Paths.get(this.outputFileName), output);
        long elapsedTime = System.nanoTime() - start;
        //time returned
        return elapsedTime;
    }
}
