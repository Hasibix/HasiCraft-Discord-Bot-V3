package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;

public class Message {
    public @Nullable String content;
    public @Nullable List<Embed> embeds;
    public @Nullable List<ActionRow> actionRows;

    public Message(@Nullable String content) {
        this.content = content;
        this.embeds = new ArrayList<Embed>();
        this.actionRows = new ArrayList<>();
    }

    public Message addEmbed(Embed embed) {
        this.embeds.add(embed);
        return this;
    }

    public Message addActionRow(ItemComponent... components) {
        actionRows.add(ActionRow.of(components));
        return this;
    }

    public net.dv8tion.jda.api.entities.Message build() {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : this.embeds) {
            embedList.add(embed.build());
        }
        net.dv8tion.jda.api.entities.Message response = new MessageBuilder()
            .append(this.content != null ? this.content : "")
            .setEmbeds(embedList)
            .build();
        
        return response;
    }

    public net.dv8tion.jda.api.entities.Message buildAR() {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : this.embeds) {
            embedList.add(embed.build());
        }
        MessageBuilder response = new MessageBuilder()
            .append(this.content != null ? this.content : "")
            .setEmbeds(embedList);
        
        return this.actionRows.size() > 0 ? response.setActionRows(this.actionRows).build() : response.build();
        
    }

    public Message addActionRow(ActionRow actionRow) {
        actionRows.add(actionRow);
        return this;
    }
}
