package com.perivoliotis.app.eSymposium.core.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationUtils {


public static String readFromProperties(String propertyName){


    Properties prop = new Properties();

    try (InputStream input = new FileInputStream("/home/nikos/Dev/Projects/eSymposium/src/main/resources/application-dev.properties")){

        // load a properties file
        prop.load(input);

        // get the property value and print it out
        return prop.getProperty(propertyName);

    } catch (IOException ex) {
        ex.printStackTrace();
    }

    return null;
}

}
