package net.hasibix.hasicraft.discordbot.models.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Config {

    public class ConfigObject {
        private Map<String, Object> properties;
    
        public ConfigObject(Map<String, Object> properties) {
            this.properties = properties;
        }
    
        public Object get(String key) {
            return properties.get(key);
        }
    
        public <T> T get(String key, Class<T> type) {
            return type.cast(get(key));
        }
    }

    private ConfigObject config;
    private Yaml yaml = new Yaml();

    public Config() {}

    public void loadConfig(String path) {
        try {
            InputStream inputStream = new FileInputStream(new File(path));
            Map<String, Object> properties = yaml.load(inputStream);
            config = new ConfigObject(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    public ConfigObject getConfig() {
        return config;
    }
}