package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Pages {

    public class Page {
        public @Nullable String text;
        public @Nullable Embed embed;

        public Page(@Nullable String text, @Nullable Embed embed) {
            this.text = text;
            this.embed = embed;
        }
    }

    private @Nullable List<Page> pages;

    public Pages() {
        this.pages = new ArrayList<Page>();
    }

    public void AddPage(@Nullable Page page) {
        if(page != null) {
            this.pages.add(page);
        }
    }

    public void Reply(@Nonnull Message message) {
        net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message response = new net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message(pages.get(0).text);
        response.AddEmbed(pages.get(0).embed);
        Response.Reply(response, message);
    }

    public void Send(@Nonnull MessageChannel channel) {
        net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message response = new net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message(pages.get(0).text);
        response.AddEmbed(pages.get(0).embed);
        Response.Send(response, channel);
    }

}