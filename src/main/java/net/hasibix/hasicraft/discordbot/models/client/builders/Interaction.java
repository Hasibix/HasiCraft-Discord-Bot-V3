package net.hasibix.hasicraft.discordbot.models.client.builders;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmoteEvent;

public class Interaction {

    public static Interaction[] interactions;
    public interface InteractionFunc<A, B, C, D> {
        void run(@Nullable A a, @Nullable B b, @Nullable C c, @Nullable D d);
    }

    public enum Type {
        Button,
        Reaction,
        Dropdown
    }

    @Nonnull public final String id;
    @Nullable public final Object id2;
    Permission[] permissions;
    @Nonnull public final Type interactionType;
    @Nonnull public final InteractionFunc<GenericComponentInteractionCreateEvent, GenericMessageReactionEvent, MessageReactionRemoveAllEvent, MessageReactionRemoveEmoteEvent>[] run;

    public Interaction(@Nonnull String id, @Nullable ReactionEmote id2, Permission[] permissions, @Nonnull Type interactionType, @Nonnull InteractionFunc<GenericComponentInteractionCreateEvent, GenericMessageReactionEvent, MessageReactionRemoveAllEvent, MessageReactionRemoveEmoteEvent>[] run) {
        this.id = id;
        this.id2 = id2;
        this.interactionType = interactionType;
        this.permissions = permissions;
        this.run = run;
    }

}