package ru.gopstop.bot;

import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Created by Semyon on 30.07.2016.
 */
public final class FileUtils {

    private static final int FILE_BUFFER_SIZE = 4096;

    private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

    private static final String FILE_PATH;

    // should be in another place, but who cares
    public static final String REPORT_CHAT_ID;

    static {
        Properties properties = new Properties();
        try {
            properties.load(properties.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("No secret.properties with telegram token found in resources/");
        }
        FILE_PATH = properties.getProperty("filespath");
        REPORT_CHAT_ID = properties.getProperty("report.chat_id");
        System.out.println("REPORT_CHAT_ID " + REPORT_CHAT_ID);
    }

    public static File getCachedFile(final String fileName) {

        File file = new File(FILE_PATH + fileName);

        if (file.exists()) {
            return file;
        }
        return null;
    }

    public static File writeResponseBodyToDisk(final ResponseBody body, final String fileName) {
        try {
            File loadingFile = new File(FILE_PATH + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[FILE_BUFFER_SIZE];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(loadingFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();
                return loadingFile;

            } catch (final IOException e) {

                LOGGER.error("Can't read file", e);
                return null;
            } finally {

                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (final IOException e) {
            LOGGER.error("zi shit happenned, file utils bullshot", e);
            return null;
        }
    }

    private FileUtils() {

    }
}
