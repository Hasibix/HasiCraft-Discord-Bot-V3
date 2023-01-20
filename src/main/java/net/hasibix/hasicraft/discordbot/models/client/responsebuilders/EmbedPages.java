package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EmbedPages {
    @Nonnull Embed[] embeds;
    
    public EmbedPages(@Nonnull Embed[] embeds) {
        this.embeds = embeds;
    }

    public Message build(GenericEvent event) {
        if(event instanceof MessageReceivedEvent) {
            
        } else if (event instanceof SlashCommandInteractionEvent) {

        }

        return null;
    }
}