package by.bulba.android.environments.config;

import by.bulba.android.environments.AndroidEnvironmentsExtension;
import org.gradle.api.Project;
import org.gradle.internal.impldep.com.google.common.annotations.VisibleForTesting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Simple implementation of {@link ConfigReaderFactory}.
 * */
public class ConfigReaderFactoryImpl implements ConfigReaderFactory {

    private final String configFilePattern;

    public ConfigReaderFactoryImpl(Project project,
                                   AndroidEnvironmentsExtension extension) {
        configFilePattern = readConfigFilePattern(project, extension);
    }

    @Override
    public ConfigReader create(String subConfig) {
        String filePath = String.format(configFilePattern, subConfig);
        File file = new File(filePath);
        return createPropertyConfigReader(file);
    }

    @VisibleForTesting
    ConfigReader createPropertyConfigReader(File propertiesFile) {
        Properties properties = new Properties();
        if (propertiesFile.exists()) {
            try {
                properties.load(new FileInputStream(propertiesFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new PropertyConfigReader(properties);
    }

    @VisibleForTesting
    String readConfigFilePattern(Project project,
                                 AndroidEnvironmentsExtension ext) {
        return project.getRootDir().getPath() + "/" +
                ext.configPath + "/%s/" + ext.configFile;
    }
}
