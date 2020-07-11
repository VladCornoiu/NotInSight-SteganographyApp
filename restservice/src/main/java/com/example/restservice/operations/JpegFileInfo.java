package com.example.restservice.operations;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class JpegFileInfo {
    private BufferedImage image;
    public int imageHeight;
    public int imageWidth;

    public int extendedImageHeight;
    public int extendedImageWidth;

    public float[][] y;
    public float[][] cb;
    public float[][] cr;

    private static final int BLOCK_SIZE = 8;

    public JpegFileInfo(BufferedImage image) throws IOException {
        this.image = image;
        imageHeight = image.getHeight();
        imageWidth = image.getWidth();
        rgbToYCbCr();
    }

    private void setExtendedLimits(int imageHeight, int imageWidth) {

        this.extendedImageHeight = (imageHeight % BLOCK_SIZE != 0) ? ((int) Math.ceil((double) imageHeight / 8.0)) * 8 : imageHeight;
        this.extendedImageWidth = (imageWidth % BLOCK_SIZE != 0) ? ((int) Math.ceil((double) imageWidth / 8.0)) * 8 : imageWidth;

    }

    private void downsampleCb(float[][] cbTemp) {

        int locali = 0, localj = 0;
        int bias;
        for (int i = 0; i < extendedImageHeight / 2; ++i) {
            // downsampling factor
            bias = 1;
            for (int j = 0; j < extendedImageWidth / 2; ++j) {
                float mean = (float) (1.0F * (cbTemp[locali][localj] + cbTemp[locali][localj + 1] + cbTemp[locali + 1][localj] + cbTemp[locali + 1][localj + 1] + (float) bias) / 4.0F); // to remove bias
                cb[i][j] = mean;
                bias ^= 3; // 1 -> 2 ; 2 -> 1 : alternating 0.25 and 0.5 to round numbers
                localj += 2;
            }

            locali += 2;
            localj = 0;
        }
    }

    private void downsampleCr(float[][] crTemp) {
        int locali = 0, localj = 0;
        int bias;
        for (int i = 0; i < extendedImageHeight / 2; ++i) {
            // downsampling factor
            bias = 1;
            for (int j = 0; j < extendedImageWidth / 2; ++j) {
                float mean = (float) (1.0F * (crTemp[locali][localj] + crTemp[locali][localj + 1] + crTemp[locali + 1][localj] + crTemp[locali + 1][localj + 1] + (float) bias)/ 4.0F);
                bias ^= 3; // 1 -> 2 ; 2 -> 1 : alternating 0.25 and 0.5 to round numbers
                cr[i][j] = mean;
                localj += 2 ;
            }

            locali += 2;
            localj = 0;
        }
    }

    private void rgbToYCbCr(){

        setExtendedLimits(imageHeight, imageWidth);

        y = new float[extendedImageHeight][extendedImageWidth];
        cb = new float[extendedImageHeight / 2][extendedImageWidth / 2];
        cr = new float[extendedImageHeight / 2][extendedImageWidth / 2];

        float[][] cbTemp = new float[extendedImageHeight][extendedImageWidth];
        float[][] crTemp = new float[extendedImageHeight][extendedImageWidth];

        for (int i = 0; i < imageHeight; ++i)
            for (int j = 0; j < imageWidth; ++j) {
                Color pixel = new Color(image.getRGB(j, i));

                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                y[i][j] = (float) (0.299F * (float) r + 0.587F * (float) g + 0.114F * (float) b);
                cbTemp[i][j] = 128.0F + (float) ( -0.16874F * (float) r - 0.33126F * (float) g + 0.5F * (float) b);
                crTemp[i][j] = 128.0F + (float) (0.5F * (float) r - 0.41869F * (float) g - 0.08131F * (float) b);
            }

        downsampleCb(cbTemp);
        downsampleCr(crTemp);

    }

}
