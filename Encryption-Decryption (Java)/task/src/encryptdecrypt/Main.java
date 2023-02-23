package encryptdecrypt;
import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputType = getMode(args);
        String inputString = getData(args);
        int key = Integer.parseInt(getKey(args));
        String algoType = getAlgoType(args);
        Encryptor encryptor = null;
        switch (inputType){
            case "dec":

                key = key - (2 * key);
                if ("unicode".equals(algoType)) {

                    encryptor = new Encryptor(new UnicodeEncryption());
                } else {

                    encryptor = new Encryptor(new ShiftEncryption());
                }
                break;
            case "enc":
                if ("unicode".equals(algoType)) {

                    encryptor = new Encryptor(new UnicodeEncryption());
                } else {

                    encryptor = new Encryptor(new ShiftEncryption());
                }
                break;
        }
        writeData(args, String.valueOf(encryptor.encrypt(inputString,key)));


    }
    public static String getAlgoType(String[] args) {
        String algo = "";
        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-alg")){
                algo = args[i + 1];
            }
        }
        return algo;
    }
    public static String getMode(String[] args) {

        String modeType= "";
        for (int i = 0;i < args.length; i++){
            if (args[i].equals("-mode")){
                modeType = args[i + 1];
            }
        }
        return modeType;
    }
    public static String getData(String[] args) throws FileNotFoundException {
        String input = "";
        for (int i = 0; i < args.length;i++){
            if (args[i].equals("-data")){
                input = args[i + 1];
                break;
            }
            else if(args[i].equals("-in")){
                input = getfromFile(args);
                //System.out.println(input);

            }

        }
        return input;
    }
    public static String getKey(String [] args) {
        String key = "0";
        for (int i = 0; i< args.length;i++) {
            if (args[i].equals("-key")){
                key = args[i + 1];
            }
        }
        return key;
    }
    public static String getfromFile(String[] args) throws FileNotFoundException {
        String content = "";


        for(int i = 0; i < args.length;i++) {
            if (args[i].equals("-in")){
                File file = new File(args[i +1]);
                try (Scanner scanFile = new Scanner(file)){
                    while(scanFile.hasNext()){

                        content = scanFile.nextLine();
                    }

                    //System.out.println(content);

                }catch (IOException e){
                    System.out.println("File " +args[i +1] + " not found!");

                }

            }
        }
        return content;
    }
    //write data algo
    public static void writeData(String[] args, String content) throws IOException {
        boolean b = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-out")) {

                File file = new File(args[i + 1]);
                try (FileWriter fileWriter = new FileWriter(file, false)){
                    fileWriter.write(content);
                    b = true;

                }catch (IOException e) {
                    System.out.println("File " +args[i +1] + " not found!");

                }

            }

        }
        if (!b){
            System.out.println(content);
        }
    }

}
class Encryptor {
    private EncryptionStrategy strategy;
    public Encryptor(EncryptionStrategy strategy) {
        this.strategy = strategy;
    }
    public StringBuilder encrypt(String s, int key) {
        return strategy.encrypt(s,key);
    }


}
interface EncryptionStrategy{
    StringBuilder encrypt(String s, int key);

}
class UnicodeEncryption implements EncryptionStrategy{

    @Override
    public StringBuilder encrypt(String s, int key) {
        //System.out.println("starting unicode");
        StringBuilder cipherString = new StringBuilder();
        for (int i = 0;i < s.length(); i++){
            cipherString.append(enc(s.charAt(i),key));
        }
        return cipherString;
    }
    public static char enc(char a, int key){
        a = (char) (a + key);
        return a;
    }
}
class ShiftEncryption implements EncryptionStrategy{

    @Override
    public StringBuilder encrypt(String s, int key) {
        System.out.println("Starting shift algo");
        System.out.println(key);
        System.out.println(s);
        StringBuilder cipherString = new StringBuilder();
        char[] smallChar = formArray('a');


        char[] largeChar = formArray('A');

        for (int i = 0; i < s.length(); i++ ){
            if (s.charAt(i) >= '\u0041' && s.charAt(i) <= '\u005A'){


                cipherString.append(shift(largeChar, s.charAt(i), key));

            }
            else if (s.charAt(i) >= '\u0061' && s.charAt(i) <= '\u007A'){

                cipherString.append(shift(smallChar, s.charAt(i), key));
                System.out.println(cipherString);

            }
            else {

                cipherString.append(s.charAt(i));

            }
        }

        return cipherString;
    }
    public static char[] formArray(char min) {
        char[] arrChar = new char[26];
        for (int i = 0; i < arrChar.length; i++ ){
            arrChar[i] = (char) (min + i);
        }
        return arrChar;
    }
    public static char shift(char[] charArr, char a, int key){

        int count =0;


        //System.out.println(charArr);
        //searching loop

        for (int i = 0; i < charArr.length;i++){
            if (a == charArr[i]){
                count = i;
                 if (key < 0){
                     for (int j = 25; j > 25 + key; j--){

                         if (count == 0){
                             count = 25;
                         }else{
                             count --;
                             System.out.println(count);
                         }
                     }
                 }
                 else{
                     for (int j = 0; j < key; j++){

                         if (count == 25){
                             count = 0;
                         }else{
                             count ++;
                         }
                     }
                 }


            }
        }
        //System.out.println(charArr[count]);
        return charArr[count];
    }
}