package com.example.restservice.writer;

import com.example.restservice.operations.DCTTransform;

import java.io.*;

import static com.example.restservice.operations.Huffman.*;

public class FileWriter {

    public void writeMarkers(final BufferedOutputStream bos, int imageHeight, int imageWidth) {

        //SOI MARKER
        final byte[] SOI = {(byte) 0xFF, (byte) 0xD8};

        // APP0-JFIF MARKER
        final byte[] APP0 = new byte[18];
        APP0[0] = (byte) 0xff;	// app0 marker
        APP0[1] = (byte) 0xe0;
        APP0[2] = (byte) 0x00;	//
        APP0[3] = (byte) 0x10;  // length in 2 bytes
        APP0[4] = (byte) 0x4a;	// J
        APP0[5] = (byte) 0x46;  // F
        APP0[6] = (byte) 0x49;  // I
        APP0[7] = (byte) 0x46;  // F
        APP0[8] = (byte) 0x00;  // null byte
        APP0[9] = (byte) 0x01;	// 1.01
        APP0[10] = (byte) 0x00;
        APP0[11] = (byte) 0x00; // density units
        APP0[12] = (byte) 0x00; // horizontal pixel density
        APP0[13] = (byte) 0x01;
        APP0[14] = (byte) 0x00; // vertical pixel density
        APP0[15] = (byte) 0x01;
        APP0[16] = (byte) 0x00; // xthumbnail
        APP0[17] = (byte) 0x00; // ythumbnail

        // DQT MARKER
        final byte DQT[] = new byte[134];
        DQT[0] = (byte) 0xFF;
        DQT[1] = (byte) 0xDB;
        DQT[2] = (byte) 0x00;
        DQT[3] = (byte) 0x84; // segment length in 2 bytes

        int bias = 4;
        DQT[bias++] = (byte) 0x00;
        for (int i = 0; i < 64; ++i) {
            DQT[bias + i] = (byte) (DCTTransform.luminance_matrix[toNaturalOrder[i]]);
        }

        bias = 69;
        DQT[bias++] = (byte) 0x01;
        for (int i = 0; i < 64; ++i) {
            DQT[bias + i] = (byte) (DCTTransform.chrominance_matrix[toNaturalOrder[i]]);
        }

        // SOF MARKER
        final byte SOF[] = new byte[19];
        SOF[0] = (byte) 0xFF;
        SOF[1] = (byte) 0xC0;
        SOF[2] = (byte) 0x00;
        SOF[3] = (byte) 17;
        SOF[4] = (byte) 8; // bits/sample precision
        SOF[5] = (byte) ((imageHeight >> 8) & 0xFF);
        SOF[6] = (byte) (imageHeight & 0xFF); // imageHeight in 2 bytes
        SOF[7] = (byte) ((imageWidth >> 8) & 0xFF);
        SOF[8] = (byte) (imageWidth & 0xFF); // imageWidth in 2 bytes
        SOF[9] = (byte) 0x03; // 3 components: Y cb cr

        int[] downsampleRate = {2, 1, 1}; // default downsample rate (4:2:0)
        int[] quantizationTableNumbers = {0, 1, 1};
        bias = 10;
        for (int i = 0; i < SOF[9]; i++) {
            SOF[bias++] = (byte) (i + 1); // component id
            SOF[bias++] = (byte) ((downsampleRate[i] << 4) + downsampleRate[i]); // 4bytes for HSamp + 4 bytes for VSamp
            SOF[bias++] = (byte) quantizationTableNumbers[i]; // only 2 quantization tables: for lum and chrom
        }

        // DHT MARKER
        final byte DHT[] = new byte[420]; // to define constant
        DHT[0] = (byte) 0xFF;
        DHT[1] = (byte) 0xC4;
        DHT[2] = (byte) 0x01;
        DHT[3] = (byte) 0xA2; // 2 bytes for DHT length -> 418
        int index = 4;
        for (int i = 0; i < bitsDCluminance.length; ++i)
            DHT[index++] = (byte) bitsDCluminance[i];

        for (int i = 0; i < valDCluminance.length; ++i)
            DHT[index++] = (byte) valDCluminance[i];

        for (int i = 0; i < bitsACluminance.length; ++i)
            DHT[index++] = (byte) bitsACluminance[i];

        for (int i = 0; i < valACluminance.length; ++i)
            DHT[index++] = (byte) valACluminance[i];

        for (int i = 0; i < bitsDCchrominance.length; ++i)
            DHT[index++] = (byte) bitsDCchrominance[i];

        for (int i = 0; i < valDCchrominance.length; ++i)
            DHT[index++] = (byte) valDCchrominance[i];

        for (int i = 0; i < bitsACchrominance.length; ++i)
            DHT[index++] = (byte) bitsACchrominance[i];

        for (int i = 0; i < ACChrominanceValues.length; ++i)
            DHT[index++] = (byte) ACChrominanceValues[i];

        //SOS MARKER
        final byte SOS[] = new byte[14];
        SOS[0] = (byte) 0xFF;
        SOS[1] = (byte) 0xDA;
        SOS[2] = (byte) 0x00;
        SOS[3] = (byte) 0x0C; // length is 2 bytes
        SOS[4] = (byte) 0x03; // number of components:  y cb cb;

        bias = 5;
        int[] DCACTableNumber = {0, 1, 1};
        for (int i = 0; i < SOS[4]; ++i) {
            SOS[bias++] = (byte) (i + 1); // component id
            SOS[bias++] = (byte) ((DCACTableNumber[i] << 4) + DCACTableNumber[i]); // DC and AC table numbers
        }

        SOS[bias++] = (byte) 0x00; // find purpose (Ss)
        SOS[bias++] = (byte) 0x3F; // find purpose (Se)
        SOS[bias++] = (byte) 0x00; // find purpose (Ah - 4bytes , Al - 4bytes)

        // write markers
        try {
            //write SOI
            bos.write(SOI, 0, 2);

            //Write APP0-JFIF
            int length = ((APP0[2] & 0xFF) << 8) + (APP0[3] & 0xFF) + 2;
            bos.write(APP0, 0, length);

            //Write DQT
            length = ((DQT[2] & 0xFF) << 8) + (DQT[3] & 0xFF) + 2;
            bos.write(DQT, 0, length);

            //Write SOF
            length = ((SOF[2] & 0xFF) << 8) + (SOF[3] & 0xFF) + 2;
            bos.write(SOF, 0, length);

            //Write DHT
            length = ((DHT[2] & 0xFF) << 8) + (DHT[3] & 0xFF) + 2;
            bos.write(DHT, 0, length);

            //Write SOS
            length = ((SOS[2] & 0xFF) << 8) + (SOS[3] & 0xFF) + 2;
            bos.write(SOS, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEOI(final BufferedOutputStream out) {
        final byte[] EOI = {(byte) 0xFF, (byte) 0xD9};
        try {
            out.write(EOI, 0, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
