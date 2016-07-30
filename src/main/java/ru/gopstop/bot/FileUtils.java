package ru.gopstop.bot;

import okhttp3.ResponseBody;
import org.apache.http.util.TextUtils;

import java.io.*;
import java.util.Properties;

/**
 * Created by Semyon on 30.07.2016.
 */
public class FileUtils {

    public static String FILE_PATH = null;

    static {
        Properties properties = new Properties();
        try {
            properties.load(properties.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("No secret.properties with telegram token found in resources/");
        }
        FILE_PATH = properties.getProperty("filespath");
    }

    public static File writeResponseBodyToDisk(final ResponseBody body, final String fileName) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(FILE_PATH + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return futureStudioIconFile;
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
