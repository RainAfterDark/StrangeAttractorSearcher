package io.github.rainafterdark.strangeattractorsearcher.Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.CameraConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ColorConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ParticleConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.StrangeConfig;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeCubicAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeQuadraticAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeQuarticAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Util.PropertyBasedInterfaceMarshal;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigSingleton {
    private static final String CONFIG_FILE = "sas_config.json";
    private static ConfigSingleton instance;
    private transient final Gson gson;

    @Getter
    private final StrangeConfig strange = new StrangeConfig();
    @Getter
    private final ParticleConfig particle = new ParticleConfig();
    @Getter
    private final ColorConfig color = new ColorConfig();
    @Getter
    private final CameraConfig camera = new CameraConfig();
    @Getter @Setter
    private transient Attractor selectedAttractor;

    private ConfigSingleton() {
        gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(StrangeAttractor.class,
                new PropertyBasedInterfaceMarshal(
                    StrangeQuadraticAttractor.class,
                    StrangeCubicAttractor.class,
                    StrangeQuarticAttractor.class
                ))
            .create();
    }

    public static synchronized ConfigSingleton getInstance() {
        if (instance == null) {
            instance = new ConfigSingleton();
            instance.loadFromFile();
        }
        return instance;
    }

    public void saveToFile() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(getInstance(), writer);
        } catch (IOException e) {
            System.err.println("Failed to save configuration: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                instance = gson.fromJson(reader, ConfigSingleton.class);
            } catch (IOException e) {
                System.err.println("Failed to load configuration, using defaults: " + e.getMessage());
            }
        }
    }
}
