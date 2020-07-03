package com.example.restservice.decoder;
import com.decoder.HuffmanDecoder;
import com.example.restservice.encrypt.FileEncryption;
import com.example.restservice.model.DCTCoefficient;
import com.example.restservice.model.SecretData;

import java.io.*;

public class SecretFileDecoder {

    private static byte[] carrierByteArray;
    private static int[] dctCoefficients;

    private static byte[] deZigZag = {
            0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13, 16, 26, 29, 42, 3, 8, 12, 17, 25, 30, 41, 43, 9, 11, 18, 24, 31,
            40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51, 55, 60, 21, 34, 37, 47, 50, 56, 59, 61,
            35, 36, 48, 49, 57, 58, 62, 63};

    public static SecretData extract(final InputStream fis, final int fileLength) throws IOException {
        carrierByteArray = new byte[fileLength];
        fis.read(carrierByteArray);
        HuffmanDecoder hd = new HuffmanDecoder(carrierByteArray);

        System.out.println("Decode");
        dctCoefficients = hd.decode();
        unzigzag(dctCoefficients);

        int coeffCount = dctCoefficients.length;

        // decode

        int bytesDecoded = 0;
        int coeffIndex = 0;
        int extractedBitsNumber = 0;
        int extractedBit;
        int extractedByte = 0;
        int dataLength = 0;
        byte[] fileLengthByteArray = new byte[4];
        byte[] secretDataByteArray = null;

        boolean hasToDecode = true;
        while (hasToDecode) {
            int square = 4;
            int squareRoot = 2;
            int coefficientsLeft = coeffCount - coeffIndex;

            if (coefficientsLeft < 4) {
                break;
            }
            while (square < coefficientsLeft) {
                square = squareRoot * squareRoot;
                squareRoot++;
            }
            if (square != coefficientsLeft) { // too much and not equal
                squareRoot -= 2;
                square = squareRoot * squareRoot;
            }
            int matrixSize = squareRoot;
            int matrixIndex = 0;
            DCTCoefficient[][] dctCoefficients = new DCTCoefficient[squareRoot][squareRoot];
            while (matrixIndex < matrixSize - 1) {
                for (int i = matrixIndex; i < matrixSize - 1; i++)
                    dctCoefficients[i][matrixIndex] = new DCTCoefficient(SecretFileDecoder.dctCoefficients[coeffIndex], coeffIndex++);
                for (int i = matrixIndex; i < matrixSize - 1; i++)
                    dctCoefficients[matrixSize - 1][i] = new DCTCoefficient(SecretFileDecoder.dctCoefficients[coeffIndex], coeffIndex++);
                for (int i = matrixSize - 1; i > matrixIndex; i--)
                    dctCoefficients[i][matrixSize - 1] = new DCTCoefficient(SecretFileDecoder.dctCoefficients[coeffIndex], coeffIndex++);
                for (int i = matrixSize - 1; i > matrixIndex; i--)
                    dctCoefficients[matrixIndex][i] = new DCTCoefficient(SecretFileDecoder.dctCoefficients[coeffIndex], coeffIndex++);

                matrixIndex++;
                matrixSize--;
            }

            if (matrixIndex == matrixSize - 1)
                dctCoefficients[matrixIndex][matrixIndex] = new DCTCoefficient(SecretFileDecoder.dctCoefficients[coeffIndex], coeffIndex++);

            boolean breakFlag = false;
            for (int i = 0; i < squareRoot && !breakFlag; ++i) {
                for (int j = 0; j < squareRoot; ++j) {

                    int index = dctCoefficients[i][j].getIndex();
                    int value = dctCoefficients[i][j].getValue();

                    if (index % 64 == 0) {
                        continue;
                    }
                    if (value == 0) {
                        continue;
                    } else {
                        extractedBit = SecretFileDecoder.dctCoefficients[index] & 1;
                        extractedByte |= extractedBit << extractedBitsNumber++;
                    }

                    if (extractedBitsNumber == 8) {
                        bytesDecoded++;
                        if (bytesDecoded >= 1 && bytesDecoded <= 4) {
                            fileLengthByteArray[bytesDecoded - 1] = (byte) extractedByte;
                            extractedByte = 0;
                            extractedBitsNumber = 0;
                            if (bytesDecoded == 4) {
                                dataLength = (fileLengthByteArray[0] & 0xFF) << 24 | (fileLengthByteArray[1] & 0xFF) << 16 | (fileLengthByteArray[2] & 0xFF) << 8 | (fileLengthByteArray[3] & 0xFF);
                                secretDataByteArray = new byte[dataLength];
                            }
                        } else {
                            if (bytesDecoded < dataLength + 4) {
                                secretDataByteArray[bytesDecoded - 5] = (byte) extractedByte;
                                extractedByte = 0;
                                extractedBitsNumber = 0;
                            } else {
                                secretDataByteArray[bytesDecoded - 5] = (byte) extractedByte;
                                hasToDecode = false;
                                breakFlag = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        SecretData secretData = decodeMessage(secretDataByteArray);
        return secretData;
    }

    private static SecretData decodeMessage(byte[] secretDataByteArray) throws FileNotFoundException {

        // decrypt first

        FileEncryption fileEncryption = new FileEncryption();
        byte[] decryptedDataByteArray = fileEncryption.decrypt(secretDataByteArray);

        byte[] fileExtensionLengthByteArray = new byte[4];
        System.arraycopy(decryptedDataByteArray, 0, fileExtensionLengthByteArray, 0, 4);
        int fileExtensionLength = fileExtensionLength = fileExtensionLengthByteArray[0] << 24 | fileExtensionLengthByteArray[1] << 16 | fileExtensionLengthByteArray[2] << 8 | fileExtensionLengthByteArray[3];

        byte[] fileExtensionByteArray = new byte[fileExtensionLength];
        char[] extension = new char[fileExtensionLength];
        System.arraycopy(decryptedDataByteArray, 4, fileExtensionByteArray, 0, fileExtensionLength);
        for (int charIndex = 0; charIndex < fileExtensionLength; ++charIndex) {
            extension[charIndex] = (char) fileExtensionByteArray[charIndex];
        }

        String extensionString = new String(extension);

        byte[] secretData = new byte[decryptedDataByteArray.length - fileExtensionLength - 4];
        System.arraycopy(decryptedDataByteArray, 4 + fileExtensionLength, secretData, 0, decryptedDataByteArray.length - fileExtensionLength - 4);

        String outputFileName = "secret." + extensionString;
        return new SecretData(outputFileName, secretData);

    }

    private static void unzigzag(int[] coeff) {
        int[] coeffBloc = new int[64];
        int coeffBlocIndex = 0;
        int index = 0;
        for (int i = 0; i < coeff.length; ++i) {
                index = i - i % 64 + deZigZag[i % 64];
                coeffBloc[coeffBlocIndex++] = coeff[index];
                if ((i + 1) % 64 == 0) {
                    coeffBlocIndex = 0;
                    System.arraycopy(coeffBloc, 0, coeff, index + 1 - 64, 64);
            }
        }
    }
}

