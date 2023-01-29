package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class Message {
    public @Nullable String content;
    public @Nullable List<Embed> embeds;
    public @Nullable List<ActionRow> actionRows;

    public Message(@Nullable String content) {
        this.content = content;
        this.embeds = new ArrayList<Embed>();
    }

    public void AddEmbed(Embed embed) {
        embeds.add(embed);
    }

    public void AddActionRow(ActionRow actionRow) {
        actionRows.add(actionRow);
    }

    public net.dv8tion.jda.api.entities.Message Build() {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : this.embeds) {
            embedList.add(embed.Build());
        }
        net.dv8tion.jda.api.entities.Message response = new MessageBuilder()
            .append(this.content)
            .setEmbeds(embedList)
            .build();
        
        return response;
    }
}
