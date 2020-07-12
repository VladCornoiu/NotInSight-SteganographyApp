package com.example.restservice.encoders;

import com.example.restservice.encrypt.FileEncryption;
import com.example.restservice.exception.NotEnoughSpaceToEncodeException;
import com.example.restservice.model.DCTCoefficient;
import com.example.restservice.operations.DCTTransform;
import com.example.restservice.operations.Huffman;
import com.example.restservice.operations.JpegFileInfo;
import com.example.restservice.writer.FileWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JpegEncoder {

    private Path stegoFileAbsolutePath;
    private JpegFileInfo jpeg;
    private BufferedImage coverImage;
    private final File secretFile;
    private final Path stegoFileDownloadStorageLocation;
    private FileEncryption fileEncryption;
    private Huffman huffman;
    private DCTTransform dct;
    private FileWriter fileWriter;
    private BufferedOutputStream outStream;
    private int[] coeff;
    private int coeffCount;
    private File stegoFile;

    public JpegEncoder(BufferedImage coverImage, File secretFile, Path stegoFileStorageLocation, String coverFileName) throws IOException {
        this.coverImage = coverImage;
        this.secretFile = secretFile;
        this.stegoFileDownloadStorageLocation = stegoFileStorageLocation;
        this.stegoFileAbsolutePath = this.stegoFileDownloadStorageLocation.resolve(coverFileName.substring(0, coverFileName.lastIndexOf(".")) + ".jpg");
        this.jpeg = new JpegFileInfo(coverImage);
        this.huffman = new Huffman();
        this.dct = new DCTTransform();
        this.fileWriter = new FileWriter();
        this.stegoFile = new File(stegoFileAbsolutePath.toString()); //"cover2.jpg"
        this.outStream = new BufferedOutputStream(new FileOutputStream(stegoFile));
        this.coeffCount = (int) (Math.ceil(1.0F * jpeg.extendedImageHeight / 16)) * (int) (Math.ceil(1.0F * jpeg.extendedImageWidth / 16)) * 6 * 64;
        this.coeff = new int[coeffCount];
    }

    public byte[] encode() {
        byte[] output = null;

        fileWriter.writeMarkers(outStream, jpeg.imageHeight, jpeg.imageWidth);

        try {
            doWork(secretFile.getAbsolutePath());
            output = Files.readAllBytes(Paths.get(stegoFile.getPath())); // stegoFileAbsolutePath
            System.out.println("Output file length is: " + output.length);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotEnoughSpaceToEncodeException ex) {
            throw ex;
        }

        return output;
    }

    private void doWork(String secretFileAbsolutePath) throws IOException {
        int shuffledIndex = 0;
        float[][] dctArray1 = new float[8][8];
        double[][] dctArray2;
        int[] dctArray3 = new int[64];

        int x, y, xoffset, yoffset;
        //dct work
        for (int i = 0; i < Math.ceil(1.0F * jpeg.extendedImageHeight / 16); ++i)
            for (int j = 0; j < Math.ceil(1.0F * jpeg.extendedImageWidth / 16); ++j) {
                x = j * 8;
                y = i * 8;

                // for y
                xoffset = 0;
                yoffset = 0;
                for (int a = 0; a < 8; ++a)
                    for (int b = 0; b < 8; ++b) {
                        int ii = (jpeg.imageHeight / 2 * 2 <= y * 2 + yoffset + a ? jpeg.imageHeight / 2 * 2 - 1 : y * 2 + yoffset + a);
                        int jj = (jpeg.imageWidth / 2 * 2 <= x * 2 + xoffset + b ? jpeg.imageWidth / 2 * 2 - 1 : x * 2 + xoffset + b);

                        dctArray1[a][b] = jpeg.y[ii][jj];
                    }

                dctArray2 = dct.forwardDCT(dctArray1);
                dctArray3 = dct.quantizeLuminanceBlock(dctArray2);

                System.arraycopy(dctArray3, 0, coeff, shuffledIndex, 64);
                shuffledIndex += 64;

                xoffset = 8;
                yoffset = 0;
                for (int a = 0; a < 8; ++a)
                    for (int b = 0; b < 8; ++b) {
                        int ii = (jpeg.imageHeight / 2 * 2 <= y * 2 + yoffset + a ? jpeg.imageHeight / 2 * 2 - 1 : y * 2 + yoffset + a);
                        int jj = (jpeg.imageWidth / 2 * 2 <= x * 2 + xoffset + b ? jpeg.imageWidth / 2 * 2 - 1 : x * 2 + xoffset + b);

                        dctArray1[a][b] = jpeg.y[ii][jj];
                    }

                dctArray2 = dct.forwardDCT(dctArray1);
                dctArray3 = dct.quantizeLuminanceBlock(dctArray2);

                System.arraycopy(dctArray3, 0, coeff, shuffledIndex, 64);
                shuffledIndex += 64;

                xoffset = 0;
                yoffset = 8;
                for (int a = 0; a < 8; ++a)
                    for (int b = 0; b < 8; ++b) {
                        int ii = (jpeg.imageHeight / 2 * 2 <= y * 2 + yoffset + a ? jpeg.imageHeight / 2 * 2 - 1 : y * 2 + yoffset + a);
                        int jj = (jpeg.imageWidth / 2 * 2 <= x * 2 + xoffset + b ? jpeg.imageWidth / 2 * 2 - 1 : x * 2 + xoffset + b);

                        dctArray1[a][b] = jpeg.y[ii][jj];
                    }

                dctArray2 = dct.forwardDCT(dctArray1);
                dctArray3 = dct.quantizeLuminanceBlock(dctArray2);

                System.arraycopy(dctArray3, 0, coeff, shuffledIndex, 64);
                shuffledIndex += 64;

                xoffset = 8;
                yoffset = 8;
                for (int a = 0; a < 8; ++a)
                    for (int b = 0; b < 8; ++b) {
                        int ii = (jpeg.imageHeight / 2 * 2 <= y * 2 + yoffset + a ? jpeg.imageHeight / 2 * 2 - 1 : y * 2 + yoffset + a);
                        int jj = (jpeg.imageWidth / 2 * 2 <= x * 2 + xoffset + b ? jpeg.imageWidth / 2 * 2 - 1 : x * 2 + xoffset + b);

                        dctArray1[a][b] = jpeg.y[ii][jj];
                    }

                dctArray2 = dct.forwardDCT(dctArray1);
                dctArray3 = dct.quantizeLuminanceBlock(dctArray2);

                System.arraycopy(dctArray3, 0, coeff, shuffledIndex, 64);
                shuffledIndex += 64;


                // for cb
                xoffset = 0;
                yoffset = 0;
                for (int a = 0; a < 8; ++a)
                    for (int b = 0; b < 8; ++b) {
                        int ii = (jpeg.imageHeight / 2 * 1 <= y * 1 + yoffset + a ? jpeg.imageHeight / 2 * 1 - 1 : y * 1 + yoffset + a);
                        int jj = (jpeg.imageWidth / 2 * 1 <= x * 1 + xoffset + b ? jpeg.imageWidth / 2 * 1 - 1 : x * 1 + xoffset + b);

                        dctArray1[a][b] = jpeg.cb[ii][jj];
                    }

                dctArray2 = dct.forwardDCT(dctArray1);
                dctArray3 = dct.quantizeChrominanceBlock(dctArray2);

                System.arraycopy(dctArray3, 0, coeff, shuffledIndex, 64);
                shuffledIndex += 64;

                // for cr
                xoffset = 0;
                yoffset = 0;
                for (int a = 0; a < 8; ++a)
                    for (int b = 0; b < 8; ++b) {
                        int ii = (jpeg.imageHeight / 2 * 1 <= y * 1 + yoffset + a ? jpeg.imageHeight / 2 * 1 - 1 : y * 1 + yoffset + a);
                        int jj = (jpeg.imageWidth / 2 * 1 <= x * 1 + xoffset + b ? jpeg.imageWidth / 2 * 1 - 1 : x * 1 + xoffset + b);

                        dctArray1[a][b] = jpeg.cr[ii][jj];
                    }

                dctArray2 = dct.forwardDCT(dctArray1);
                dctArray3 = dct.quantizeChrominanceBlock(dctArray2);

                System.arraycopy(dctArray3, 0, coeff, shuffledIndex, 64);
                shuffledIndex += 64;

            }

        //encode Encrypted Data
        try {
            encodeSecretData(secretFileAbsolutePath);
        } catch (NotEnoughSpaceToEncodeException ex) {
            throw ex;
        }
        //Huffman Compression

        shuffledIndex = 0;
        int lastDCvalueY = 0, lastDCvalueCb = 0, lastDCvalueCr = 0;
        for (int r = 0; r < Math.ceil(1.0F * jpeg.extendedImageHeight / 16); r++) {
            for (int c = 0; c < Math.ceil(1.0F * jpeg.extendedImageWidth / 16); c++) {

                //huff encode for y
                System.arraycopy(coeff, shuffledIndex, dctArray3, 0, 64);
                huffman.encodeLuminance(outStream, dctArray3, lastDCvalueY);
                lastDCvalueY = dctArray3[0];
                shuffledIndex += 64;

                System.arraycopy(coeff, shuffledIndex, dctArray3, 0, 64);
                huffman.encodeLuminance(outStream, dctArray3, lastDCvalueY);
                lastDCvalueY = dctArray3[0];
                shuffledIndex += 64;

                System.arraycopy(coeff, shuffledIndex, dctArray3, 0, 64);
                huffman.encodeLuminance(outStream, dctArray3, lastDCvalueY);
                lastDCvalueY = dctArray3[0];
                shuffledIndex += 64;

                System.arraycopy(coeff, shuffledIndex, dctArray3, 0, 64);
                huffman.encodeLuminance(outStream, dctArray3, lastDCvalueY);
                lastDCvalueY = dctArray3[0];
                shuffledIndex += 64;

                // huff encode for cb

                System.arraycopy(coeff, shuffledIndex, dctArray3, 0, 64);
                huffman.encodeChrominance(outStream, dctArray3, lastDCvalueCb);
                lastDCvalueCb = dctArray3[0];
                shuffledIndex += 64;

                //huff encode for cr

                System.arraycopy(coeff, shuffledIndex, dctArray3, 0, 64);
                huffman.encodeChrominance(outStream, dctArray3, lastDCvalueCr);
                lastDCvalueCr = dctArray3[0];
                shuffledIndex += 64;
            }
        }
        huffman.flushBuffer(outStream);

        fileWriter.writeEOI(outStream);

        try {
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outStream.close();
        }

    }

    private void encodeSecretData(String secretFileAbsolutePath) throws IOException {
        String fileExtension = secretFileAbsolutePath.substring(secretFileAbsolutePath.lastIndexOf(".") + 1);

        int zeroCoef = 0;

        for (int i = 0; i < coeffCount; i++) {
            if (i % 64 == 0) {
                continue;
            } else if (coeff[i] == 0) {
                zeroCoef++;
            }
        }
        int aproximate_capacity = coeffCount - zeroCoef - coeffCount / 64;
//        PrintStream fps = new PrintStream(new FileOutputStream("log_coeff.txt", true));
//        System.setOut(fps);
        System.out.print("The aproximate capacity is: " + aproximate_capacity + ";");
//        System.out.print(" The total rgb size is: " + coverImage.getWidth() * coverImage.getHeight() + ";");
//        System.out.println(" The ratio between is: " + ((coverImage.getWidth() * coverImage.getHeight()) / aproximate_capacity));
//        System.setOut(System.out);
//        fps.close();
        int fileLength;

        fileLength = (int) new File(secretFileAbsolutePath).length();

        if (fileLength + 264 > aproximate_capacity / 8) {
            System.out.println("Not enough space to encode");
            throw new NotEnoughSpaceToEncodeException("There is not enough space to encode message");
        }
        byte[] fileExtensionByteArray = fileExtension.getBytes(); // fileExtension
        byte[] fileExtensionLengthArrayByte = ByteBuffer.allocate(4).putInt(fileExtensionByteArray.length).array(); // fileExtensionLength
        byte[] secretDataByteArray = Files.readAllBytes(Paths.get(secretFileAbsolutePath)); // secretData
        int fileExtensionLength = fileExtensionByteArray.length;

        int dataLength = fileLength + fileExtensionLengthArrayByte.length + fileExtensionLength;
        dataLength &= 0xFFFFFFFF;

        byte[] dataToEncryptFirst = new byte[dataLength];

        System.arraycopy(fileExtensionLengthArrayByte, 0, dataToEncryptFirst, 0, 4);
        System.arraycopy(fileExtensionByteArray, 0, dataToEncryptFirst, 4, fileExtensionLength);
        System.arraycopy(secretDataByteArray, 0, dataToEncryptFirst, 4 + fileExtensionLength, fileLength);

        fileEncryption = new FileEncryption();
        byte[] encryptedData = fileEncryption.encrypt(dataToEncryptFirst);
        //encrypt first
        int encryptedDataLength = encryptedData.length;

        byte[] encryptedFileLengthByteArray = new byte[]{
                (byte) ((encryptedDataLength >> 24) & 0xFF),
                (byte) ((encryptedDataLength >> 16) & 0xFF),
                (byte) ((encryptedDataLength >> 8) & 0xFF),
                (byte) ((encryptedDataLength) & 0xFF)
        };

        byte[] dataToEncode = new byte[encryptedDataLength + 4];
        System.arraycopy(encryptedFileLengthByteArray, 0, dataToEncode, 0, 4);
        System.arraycopy(encryptedData, 0, dataToEncode, 4, encryptedData.length);

        System.out.println("Data to encode has : " + dataToEncode.length + " bytes");
        // get closest perfect square;

        byte dataToEncodeByte = dataToEncode[0];
        int dataToEncodeSize = dataToEncode.length;
        int dataToEncodeBit = dataToEncodeByte & 1;
        int bitsToEmbed = 8;
        int bytesEncoded = 0;
        int coeffIndex = 0;

        //------ needs to repeat
        boolean hasToEncode = true;
        while(hasToEncode) {
            int square = 1;
            int squareRoot = 1;
            int coefficientsLeft = coeffCount - coeffIndex;

            if (coefficientsLeft < 4) {
                System.out.println("Not enough space to encode");
                throw new NotEnoughSpaceToEncodeException("There is not enough space to encode message");
            }
            while (square < coefficientsLeft) {
                squareRoot++;
                square = squareRoot * squareRoot;
            }
            if (square != coefficientsLeft) { // too much and not equal
                squareRoot--;
                square = squareRoot * squareRoot;
            }
            int matrixSize = squareRoot;
            int matrixIndex = 0;
            DCTCoefficient[][] dctCoefficients = new DCTCoefficient[squareRoot][squareRoot];
            while (matrixIndex < matrixSize - 1) {
                for (int i = matrixIndex; i < matrixSize - 1; i++)
                    dctCoefficients[i][matrixIndex] = new DCTCoefficient(coeff[coeffIndex], coeffIndex++);
                for (int i = matrixIndex; i < matrixSize - 1; i++)
                    dctCoefficients[matrixSize - 1][i] = new DCTCoefficient(coeff[coeffIndex], coeffIndex++);
                for (int i = matrixSize - 1; i > matrixIndex; i--)
                    dctCoefficients[i][matrixSize - 1] = new DCTCoefficient(coeff[coeffIndex], coeffIndex++);
                for (int i = matrixSize - 1; i > matrixIndex; i--)
                    dctCoefficients[matrixIndex][i] = new DCTCoefficient(coeff[coeffIndex], coeffIndex++);
                // Array Out Of Bounds Exception?
                matrixIndex++;
                matrixSize--;
            }

            if (matrixIndex == matrixSize - 1)
                dctCoefficients[matrixIndex][matrixIndex] = new DCTCoefficient(coeff[coeffIndex], coeffIndex++);

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
                        if (value > 0) {
                            if (dataToEncodeBit != (value & 1) && coeff[index] != 1) {
                                coeff[index]--;
                            } else if (coeff[index] == 1 && dataToEncodeBit == 0)
                                coeff[index]++;
                        } else {
                            if (dataToEncodeBit != (value & 1) && coeff[index] != -1) {
                                coeff[index]++;
                            } else if (coeff[index] == -1 && dataToEncodeBit == 0) {
                                coeff[index]--;
                            }
                        }
                        bitsToEmbed--;
                        dataToEncodeBit = dataToEncodeByte & 1;
                        dataToEncodeByte >>= 1;
                    }

                    if (bitsToEmbed == 0) {
                        bytesEncoded++;
                        if (bytesEncoded < dataToEncodeSize) {
                            dataToEncodeByte = dataToEncode[bytesEncoded];
                            dataToEncodeBit = dataToEncodeByte & 1;
                            dataToEncodeByte >>= 1;
                            bitsToEmbed = 8;
                        } else {
                            hasToEncode = false;
                            breakFlag = true;
                            break;
                        }
                    }
                }
            }
        }

    }
}

