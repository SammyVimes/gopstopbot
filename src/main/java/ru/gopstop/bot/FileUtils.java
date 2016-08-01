package ru.gopstop.bot;

import okhttp3.ResponseBody;

import java.io.*;
import java.util.Properties;

/**
 * Created by Semyon on 30.07.2016.
 */
public class FileUtils {

    private static String filePath = null;

    static {
        Properties properties = new Properties();
        try {
            properties.load(properties.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("No secret.properties with telegram token found in resources/");
        }
        filePath = properties.getProperty("filespath");
    }

    public static File getCachedFile(final String fileName) {

        File file = new File(filePath + fileName);

        if (file.exists()) {
            return file;
        }
        return null;
    }

    public static File writeResponseBodyToDisk(final ResponseBody body, final String fileName) {
        try {
            File loadingFile = new File(filePath + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(loadingFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return loadingFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }



}
