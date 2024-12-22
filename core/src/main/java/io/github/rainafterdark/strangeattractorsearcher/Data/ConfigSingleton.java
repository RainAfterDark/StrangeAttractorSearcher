package io.github.rainafterdark.strangeattractorsearcher.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.CameraConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ColorConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ParticleConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.StrangeConfig;
import lombok.Data;

import java.io.File;
import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ConfigSingleton {
    private static final String CONFIG_FILE = "sas_config.json";
    private static ConfigSingleton instance;
    private static final ObjectMapper mapper = new ObjectMapper();

    // Fields
    private StrangeConfig strange = new StrangeConfig();
    private ParticleConfig particle = new ParticleConfig();
    private ColorConfig color = new ColorConfig();
    private CameraConfig camera = new CameraConfig();

    // Private constructor for singleton
    private ConfigSingleton() {}

    // Get the singleton instance
    public static synchronized ConfigSingleton getInstance() {
        if (instance == null) {
            instance = new ConfigSingleton();
            instance.loadFromFile();
        }
        return instance;
    }

    // Save the current configuration to the JSON file
    public void saveToFile() {
        try {
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(new File(CONFIG_FILE), getInstance());
        } catch (IOException e) {
            System.err.println("Failed to save configuration: " + e.getMessage());
        }
    }

    // Load configuration from the JSON file, or create a new default instance
    private void loadFromFile() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try {
                // Map the JSON values to existing fields
                mapper.readerForUpdating(getInstance()).readValue(file);
            } catch (IOException e) {
                System.err.println("Failed to load configuration, using defaults: " + e.getMessage());
            }
        }
    }
}

