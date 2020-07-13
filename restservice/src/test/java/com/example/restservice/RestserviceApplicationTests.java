package com.example.restservice;

import com.example.restservice.decoder.SecretFileDecoder;
import com.example.restservice.encoders.JpegEncoder;
import com.example.restservice.model.SecretData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class RestserviceApplicationTests {

    @BeforeAll
    public static void prepareOutputData() throws IOException {
        String[] keywords = new String[]{"animals_", "architecture_", "business_", "foodndrink_", "nature_", "people_", "technology_", "textures_", "travel_"};

        for (int i = 0; i < 45; ++i) {
            try {
                String pathName = "C:\\Users\\Vlad\\Desktop\\stego_testing\\test\\thesis_presentation\\" + keywords[i / 5] + (i % 5) + ".jfif";
                BufferedImage bufferedImage = ImageIO.read(new File(pathName));
                JpegEncoder jpegEncoder = new JpegEncoder(bufferedImage, new File("C:\\Users\\Vlad\\Desktop\\stego_testing\\SECRETFILE_3000.txt"), Paths.get("C:\\Projects\\GitHub\\NotInSight-SteganographyApp\\test"), keywords[i / 5] + (i % 5) + "_stego.jpg");
                jpegEncoder.encode();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 45; ++i) {
            String pathName = "C:\\Projects\\GitHub\\NotInSight-SteganographyApp\\test\\" + keywords[i / 5] + (i % 5) + "_stego" + ".jpg";
            File file = new File(pathName);
            SecretData secretDataToReturn = null;
            secretDataToReturn = SecretFileDecoder.extract(new FileInputStream(file), (int) file.length());
            try (FileOutputStream fos = new FileOutputStream("C:\\Projects\\GitHub\\NotInSight-SteganographyApp\\afterStego\\" + i + "_" + secretDataToReturn.getFilename())) {
                fos.write(secretDataToReturn.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testDecodedData() throws IOException {

        byte[] first = Files.readAllBytes(Paths.get("C:\\Users\\Vlad\\Desktop\\stego_testing\\SECRETFILE_3000.txt"));

        for (int i = 0; i < 45; ++i) {
            byte[] second = Files.readAllBytes(Paths.get("C:\\Projects\\GitHub\\NotInSight-SteganographyApp\\afterStego\\" + i + "_secret" + ".txt"));

            assertArrayEquals(first, second);
        }
    }

    @AfterAll
    public static void deleteTestData() {

        File stegoFilesDirectory = new File("C:\\Projects\\GitHub\\NotInSight-SteganographyApp\\test");
        File[] files = stegoFilesDirectory.listFiles();

        for (File file : files) {
            file.delete();
        }

        File secretOutputFilesDirectory = new File("C:\\Projects\\GitHub\\NotInSight-SteganographyApp\\afterStego");
        files = secretOutputFilesDirectory.listFiles();

        for (File file : files) {
            file.delete();
        }
    }
}
