package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import java.awt.Color;

public class Embed {
    private EmbedBuilder builder;
    
    public Embed() {
        this.builder = new EmbedBuilder();
    }

    public Embed setTitle(String title, String url) {
        builder.setTitle(title, url);
        return this;
    }

    public Embed setColor(Color color) {
        builder.setColor(color);
        return this;
    }

    public Embed setDescription(String text) {
        builder.setDescription(text);
        return this;
    }

    public Embed addField(String title, String text, boolean inline) {
        builder.addField(title, text, inline);
        return this;
    }

    public Embed addBlankField(boolean inline) {
        builder.addBlankField(inline);
        return this;
    }

    public Embed setAuthor(String name, String url, String iconUrl) {
        builder.setAuthor(name, url, iconUrl);
        return this;
    }

    public Embed setFooter(String text, String iconUrl) {
        builder.setFooter(text, iconUrl);
        return this;
    }

    public Embed setImage(String url) {
        builder.setImage(url);
        return this;
    }

    public Embed setThumbnail(String url) {
        builder.setThumbnail(url);
        return this;
    }

    public MessageEmbed build() {
        MessageEmbed embed = builder.build();
        return embed;
    }
}
