package com.example.restservice.model;

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
    public int luminanceDCTable[][] = new int[][]{
            {0, 2}, {2, 3}, {3, 3}, {4, 3}, {5, 3}, {6, 3}, {14, 4}, {30, 5}, {62, 6}, {126, 7}, {254, 8}, {510, 9}
    };
    public int luminanceACTable[][] = new int[][]{
            {10, 4}, {0, 2}, {1, 2}, {4, 3}, {11, 4}, {26, 5}, {120, 7}, {248, 8}, {1014, 10}, {65410, 16}, {65411, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {12, 4}, {27, 5}, {121, 7}, {502, 9}, {2038, 11}, {65412, 16}, {65413, 16}, {65414, 16}, {65415, 16}, {65416, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {28, 5}, {249, 8}, {1015, 10}, {4084, 12}, {65417, 16}, {65418, 16}, {65419, 16}, {65420, 16}, {65421, 16}, {65422, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {58, 6}, {503, 9}, {4085, 12}, {65423, 16}, {65424, 16}, {65425, 16}, {65426, 16}, {65427, 16}, {65428, 16}, {65429, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {59, 6}, {1016, 10}, {65430, 16}, {65431, 16}, {65432, 16}, {65433, 16}, {65434, 16}, {65435, 16}, {65436, 16}, {65437, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {122, 7}, {2039, 11}, {65438, 16}, {65439, 16}, {65440, 16}, {65441, 16}, {65442, 16}, {65443, 16}, {65444, 16}, {65445, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {123, 7}, {4086, 12}, {65446, 16}, {65447, 16}, {65448, 16}, {65449, 16}, {65450, 16}, {65451, 16}, {65452, 16}, {65453, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {250, 8}, {4087, 12}, {65454, 16}, {65455, 16}, {65456, 16}, {65457, 16}, {65458, 16}, {65459, 16}, {65460, 16}, {65461, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {504, 9}, {32704, 15}, {65462, 16}, {65463, 16}, {65464, 16}, {65465, 16}, {65466, 16}, {65467, 16}, {65468, 16}, {65469, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {505, 9}, {65470, 16}, {65471, 16}, {65472, 16}, {65473, 16}, {65474, 16}, {65475, 16}, {65476, 16}, {65477, 16}, {65478, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {506, 9}, {65479, 16}, {65480, 16}, {65481, 16}, {65482, 16}, {65483, 16}, {65484, 16}, {65485, 16}, {65486, 16}, {65487, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {1017, 10}, {65488, 16}, {65489, 16}, {65490, 16}, {65491, 16}, {65492, 16}, {65493, 16}, {65494, 16}, {65495, 16}, {65496, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {1018, 10}, {65497, 16}, {65498, 16}, {65499, 16}, {65500, 16}, {65501, 16}, {65502, 16}, {65503, 16}, {65504, 16}, {65505, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {2040, 11}, {65506, 16}, {65507, 16}, {65508, 16}, {65509, 16}, {65510, 16}, {65511, 16}, {65512, 16}, {65513, 16}, {65514, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {65515, 16}, {65516, 16}, {65517, 16}, {65518, 16}, {65519, 16}, {65520, 16}, {65521, 16}, {65522, 16}, {65523, 16}, {65524, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {2041, 11}, {65525, 16}, {65526, 16}, {65527, 16}, {65528, 16}, {65529, 16}, {65530, 16}, {65531, 16}, {65532, 16}, {65533, 16}, {65534, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}
    };
    public int chrominanceDCTable[][] = new int[][] {
            {0, 2}, {1, 2}, {2, 2}, {6, 3}, {14, 4}, {30, 5}, {62, 6}, {126, 7}, {254, 8}, {510, 9}, {1022, 10}, {2046, 11}
    };
    public int chrominanceACTable[][] = new int[][] {
            {0, 2}, {1, 2}, {4, 3}, {10, 4}, {24, 5}, {25, 5}, {56, 6}, {120, 7}, {500, 9}, {1014, 10}, {4084, 12}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {11, 4}, {57, 6}, {246, 8}, {501, 9}, {2038, 11}, {4085, 12}, {65416, 16}, {65417, 16}, {65418, 16}, {65419, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {26, 5}, {247, 8}, {1015, 10}, {4086, 12}, {32706, 15}, {65420, 16}, {65421, 16}, {65422, 16}, {65423, 16}, {65424, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {27, 5}, {248, 8}, {1016, 10}, {4087, 12}, {65425, 16}, {65426, 16}, {65427, 16}, {65428, 16}, {65429, 16}, {65430, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {58, 6}, {502, 9}, {65431, 16}, {65432, 16}, {65433, 16}, {65434, 16}, {65435, 16}, {65436, 16}, {65437, 16}, {65438, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {59, 6}, {1017, 10}, {65439, 16}, {65440, 16}, {65441, 16}, {65442, 16}, {65443, 16}, {65444, 16}, {65445, 16}, {65446, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {121, 7}, {2039, 11}, {65447, 16}, {65448, 16}, {65449, 16}, {65450, 16}, {65451, 16}, {65452, 16}, {65453, 16}, {65454, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {122, 7}, {2040, 11}, {65455, 16}, {65456, 16}, {65457, 16}, {65458, 16}, {65459, 16}, {65460, 16}, {65461, 16}, {65462, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {249, 8}, {65463, 16}, {65464, 16}, {65465, 16}, {65466, 16}, {65467, 16}, {65468, 16}, {65469, 16}, {65470, 16}, {65471, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {503, 9}, {65472, 16}, {65473, 16}, {65474, 16}, {65475, 16}, {65476, 16}, {65477, 16}, {65478, 16}, {65479, 16}, {65480, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {504, 9}, {65481, 16}, {65482, 16}, {65483, 16}, {65484, 16}, {65485, 16}, {65486, 16}, {65487, 16}, {65488, 16}, {65489, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {505, 9}, {65490, 16}, {65491, 16}, {65492, 16}, {65493, 16}, {65494, 16}, {65495, 16}, {65496, 16}, {65497, 16}, {65498, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {506, 9}, {65499, 16}, {65500, 16}, {65501, 16}, {65502, 16}, {65503, 16}, {65504, 16}, {65505, 16}, {65506, 16}, {65507, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {2041, 11}, {65508, 16}, {65509, 16}, {65510, 16}, {65511, 16}, {65512, 16}, {65513, 16}, {65514, 16}, {65515, 16}, {65516, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {0, 0}, {16352, 14}, {65517, 16}, {65518, 16}, {65519, 16}, {65520, 16}, {65521, 16}, {65522, 16}, {65523, 16}, {65524, 16}, {65525, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0},
            {1018, 10}, {32707, 15}, {65526, 16}, {65527, 16}, {65528, 16}, {65529, 16}, {65530, 16}, {65531, 16}, {65532, 16}, {65533, 16}, {65534, 16}, {0, 0}, {0, 0}, {0, 0}, {0, 0}
    };
    public static int[] bitsDCluminance = {
            0x00, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0};
    public static int[] valDCluminance = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    public static int[] bitsDCchrominance = {
            0x01, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0};
    public static int[] valDCchrominance = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    public static int[] bitsACluminance = {
            0x10, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 0x7d};
    public static int[] valACluminance =
            {0x01, 0x02, 0x03, 0x00, 0x04, 0x11, 0x05, 0x12,
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
                    0xf9, 0xfa};
    public static int[] bitsACchrominance = {0x11, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 0x77};
    public static int[] ACChrominanceValues =
            {0x00, 0x01, 0x02, 0x03, 0x11, 0x04, 0x05, 0x21,
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
                    0xf9, 0xfa};

    int bufferBits, word;
    public Huffman() {
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
                    streamOut(outStream, luminanceACTable[0xF0][0], luminanceACTable[0xF0][1]);
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
        int PutBits = bufferBits;

        PutBuffer &= (1 << size) - 1;
        PutBits += size;
        PutBuffer <<= 24 - PutBits;
        PutBuffer |= word;

        while (PutBits >= bitsInByte) {
            final int c = ((PutBuffer >> 16) & mask);
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
            PutBuffer <<= bitsInByte;
            PutBits -= bitsInByte;
        }
        word = PutBuffer;
        bufferBits = PutBits;

    }

    public void flushBuffer(final BufferedOutputStream outStream) {
        int toWrite;
        int PutBuffer = word;
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


}
