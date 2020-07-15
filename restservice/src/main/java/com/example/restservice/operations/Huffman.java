package com.example.restservice.operations;

import java.io.*;

public class Huffman {

    public static final int MATRIX_SIZE = 64;
    public static final int bitsInByte = 8;
    public static final int mask = 0xFF;

    public static int[] toNaturalOrder = {
        0, 1, 8, 16, 9, 2, 3, 10,
        17, 24, 32, 25, 18, 11, 4, 5,
        12, 19, 26, 33, 40, 48, 41, 34,
        27, 20, 13, 6, 7, 14, 21, 28,
        35, 42, 49, 56, 57, 50, 43, 36,
        29, 22, 15, 23, 30, 37, 44, 51,
        58, 59, 52, 45, 38, 31, 39, 46,
        53, 60, 61, 54, 47, 55, 62, 63,
    };

    public static int[] luminanceDCNumberOfBitRepresentations = { 0x00, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 };
    public static int[] luminanceDCLengthValues = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    public static int[] chrominanceDCNumberOfBitRepresentations = { 0x01, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 };
    public static int[] chrominanceDCLengthValues = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    public static int[] luminanceACNumberOfBitRepresentations = { 0x10, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 0x7d };
    public static int[] luminanceACLengthValues = {
        0x01, 0x02, 0x03, 0x00, 0x04, 0x11, 0x05, 0x12,
        0x21, 0x31, 0x41, 0x06, 0x13, 0x51, 0x61, 0x07,
        0x22, 0x71, 0x14, 0x32, 0x81, 0x91, 0xa1, 0x08,
        0x23, 0x42, 0xb1, 0xc1, 0x15, 0x52, 0xd1, 0xf0,
        0x24, 0x33, 0x62, 0x72, 0x82, 0x09, 0x0a, 0x16,
        0x17, 0x18, 0x19, 0x1a, 0x25, 0x26, 0x27, 0x28,
        0x29, 0x2a, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
        0x3a, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49,
        0x4a, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59,
        0x5a, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69,
        0x6a, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79,
        0x7a, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89,
        0x8a, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98,
        0x99, 0x9a, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6, 0xa7,
        0xa8, 0xa9, 0xaa, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6,
        0xb7, 0xb8, 0xb9, 0xba, 0xc2, 0xc3, 0xc4, 0xc5,
        0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xd2, 0xd3, 0xd4,
        0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xe1, 0xe2,
        0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9, 0xea,
        0xf1, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8,
        0xf9, 0xfa
        };
    public static int[] chrominanceACNumberOfBitRepresentations = { 0x11, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 0x77 };
    public static int[] chrominanceACLengthValues = {
        0x00, 0x01, 0x02, 0x03, 0x11, 0x04, 0x05, 0x21,
        0x31, 0x06, 0x12, 0x41, 0x51, 0x07, 0x61, 0x71,
        0x13, 0x22, 0x32, 0x81, 0x08, 0x14, 0x42, 0x91,
        0xa1, 0xb1, 0xc1, 0x09, 0x23, 0x33, 0x52, 0xf0,
        0x15, 0x62, 0x72, 0xd1, 0x0a, 0x16, 0x24, 0x34,
        0xe1, 0x25, 0xf1, 0x17, 0x18, 0x19, 0x1a, 0x26,
        0x27, 0x28, 0x29, 0x2a, 0x35, 0x36, 0x37, 0x38,
        0x39, 0x3a, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48,
        0x49, 0x4a, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58,
        0x59, 0x5a, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68,
        0x69, 0x6a, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78,
        0x79, 0x7a, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87,
        0x88, 0x89, 0x8a, 0x92, 0x93, 0x94, 0x95, 0x96,
        0x97, 0x98, 0x99, 0x9a, 0xa2, 0xa3, 0xa4, 0xa5,
        0xa6, 0xa7, 0xa8, 0xa9, 0xaa, 0xb2, 0xb3, 0xb4,
        0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xc2, 0xc3,
        0xc4, 0xc5, 0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xd2,
        0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda,
        0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9,
        0xea, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8,
        0xf9, 0xfa
    };

    private int[][] luminanceACTable;
    private int[][] luminanceDCTable;
    private int[][] chrominanceDCTable;
    private int[][] chrominanceACTable;
    int bufferBits, bufferWord;

    public Huffman() {
        createLuminanceCodeTables();
        createChrominanceCodeTables();
    }

    public void encodeLuminance(final BufferedOutputStream outStream, final int zigzag[], final int prec) {
        int bitsToWrite;
        int valueTemp, value;

        // DC
        valueTemp = value = zigzag[0] - prec;
        if (valueTemp < 0) {
            valueTemp = -valueTemp;
            value--;
        }
        bitsToWrite = 0;
        while (valueTemp != 0) {
            bitsToWrite++;
            valueTemp >>= 1;
        }
        streamOut(outStream, luminanceDCTable[bitsToWrite][0], luminanceDCTable[bitsToWrite][1]);
        if (bitsToWrite != 0) {
            streamOut(outStream, value, bitsToWrite);
        }

        // AC
        int occurences = 0;
        for (int i = 1; i < MATRIX_SIZE; i++) {
            valueTemp = zigzag[toNaturalOrder[i]];
            if (valueTemp == 0) {
                occurences++;
            } else {
                while (occurences > 15) {
                    streamOut(outStream, luminanceACTable[0xF0][0], luminanceACTable[0xF0][1]); // writes 16 occurences of 0
                    occurences -= 16;
                }
                value = valueTemp;
                if (valueTemp < 0) {
                    valueTemp = -valueTemp;
                    value--;
                }
                bitsToWrite = 1;
                while ((valueTemp >>= 1) != 0) {
                    bitsToWrite++; // bits necessary to write specific number
                }
                int poz = (occurences << 4) + bitsToWrite; // add occurences of 0 if necessary
                streamOut(outStream, luminanceACTable[poz][0], luminanceACTable[poz][1]);
                streamOut(outStream, value, bitsToWrite);

                occurences = 0;
            }
        }

        if (occurences > 0) {
            streamOut(outStream, luminanceACTable[0][0], luminanceACTable[0][1]);
        }

    }

    public void encodeChrominance(final BufferedOutputStream outStream, final int zigzag[], final int prec) {
        int bitsToWrite;
        int value, valueTemp;

        // DC
        valueTemp = value = zigzag[0] - prec;
        if (valueTemp < 0) {
            valueTemp = -valueTemp;
            value--;
        }
        bitsToWrite = 0;
        while (valueTemp != 0) {
            bitsToWrite++;
            valueTemp >>= 1;
        }
        streamOut(outStream, chrominanceDCTable[bitsToWrite][0], chrominanceDCTable[bitsToWrite][1]);
        if (bitsToWrite != 0) {
            streamOut(outStream, value, bitsToWrite);
        }

        // AC
        int occurences = 0;
        for (int i = 1; i < MATRIX_SIZE; i++) {
            valueTemp = zigzag[toNaturalOrder[i]];
            if (valueTemp == 0) {
                occurences++;
            } else {
                while (occurences > 15) {
                    streamOut(outStream, chrominanceACTable[0xF0][0], chrominanceACTable[0xF0][1]);
                    occurences -= 16;
                }
                value = valueTemp;
                if (valueTemp < 0) {
                    valueTemp = -valueTemp;
                    value--;
                }
                bitsToWrite = 1;
                while ((valueTemp >>= 1) != 0) {
                    bitsToWrite++;
                }
                int poz = (occurences << 4) + bitsToWrite;
                streamOut(outStream, chrominanceACTable[poz][0], chrominanceACTable[poz][1]);
                streamOut(outStream, value, bitsToWrite);

                occurences = 0;
            }
        }
        if (occurences > 0) {
            streamOut(outStream, chrominanceACTable[0][0], chrominanceACTable[0][1]);
        }

    }

    void streamOut(final BufferedOutputStream outStream, final int code, final int size) {
        int PutBuffer = code;
        int PutBits = bufferBits; // get remaining bits to write

        PutBuffer &= (1 << size) - 1; // for < 0 numbers ; write only necessary bits from 2's complement representation
        PutBits += size;
        PutBuffer <<= 24 - PutBits;
        PutBuffer |= bufferWord;

        while (PutBits >= bitsInByte) {
            final int c = ((PutBuffer >> 16) & mask); // write only 8 bits
            try {
                outStream.write(c);
            } catch (final IOException e) {
                e.printStackTrace();
            }
            if (c == mask) {
                try {
                    outStream.write(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            PutBuffer <<= bitsInByte; // discard wrote bits
            PutBits -= bitsInByte;
        }
        bufferWord = PutBuffer; // save current bitstream
        bufferBits = PutBits; // save current number of bits to write

    }

    public void flushBuffer(final BufferedOutputStream outStream) {
        int toWrite;
        int PutBuffer = bufferWord;
        int PutBits = bufferBits;
        while (PutBits >= bitsInByte) {
            toWrite = ((PutBuffer >> 16) & mask);
            try {
                outStream.write(toWrite);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (toWrite == mask) {
                try {
                    outStream.write(0);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            PutBuffer <<= bitsInByte;
            PutBits -= bitsInByte;
        }
        if (PutBits > 0) {
            toWrite = ((PutBuffer >> 16) & mask);
            try {
                outStream.write(toWrite);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createLuminanceCodeTables() {
        luminanceDCTable = new int[12][2];
        int[] codesize = new int[257];
        int[] codeValues = new int[257];

        int index = 0;
        for (int i = 1; i <= 16; ++i) {
            for (int j = 1; j <= luminanceDCNumberOfBitRepresentations[i]; ++j) {
                codesize[index++]  = i;
            }
        }

        codesize[index] = -1;
        int lastIndex = index;
        index = 0;
        int bitsValue = codesize[index];
        int codeValue = 0;

        while(codesize[index] != -1) {
            while(codesize[index] == bitsValue) {
                codeValues[index++] = codeValue;
                codeValue++;
            }
            bitsValue++;
            codeValue *= 2;
        }

        for (int i = 0; i < lastIndex; ++i) {
            luminanceDCTable[luminanceDCLengthValues[i]][0] = codeValues[i];
            luminanceDCTable[luminanceDCLengthValues[i]][1] = codesize[i];
        }

        luminanceACTable = new int[255][2];
        index = 0;
        for (int i = 1; i <= 16; ++i) {
            for (int j = 1; j <= luminanceACNumberOfBitRepresentations[i]; ++j) {
                codesize[index++] = i;
            }
        }

        codesize[index] = -1;
        lastIndex = index;
        index = 0;
        bitsValue = codesize[index];
        codeValue = 0;

        while(codesize[index] != -1) {
            while(codesize[index] == bitsValue) {
                codeValues[index++] = codeValue;
                codeValue++;
            }
            bitsValue++;
            codeValue *= 2;
        }

        for (int i = 0; i < lastIndex; ++i) {
            luminanceACTable[luminanceACLengthValues[i]][0] = codeValues[i];
            luminanceACTable[luminanceACLengthValues[i]][1] = codesize[i];
        }
    }

    private void createChrominanceCodeTables() {
        chrominanceDCTable = new int[12][2];
        int[] codesize = new int[257];
        int[] codeValues = new int[257];

        int index = 0;
        for (int i = 1; i <= 16; ++i) {
            for (int j = 1; j <= chrominanceDCNumberOfBitRepresentations[i]; ++j) {
                codesize[index++]  = i;
            }
        }

        codesize[index] = -1;
        int lastIndex = index;
        index = 0;
        int bitsValue = codesize[index];
        int codeValue = 0;

        while(codesize[index] != -1) {
            while(codesize[index] == bitsValue) {
                codeValues[index++] = codeValue;
                codeValue++;
            }
            bitsValue++;
            codeValue *= 2;
        }

        for (int i = 0; i < lastIndex; ++i) {
            chrominanceDCTable[chrominanceDCLengthValues[i]][0] = codeValues[i];
            chrominanceDCTable[chrominanceDCLengthValues[i]][1] = codesize[i];
        }

        chrominanceACTable = new int[255][2];
        index = 0;
        for (int i = 1; i <= 16; ++i) {
            for (int j = 1; j <= chrominanceACNumberOfBitRepresentations[i]; ++j) {
                codesize[index++] = i;
            }
        }

        codesize[index] = -1;
        lastIndex = index;
        index = 0;
        bitsValue = codesize[index];
        codeValue = 0;

        while(codesize[index] != -1) {
            while(codesize[index] == bitsValue) {
                codeValues[index++] = codeValue;
                codeValue++;
            }
            bitsValue++;
            codeValue *= 2;
        }

        for (int i = 0; i < lastIndex; ++i) {
            chrominanceACTable[chrominanceACLengthValues[i]][0] = codeValues[i];
            chrominanceACTable[chrominanceACLengthValues[i]][1] = codesize[i];
        }
    }
}
