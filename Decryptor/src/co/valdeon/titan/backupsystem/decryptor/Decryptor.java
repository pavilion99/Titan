package co.valdeon.titan.backupsystem.decryptor;

import org.jasypt.util.binary.BasicBinaryEncryptor;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Decryptor {

    public static void main(String[] args) {
        System.out.println("Decryptor initialized.");
        System.out.println("Please type a complete path to the directory containing files that you wish to be decrypted.");
        Console c = System.console();
        File dir = new File(c.readLine());
        while(!dir.exists()) {
            System.out.println(dir.getAbsolutePath() + " does not exist!");
            System.out.println("Please type a complete path to the directory containing files that you wish to be decrypted.");
            dir = new File(c.readLine());
        }
        while(!dir.isDirectory()) {
            System.out.println(dir.getAbsolutePath() + " is not a directory!");
            System.out.println("Please type a complete path to the directory containing files that you wish to be decrypted.");
            dir = new File(c.readLine());
        }
        System.out.println("Please enter the password with which you wish to decrypt the data");
        String password = new String(c.readPassword());
        System.out.println("Data decryption begun on directory " + dir.getAbsolutePath());
        encrypt(dir, password);
    }

    private static void encrypt(File a, String password) {
        assert a.isDirectory();
        for(File b : a.listFiles()) {
            if(b.isDirectory()) {
                encrypt(b, password);
            }else{
                BasicBinaryEncryptor bbe = new BasicBinaryEncryptor();
                bbe.setPassword(password);
                if(runCryptoAlg(b, bbe)) {
                    System.out.println("Decrypted: " + b.getAbsolutePath());
                } else {
                    System.out.println("Decryption failed: " + b.getAbsolutePath());
                }
            }
        }
    }

    private static boolean runCryptoAlg(File a, BasicBinaryEncryptor b) {
        try {
            if(getExtension(a).equals(".enc")) {
                byte[] bytes = Files.readAllBytes(a.toPath());
                byte[] encryptedFile = b.decrypt(bytes);
                FileOutputStream fos = new FileOutputStream(new File(replaceLast(a.getAbsolutePath())));
                fos.write(encryptedFile);
                fos.close();
                return a.delete();
            }else {
                System.out.println(a.getAbsolutePath() + "'s extension isn't .enc, skipping");
                return false;
            }
        } catch(IOException e) {
            System.out.println("Warning: File " + a.getAbsolutePath() + " does not exist; skipping encryption");
        }
        return false;
    }

    private static String getExtension(File a) {
        String name = a.getName();
        try {
            return name.substring(name.lastIndexOf("."));
        } catch (Exception e) {
            return "";
        }
    }

    private static String replaceLast(String s){
        String substring = ".enc";
        String replacement = "";
        int index = s.lastIndexOf(substring);
        if (index == -1)
            return s;
        return s.substring(0, index) + replacement + s.substring(index+substring.length());
    }

}
