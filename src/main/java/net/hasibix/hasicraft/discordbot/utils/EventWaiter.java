package net.hasibix.hasicraft.discordbot.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.internal.utils.Checks;

public class EventWaiter implements EventListener {
    private static final Logger LOG = Logger.of(EventWaiter.class);
    private final HashMap<Class<?>, Set<WaitingEvent<?>>> waitingEvents;
    private final ScheduledExecutorService threadpool;

    public EventWaiter() {
        this.threadpool = Executors.newSingleThreadScheduledExecutor();
        this.waitingEvents = new HashMap<>();
    }

    public <T extends Event> void waitForEvent(Class<T> classType, Predicate<T> condition, Consumer<T> action) {
        waitForEvent(classType, condition, action, -1, null, null);
    }

    public <T extends Event> void waitForEvent(Class<T> classType, Predicate<T> condition, Consumer<T> action,
                                               long timeout, TimeUnit unit, Runnable timeoutAction) {
        Checks.notNull(classType, "The provided class type");
        Checks.notNull(condition, "The provided condition predicate");
        Checks.notNull(action, "The provided action consumer");

        WaitingEvent<?> we = new WaitingEvent<>(condition, action);
        Set<WaitingEvent<?>> set = waitingEvents.computeIfAbsent(classType, c -> new HashSet<>());
        set.add(we);

        if (timeout > 0 && unit != null) {
            threadpool.schedule(() -> {
                try {
                    if (set.remove(we) && timeoutAction != null)
                        timeoutAction.run();
                } catch (Exception ex) {
                    LOG.error("Failed to run timeoutAction");
                    LOG.trace(ex);
                }
            }, timeout, unit);
        }
    }

    @Override
    public void onEvent(GenericEvent event) {
        Class<?> c = event.getClass();

        while (c != null) {
            if (waitingEvents.containsKey(c)) {
                Set<WaitingEvent<?>> set = waitingEvents.get(c);
                WaitingEvent<?>[] toRemove = set.toArray(new WaitingEvent[set.size()]);

                set.removeAll(Stream.of(toRemove).filter(i -> i.attempt(event)).collect(Collectors.toSet()));
            }
            if (event instanceof ShutdownEvent) {
                threadpool.shutdown();
                return;
            }
            c = c.getSuperclass();
        }
    }

    private class WaitingEvent<T extends GenericEvent> {
        final Predicate<T> condition;
        final Consumer<T> action;

        WaitingEvent(Predicate<T> condition, Consumer<T> action) {
            this.condition = condition;
            this.action = action;
        }

        @SuppressWarnings("unchecked")
        boolean attempt(GenericEvent event) {
            if (condition.test((T) event)) {
                action.accept((T) event);
                return true;
            }
            return false;
        }
    }
}