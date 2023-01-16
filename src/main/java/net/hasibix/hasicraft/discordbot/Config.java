package net.hasibix.hasicraft.discordbot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import net.hasibix.hasicraft.discordbot.models.client.Configuration;

public class Config {

    public Configuration[] configArray = new Configuration[0];

    public Config() {}

    public void loadConfig(String path) {
        List<Configuration> config = new ArrayList<Configuration>() {};
        
        try {
            JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(path))));
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);
                config.add(new Configuration(key, value));
            }

            configArray = config.toArray(new Configuration[config.size()]);

        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public Configuration getKey(String keyName) {
        for (Configuration i : configArray) {
            if(i.name.equals(keyName)) {
                return i;
            } else {
                continue;
            }
        }
        return null;
    }

}

