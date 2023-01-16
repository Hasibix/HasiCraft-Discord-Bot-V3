package net.hasibix.hasicraft.discordbot.handlers;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.hasibix.hasicraft.discordbot.models.client.Interaction;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmoteEvent;

public class InteractionHandler extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        for (Interaction i : Interaction.interactions) {
            if(event.getMessageId().equals(i.id) && event.getReactionEmote().equals(i.id2)) {
                i.run[0].run(null, event, null, null);
            } else {
                return;
            }
        }
    }

    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        for (Interaction i : Interaction.interactions) {
            if(event.getMessageId().equals(i.id) && event.getReactionEmote().equals(i.id2)) {
                if(i.run[1] != null) {
                    i.run[1].run(null, event, null, null);
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override
    public void onMessageReactionRemoveAll(@Nonnull MessageReactionRemoveAllEvent event) {
        for (Interaction i : Interaction.interactions) {
            if(event.getMessageId().equals(i.id)) {
                if(i.run[2] != null) {
                    i.run[2].run(null, null, event, null);
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override
    public void onMessageReactionRemoveEmote(@Nonnull MessageReactionRemoveEmoteEvent event) {
        for (Interaction i : Interaction.interactions) {
            if(event.getMessageId().equals(i.id) && event.getReactionEmote().equals(i.id2)) {
                if(i.run[3] != null) {
                    i.run[3].run(null, null, null, event);
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        for (Interaction i : Interaction.interactions) {
            if(event.getMessageId().equals(i.id) && event.getButton().equals(i.id2)) {
                if(i.run[3] != null) {
                    i.run[3].run(event, null, null, null);
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override
    public void onSelectMenuInteraction(@Nonnull SelectMenuInteractionEvent event) {
        for (Interaction i : Interaction.interactions) {
            if(event.getMessageId().equals(i.id) && event.getComponentId().equals(i.id2)) {
                i.run[0].run(event, null, null, null);
            } else {
                return;
            }
        }
    }
}
