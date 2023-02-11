package net.hasibix.hasicraft.discordbot.models.client.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

public class Config {
    private Map<String, Object> data;
    
    public Config() {}

    private Yaml yaml = new Yaml();

    public void load() {
        try {
            InputStream inputStream = new FileInputStream(new File("config.yml"));
            Map<String, Object> data = yaml.load(inputStream);
            this.data = data;
        } catch (FileNotFoundException e) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.err.println("[\033[0;36m" + dtf.format(now) + "\033[0m]" + " [" + "\033[1;31m" + "FATAL\033[0m] " +  "\033[0m[Config]: " + "Config file not found! Creating one..");
            try {
                File file = new File("config.yml");
                if (file.createNewFile()) {
                    FileWriter writer = new FileWriter(file);
                    String data = getResourceFileAsString("config.yml");
                    writer.write(data);
                    writer.close();
                    System.exit(1);
                }
            } catch (IOException er) {
                System.err.println("[\033[0;36m" + dtf.format(now) + "\033[0m]" + " [" + "\033[1;31m" + "FATAL\033[0m] " +  "\033[0m[Config]: " + "Unable to create a config file. Please create one manually!");
                System.exit(1);
            }
        }
    }

    private String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public Object get(String key) {
        return this.data.get(key);
    }
    
    public <T> T get(String key, Class<T> type) {
        return type.cast(this.get(key));
    }
    
    @SuppressWarnings("unchecked")
    public Object get(String key, String position) {
        Map<String, Object> map = (Map<String, Object>) data.get(key); 
        return map.get(position);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, String position, Class<T> type) {
        Map<String, Object> map = (Map<String, Object>) data.get(key); 
        return type.cast(map.get(position));
    }
}