package org.smarterbalanced.itemreviewviewer.web.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The settings reader use for reading the item viewer service properties file.
 */
public class SettingsReader {
    private static final Logger logger = LoggerFactory.getLogger(SettingsReader.class);

    private static Properties props;

    /**
     * Get the string value associated with the given property key.
     *
     * @param key the key
     * @return the value associated with the given key
     */
    public static String get(String key) {
        try {
            if (props == null) {
                props = new Properties();
            }
            URL resource = SettingsReader.class.getResource("/settings-mysql.xml");
            InputStream in = new FileInputStream(resource.getPath());
            props.loadFromXML(in);
            in.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
        return props.getProperty(key);

    }

    /**
     * Read content path string.
     *
     * @return the iris content path as a string
     * @throws IOException                  io exception loading config files
     * @throws URISyntaxException           uri syntax exception
     */
    public static String readIrisContentPath() throws IOException, URISyntaxException {
        return get("iris.ContentPath");
    }

    public static String getZipFileLocation() throws IOException, URISyntaxException {
        return get("iris.ZipFileLocation");
    }

}
