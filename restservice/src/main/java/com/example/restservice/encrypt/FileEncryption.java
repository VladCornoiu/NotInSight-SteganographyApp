package com.example.restservice.encrypt;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class FileEncryption {

    private static final int AES_KEY_SIZE = 256;
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int AES_BLOCK_SIZE = 16;

    public Cipher aesCipher;
    private KeyGenerator keyGenerator;
    public SecretKey secretKey;

    public FileEncryption() {
        try {
            aesCipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
            keyGenerator.init(AES_KEY_SIZE);

            secretKey = keyGenerator.generateKey();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[AES_BLOCK_SIZE];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        try {
            aesCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            aesCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey.getEncoded(), ENCRYPTION_ALGORITHM), ivParameterSpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(byte[] dataToEncrypt) {

        byte[] secretKeyByteArray = this.secretKey.getEncoded();
        int secretKeyByteArrayLength = secretKeyByteArray.length;
        byte[] ivParameterSpecByteArray = this.aesCipher.getIV();
        int ivParameterSpecByteArrayLength = ivParameterSpecByteArray.length;

        byte[] encryptedSecretFile = null;
        try {
            encryptedSecretFile = aesCipher.doFinal(dataToEncrypt);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        byte[] encrypted = new byte[secretKeyByteArrayLength + ivParameterSpecByteArrayLength + encryptedSecretFile.length];

        System.arraycopy(secretKeyByteArray, 0, encrypted, 0, secretKeyByteArrayLength);
        System.arraycopy(ivParameterSpecByteArray, 0, encrypted, secretKeyByteArrayLength, ivParameterSpecByteArrayLength);
        System.arraycopy(encryptedSecretFile, 0, encrypted, secretKeyByteArrayLength + ivParameterSpecByteArrayLength, encryptedSecretFile.length);
        return encrypted;
    }
    public byte[] decrypt(byte[] secretDataByteArray) {

        byte[] secretKey = Arrays.copyOfRange(secretDataByteArray, 0, AES_KEY_SIZE / 8);
        byte[] iv = Arrays.copyOfRange(secretDataByteArray, AES_KEY_SIZE / 8, AES_KEY_SIZE / 8 + AES_BLOCK_SIZE);
        byte[] secretData = Arrays.copyOfRange(secretDataByteArray, AES_KEY_SIZE / 8 + AES_BLOCK_SIZE, secretDataByteArray.length);

        byte[] secretFileData = new byte[secretData.length];

        try {
            aesCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, ENCRYPTION_ALGORITHM), new IvParameterSpec(iv));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            secretFileData = aesCipher.doFinal(secretData);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return secretFileData;
    }
}
