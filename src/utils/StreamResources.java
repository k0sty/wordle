package utils;

import java.io.InputStream;

public class StreamResources {
    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    public static InputStream getFileFromResourceAsStream(String resourceName) {

        // The class loader that loaded the class
        ClassLoader classLoader = StreamResources.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + resourceName);
        } else {
            return inputStream;
        }

    }
}
