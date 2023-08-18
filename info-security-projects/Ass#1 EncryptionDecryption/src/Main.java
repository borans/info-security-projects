import java.io.FileWriter;

public class Main {
    public static void main(String[] args) throws Exception {

        //object is intialized for the operations with the command line argument
        FileOP fileOperator = new FileOP(args);

        //the operations are done and execution time is calculated.
        long elapsedTime = fileOperator.finalizeOperation();

        //log file is written
        FileWriter logFileWriter = new FileWriter("run.log",true);
        logFileWriter.write(fileOperator.inputFileName + " " + fileOperator.outputFileName + " " + fileOperator.isEncryption + " " +
                fileOperator.desType + " " + fileOperator.modeType + " " + elapsedTime / 1000000 + "\n");
        logFileWriter.close();
    }
}
