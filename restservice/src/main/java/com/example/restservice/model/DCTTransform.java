package com.example.restservice.model;

public class DCTTransform {
    public double DivisorsLuminance[] = new double[64];
    public double DivisorsChrominance[] = new double[64];

    public static final int BLOCK_SIZE = 8;

    public final static double cosPI_4 = 0.707106781D;
    public final static double cosPI_38 = 0.382683433D;
    public final static double cosPI_8_cosPI3_8 = 0.541196100D;
    public final static double cosPI_8cosPI3_8 = 1.306562965D;

    public static final int[] luminance_matrix = { // Q50
            16, 11, 10, 16, 24, 40, 51, 61,
            12, 12, 14, 19, 26, 58, 60, 55,
            14, 13, 16, 24, 40, 57, 69, 56,
            14, 17, 22, 29, 51, 87, 80, 62,
            18, 22, 37, 56, 68, 109, 103, 77,
            24, 35, 55, 64, 81, 104, 113, 92,
            49, 64, 78, 87, 103, 121, 120, 101,
            72, 92, 95, 98, 112, 100, 103, 99
    };

    public static final int[] chrominance_matrix = {
            17, 18, 24, 47, 99, 99, 99, 99,
            18, 21, 26, 66, 99, 99, 99, 99,
            24, 26, 56, 99, 99, 99, 99, 99,
            47, 66, 99, 99, 99, 99, 99, 99,
            99, 99, 99, 99, 99, 99, 99, 99,
            99, 99, 99, 99, 99, 99, 99, 99,
            99, 99, 99, 99, 99, 99, 99, 99,
            99, 99, 99, 99, 99, 99, 99, 99
    };

    public DCTTransform() {
        int index = 0;
        double[] AANscaleFactor = { 1.0, 1.387039845, 1.306562965, 1.175875602, 1.0, 0.785694958, 0.541196100, 0.275899379};

        for (int j = 0; j < 64; j++)
        {
            int temp = (int) (0.4F * luminance_matrix[j]); // designed to a quality of 80%
            luminance_matrix[j] = temp;
        }

        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                DivisorsLuminance[index] = (1.0D / ((double) luminance_matrix[index] * AANscaleFactor[i] * AANscaleFactor[j] * 8.0D));
                index++;
            }
        }

        for (int j = 0; j < 64; j++)
        {
            int temp = (int) (0.4F * chrominance_matrix[j]);
            chrominance_matrix[j] = temp;
        }

        index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                DivisorsChrominance[index] = (1.0D / ((double) chrominance_matrix[index] * AANscaleFactor[i] * AANscaleFactor[j] * 8.0D));
                index++;
            }
        }
    }

    public double[][] forwardDCT(float input[][])
    {
        double output[][] = new double[BLOCK_SIZE][BLOCK_SIZE];
        double b0, b1, b2, b3, b4, b5, b6, b7;
        double c1, c2, c3, c4, c5, c6, c7;
        double d1, d2, d3, d4, d5, d6, d7;

        for (int i = 0; i < BLOCK_SIZE; i++) {
            for(int j = 0; j < BLOCK_SIZE; j++) {
                output[i][j] = ((double)input[i][j] - 128.0D); // center the values between [-128, 127]
            }
        }

        for (int i = 0; i < 8; i++) {
            b0 = output[i][0] + output[i][7];
            b1 = output[i][1] + output[i][6];
            b2 = output[i][2] + output[i][5];
            b3 = output[i][3] + output[i][4];
            b4 = output[i][3] - output[i][4];
            b5 = output[i][2] - output[i][5];
            b6 = output[i][1] - output[i][6];
            b7 = output[i][0] - output[i][7];

            c1 = b0 + b3;
            c2 = b1 + b2;
            c3 = b1 - b2;
            c4 = b0 - b3;

            output[i][0] = c1 + c2;
            output[i][4] = c1 - c2;

            d1 = (c3 + c4) * cosPI_4;
            output[i][2] = c4 + d1;
            output[i][6] = c4 - d1;

            c5 = b4 + b5;
            c6 = b5 + b6;
            c7 = b6 + b7;

            d3 = c6 * cosPI_4;
            d6 = b7 + d3;
            d7 = b7 - d3;
            d5 = (c5 - c7) * cosPI_38;
            d2 = cosPI_8_cosPI3_8 * c5 + d5;
            d4 = cosPI_8cosPI3_8 * c7 + d5;

            output[i][5] = d7 + d2;
            output[i][3] = d7 - d2;
            output[i][1] = d6 + d4;
            output[i][7] = d6 - d4;
        }

        for (int i = 0; i < 8; i++) {
            b0 = output[0][i] + output[7][i];
            b1 = output[1][i] + output[6][i];
            b2 = output[2][i] + output[5][i];
            b3 = output[3][i] + output[4][i];
            b4 = output[3][i] - output[4][i];
            b5 = output[2][i] - output[5][i];
            b6 = output[1][i] - output[6][i];
            b7 = output[0][i] - output[7][i];

            c1 = b0 + b3;
            c4 = b0 - b3;
            c2 = b1 + b2;
            c3 = b1 - b2;

            output[0][i] = c1 + c2;
            output[4][i] = c1 - c2;

            d1 = (c3 + c4) * cosPI_4;
            output[2][i] = c4 + d1;
            output[6][i] = c4 - d1;

            c5 = b4 + b5;
            c6 = b5 + b6;
            c7 = b6 + b7;

            d3 = c6 * cosPI_4;
            d6 = b7 + d3;
            d7 = b7 - d3;
            d5 = (c5 - c7) * cosPI_38;
            d2 = cosPI_8_cosPI3_8 * c5 + d5;
            d4 = cosPI_8cosPI3_8 * c7 + d5;


            output[5][i] = d7 + d2;
            output[3][i] = d7 - d2;
            output[1][i] = d6 + d4;
            output[7][i] = d6 - d4;
        }

        return output;
    }

    public int[] quantizeLuminanceBlock(double inputData[][])
    {
        int outputData[] = new int[BLOCK_SIZE * BLOCK_SIZE];
        int index = 0;
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                outputData[index] = (int)(Math.round(inputData[i][j] * DivisorsLuminance[index]));
                index++;
            }
        }

        return outputData;
    }

    public int[] quantizeChrominanceBlock(double inputData[][])
    {
        int outputData[] = new int[BLOCK_SIZE * BLOCK_SIZE];
        int index = 0;
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                outputData[index] = (int)(Math.round(inputData[i][j] * DivisorsChrominance[index]));
                index++;
            }
        }

        return outputData;
    }

    public static void main(String[] args) {
        float[][] testDctTransformMatrix = new float[][] {
                        { 140, 144, 147, 140, 140, 155, 179, 175 },
                        { 144, 152, 140, 147, 140, 148, 167, 179 },
                        { 152, 155, 136, 167, 163, 162, 152, 172 },
                        { 168, 145, 156, 160, 152, 155, 136, 160 },
                        { 162, 148, 156, 148, 140, 136, 147, 162 },
                        { 147, 167, 140, 155, 155, 140, 136, 162 },
                        { 136, 156, 123, 167, 162, 144, 140, 147 },
                        { 148, 155, 136, 155, 152, 147, 147, 136 }
                };

        float[][] testtDctTransformMatrix = new float[][] {
                { 154, 123, 123, 123, 123, 123, 123, 136 },
                { 192, 180, 136, 154, 154, 154, 136, 110 },
                { 254, 198, 154, 154, 180, 154, 123, 123 },
                { 239, 180, 136, 180, 180, 166, 123, 123 },
                { 180, 154, 136, 167, 166, 149, 136, 136 },
                { 128, 136, 123, 136, 154, 180, 198, 154 },
                { 123, 105, 110, 149, 136, 136, 180, 166 },
                { 110, 136, 123, 123, 123, 136, 154, 136 }
        };

    }
}
