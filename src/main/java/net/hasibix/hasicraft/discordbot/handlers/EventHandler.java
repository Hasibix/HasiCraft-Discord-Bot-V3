package net.hasibix.hasicraft.discordbot.handlers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.hasibix.hasicraft.discordbot.models.client.utils.Logger;
import net.hasibix.hasicraft.discordbot.utils.ClassFinder;

public class EventHandler {
    
    private Logger logger;
    public List<ListenerAdapter> events = new ArrayList<ListenerAdapter>();

    public void Initialize() {
        String packageName = "net.hasibix.hasicraft.discordbot.events";
        this.logger = Logger.of(EventHandler.class);

        try {
            Class<?>[] eventFiles = ClassFinder.getClassesFromPackage(packageName);
            for (Class<?> i : eventFiles) {
                Object event = i.getDeclaredConstructor().newInstance();
                if(event instanceof ListenerAdapter) {
                    this.events.add((ListenerAdapter) event);
                }
            }
        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | IllegalAccessException | IOException | NoSuchMethodException e) {
            this.logger.error("An exception occurred while trying to load events!");
            this.logger.trace(e);
        }
    }

    public void registerEvents(JDA client) {
        for (ListenerAdapter event : events) {
            client.addEventListener(event);
        }
    }
}
