package net.hasibix.hasicraft.discordbot.models.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
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
            System.err.println("[\033[0;36m" + dtf.format(now) + " \033[0m] " + " [" + "\033[1;31m" + "FATAL\033[0m] " +  "\033[0m[Config]: " + "Config file not found! Creating one..");
            try {
                File file = new File("config.yml");
                if (file.createNewFile()) {
                    FileWriter writer = new FileWriter(file);
                    writer.write("prefix: \'!\'\nemoji:\n success: \':white_check_mark:\'\n error: \':x:\'\n music: \':musical_note:\'\n warning: \':warning:\'\n musicNote: \':notes:\'\nclient_id: \'00000000000000000\'\nspotify_client_id: \'xxxx000x000x0xx00x000xx0x000x000\'\nminecraft_server_ip: \'0.0.0.0\'\nminecraft_server_port: 5000");
                    writer.close();
                    System.exit(1);
                }
            } catch (IOException er) {
                System.err.println("[\033[0;36m" + dtf.format(now) + " \033[0m] " + " [" + "\033[1;31m" + "FATAL\033[0m] " +  "\033[0m[Config]: " + "Unable to create a config file. Please create one manually!");
                System.exit(1);
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