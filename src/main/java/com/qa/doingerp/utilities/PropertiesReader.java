package com.qa.doingerp.utilities;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompi
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 final class PropertiesReader {
    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);
    private final String fileName;
    private Properties properties;

    public PropertiesReader(String fileName) {
        this.fileName = fileName;
    }

    public synchronized String getProperty(String key, String defaultValue) {
        if (this.properties == null) {
            this.properties = this.loadSelenideProperties();
        }

        return this.properties.getProperty(key, defaultValue);
    }

    private Properties loadSelenideProperties() {
        Properties properties = new Properties();
        this.loadPropertiesFrom(Thread.currentThread().getContextClassLoader(), properties);
        properties.putAll(System.getProperties());
        return properties;
    }

    private void loadPropertiesFrom(ClassLoader classLoader, Properties properties) {
        try {
            InputStream stream = classLoader.getResourceAsStream(this.fileName);

            try {
                if (stream != null) {
                    log.debug("Reading settings from {}", classLoader.getResource(this.fileName));
                    properties.load(stream);
                }
            } catch (Throwable var7) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }

            if (stream != null) {
                stream.close();
            }

        } catch (IOException var8) {
            throw new UncheckedIOException("Failed to read " + this.fileName + " file from classpath", var8);
        }
    }
}

