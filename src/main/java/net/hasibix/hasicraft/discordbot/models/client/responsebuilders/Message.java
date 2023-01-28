package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Message {
    public @Nullable String content;
    public @Nullable List<Embed> embeds;

    public Message(@Nullable String content) {
        this.content = content;
        this.embeds = new ArrayList<Embed>();
    }

    public void AddEmbed(Embed embed) {
        embeds.add(embed);
    }

    public void Send(MessageChannel channel) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : embeds) {
            embedList.add(embed.Build());
        }
        net.dv8tion.jda.api.entities.Message response = new MessageBuilder()
            .append(content)
            .setEmbeds(embedList)
            .build();
        channel.sendMessage(response).queue();
    }

    public void Reply(net.dv8tion.jda.api.entities.Message message) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : embeds) {
            embedList.add(embed.Build());
        }
        net.dv8tion.jda.api.entities.Message response = new MessageBuilder()
            .append(content)
            .setEmbeds(embedList)
            .build();
        message.reply(response).queue();
    }
}
