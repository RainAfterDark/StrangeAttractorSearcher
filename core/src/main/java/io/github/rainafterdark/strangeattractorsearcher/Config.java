package io.github.rainafterdark.strangeattractorsearcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;

import java.io.File;
import java.io.IOException;

@Data
public class Config {
    private static final String CONFIG_FILE = "sas_config.json";
    private static Config instance;
    private static final ObjectMapper mapper = new ObjectMapper();

    private float simulationSpeed = 1f;
    private int particleAmount = 100;
    private int trailLength = 200;

    // Private constructor for singleton
    private Config() {}

    // Get the singleton instance
    public static synchronized Config getInstance() {
        if (instance == null) {
            instance = loadFromFile();
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
    private static Config loadFromFile() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try {
                return mapper.readValue(file, Config.class);
            } catch (IOException e) {
                System.err.println("Failed to load configuration, using defaults: " + e.getMessage());
            }
        }
        return new Config(); // Return default instance if file doesn't exist
    }
}

