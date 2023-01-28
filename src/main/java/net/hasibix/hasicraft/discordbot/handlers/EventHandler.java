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
    
    public Logger logger;
    public List<ListenerAdapter> events = new ArrayList<ListenerAdapter>();

    public void Initialize(Logger logger) {
        String packageName = "net.hasibix.hasicraft.discordbot.events";

        try {
            Class<?>[] eventFiles = ClassFinder.getClassesFromPackage(packageName);
            for (Class<?> i : eventFiles) {
                Object event = i.getDeclaredConstructor().newInstance();
                if(event instanceof ListenerAdapter) {
                    this.events.add((ListenerAdapter) event);
                }
            }
        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | IllegalAccessException | IOException | NoSuchMethodException e) {
            this.logger.Error(e.toString());
        }
    }

    public void registerEvents(JDA client) {
        for (ListenerAdapter event : events) {
            client.addEventListener(event);
        }
    }
}
