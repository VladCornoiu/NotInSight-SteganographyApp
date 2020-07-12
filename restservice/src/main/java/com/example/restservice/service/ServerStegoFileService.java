package com.example.restservice.service;

import com.example.restservice.decoder.SecretFileDecoder;
import com.example.restservice.encoders.AudioEncoder;
import com.example.restservice.encoders.JpegEncoder;
import com.example.restservice.exception.NotEnoughSpaceToEncodeException;
import com.example.restservice.model.SecretData;
import com.example.restservice.properties.ServerFileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ServerStegoFileService {

    private Path coverFileUploadStorageLocation;
    private Path secretFileUploadStorageLocation;
    private Path stegoFileUploadStorageLocation;
    private Path stegoFileDownloadStorageLocation;
    private Path secretFileDownloadStorageLocation;

    private AudioEncoder audioStegoFile;

    @Autowired
    public ServerStegoFileService(ServerFileStorageProperties serverFileStorageProperties) {
        this.coverFileUploadStorageLocation = Paths.get(serverFileStorageProperties.getCoverFileUploadDir()).toAbsolutePath().normalize();
        this.secretFileUploadStorageLocation = Paths.get(serverFileStorageProperties.getSecretFileUploadDir()).toAbsolutePath().normalize();
        this.stegoFileUploadStorageLocation = Paths.get(serverFileStorageProperties.getStegoFileUploadDir()).toAbsolutePath().normalize();
        this.stegoFileDownloadStorageLocation = Paths.get(serverFileStorageProperties.getStegoFileDownloadDir()).toAbsolutePath().normalize();
        this.secretFileDownloadStorageLocation = Paths.get(serverFileStorageProperties.getSecretFileDownloadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.coverFileUploadStorageLocation);
            Files.createDirectories(this.secretFileUploadStorageLocation);
            Files.createDirectories(this.stegoFileDownloadStorageLocation);
            Files.createDirectories(this.secretFileDownloadStorageLocation);
            Files.createDirectories(this.stegoFileUploadStorageLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] computeStegoFile(MultipartFile coverFile, MultipartFile secretFile) throws Exception {

        Path targetCoverFileLocation = null;
        Path targetSecretFileLocation = null;

        String coverFileName = StringUtils.cleanPath(coverFile.getOriginalFilename());
        String secretFileName = StringUtils.cleanPath(secretFile.getOriginalFilename());
        try {
            targetCoverFileLocation = this.coverFileUploadStorageLocation.resolve(coverFileName);
            targetSecretFileLocation = this.secretFileUploadStorageLocation.resolve(secretFileName);

            Files.copy(coverFile.getInputStream(), targetCoverFileLocation, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(secretFile.getInputStream(), targetSecretFileLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        byte[] stegoBytes = null;
        try {
            stegoBytes = getStegoFile(new File(targetCoverFileLocation.toString()), new File(targetSecretFileLocation.toString()));
        } catch (NotEnoughSpaceToEncodeException ex) {
            throw ex;
        }
        return stegoBytes;
    }

    private byte[] getStegoFile(File coverFile, File secretFile) throws Exception {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(coverFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] encodedBytes = null;
        JpegEncoder jpegEncoder = new JpegEncoder(bufferedImage, secretFile, stegoFileDownloadStorageLocation, coverFile.getName());
        try {
            encodedBytes = jpegEncoder.encode();
        } catch (NotEnoughSpaceToEncodeException ex) {
            throw ex;
        }

        return encodedBytes;
    }

    private byte[] getAudioStegoFile(File secretFile, File coverFile) {
        audioStegoFile = new AudioEncoder();

        return new byte[0];
    }

    private byte[] getImageStegoFile(File secretFile, File coverFile) throws IOException {
        BufferedImage image = ImageIO.read(coverFile);
//        BufferedImage coverImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
//        coverImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

//        jpgStegoFile = new JpegEncoder(image, secretFile, stegoFileDownloadStorageLocation, coverFile.getName());
//        return jpgStegoFile.encode();

        return new byte[0];
    }

    private boolean isCoverAudioFile(File coverFile) throws IOException, UnsupportedAudioFileException {
        AudioFileFormat format = AudioSystem.getAudioFileFormat(coverFile);
        return format != null;
    }

    private boolean isCoverFileImage(File coverFile) { // add check if jpeg
        String mimetype = new MimetypesFileTypeMap().getContentType(coverFile);
        String type = mimetype.split("/")[0];

        if ("image".equals(type))
            return true;
        return false;
    }

    public SecretData retrieveSecret(MultipartFile stegoFile) throws IOException {
        Path targetStegoFileUploadLocation = null;

        String stegoFileName = StringUtils.cleanPath(stegoFile.getOriginalFilename());

        try {
            targetStegoFileUploadLocation = this.stegoFileUploadStorageLocation.resolve(stegoFileName);
            Files.copy(stegoFile.getInputStream(), targetStegoFileUploadLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return getSecretData(new File(targetStegoFileUploadLocation.toString()));
    }

    private SecretData getSecretData(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        SecretData secretDataToReturn = SecretFileDecoder.extract(fis, (int) file.length());

        return secretDataToReturn;
    }

}
