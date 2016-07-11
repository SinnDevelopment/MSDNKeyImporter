package com.sinndevelopment.msdnkeyimporter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ConfigReader
{
    private Properties properties;

    public ConfigReader() throws IOException
    {
        init();
    }

    private void init() throws IOException
    {
        File config = new File("config.ini");
        if(!config.exists())
        {
            URL inputUrl = getClass().getResource("/config.ini");
            FileUtils.copyURLToFile(inputUrl, config);
        }

        FileInputStream fis = new FileInputStream(config);

        properties = new Properties();
        properties.load(fis);
    }

    public String getHost()
    {
        return properties.getProperty("host", "localhost");
    }
    public int getPort()
    {
        return Integer.valueOf(properties.getProperty("port", "3306"));
    }

    public String getUsername()
    {
        return properties.getProperty("mysqlaccount", "user");
    }
    public String getPassword()
    {
        return properties.getProperty("mysqlpass", "pass");
    }
    public String getDatabase()
    {
        return properties.getProperty("mysqldb", "msdnkeys");
    }
}
