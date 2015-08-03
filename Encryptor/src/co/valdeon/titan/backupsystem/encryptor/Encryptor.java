package co.valdeon.titan.backupsystem.encryptor;

import org.jasypt.util.binary.BasicBinaryEncryptor;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Encryptor {

    public static void main(String[] args) {
        System.out.println("Encryptor initialized.");
        System.out.println("Please type a complete path to the directory containing files that you wish to be encrypted.");
        Console c = System.console();
        File dir = new File(c.readLine());
        while(!dir.exists()) {
            System.out.println(dir.getAbsolutePath() + " does not exist!");
            System.out.println("Please type a complete path to the directory containing files that you wish to be encrypted.");
            dir = new File(c.readLine());
        }
        while(!dir.isDirectory()) {
            System.out.println(dir.getAbsolutePath() + " is not a directory!");
            System.out.println("Please type a complete path to the directory containing files that you wish to be encrypted.");
            dir = new File(c.readLine());
        }
        System.out.println("Please enter the password with which you wish to encrypt the data");
        String password = new String(c.readPassword());
        System.out.println("Data encryption begun on directory " + dir.getAbsolutePath());
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
                    System.out.println("Encrypted: " + b.getAbsolutePath());
                } else {
                    System.out.println("Encryption failed: " + b.getAbsolutePath());
                }
            }
        }
    }

    private static boolean runCryptoAlg(File a, BasicBinaryEncryptor b) {
        try {
            byte[] bytes = Files.readAllBytes(a.toPath());
            byte[] encryptedFile = b.encrypt(bytes);
            FileOutputStream fos = new FileOutputStream(new File(a.getAbsolutePath() + ".enc"));
            fos.write(encryptedFile);
            fos.close();
            return a.delete();
        } catch(IOException e) {
            System.out.println("Warning: File " + a.getAbsolutePath() + " does not exist; skipping encryption");
        }
        return false;
    }

}
