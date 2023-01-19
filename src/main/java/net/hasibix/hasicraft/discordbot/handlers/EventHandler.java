package net.hasibix.hasicraft.discordbot.handlers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.hasibix.hasicraft.discordbot.models.client.Event;
import net.hasibix.hasicraft.discordbot.models.client.Logger;
import net.hasibix.hasicraft.discordbot.utils.ClassFinder;

public class EventHandler implements EventListener {
    
    public Logger logger;
    public List<Event> events = new ArrayList<Event>();

    public void Initialize(Logger logger) {
        String packageName = "net.hasibix.hasicraft.discordbot.evemts";

        try {
            Class<?>[] commandFiles = ClassFinder.getClassesFromPackage(packageName);
            for (Class<?> i : commandFiles) {
                Method method = i.getDeclaredMethod("register");
                method.invoke(null);
            }
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | IOException | NoSuchMethodException e) {
            this.logger.Error(e.toString());
        }
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void onEvent(@Nonnull GenericEvent event) {

    }

}
