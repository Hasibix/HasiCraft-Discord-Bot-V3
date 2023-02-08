package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;

public class Pagination {

    public class Page {
        public @Nullable String text;
        public @Nullable Embed embed;

        public Page(@Nullable String text, @Nullable Embed embed) {
            if(text == null) {
                this.text = "";
            } else {
                this.text = text;
            }

            this.embed = embed;
        }
    }

    private @Nullable List<Page> pages;
    public String messageId;

    public Pagination() {
        this.pages = new ArrayList<Page>();
        HasiBot.paginationHandler.Add(this);
    }

    public void AddPage(@Nullable Page page) {
        if(page != null) {
            this.pages.add(page);
        }
    }

    public void Reply(@Nonnull Message message, boolean mention) {
        net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message response = new net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message(pages.get(0).text);
        response.AddEmbed(pages.get(0).embed);
        response.AddActionRow(
            Button.secondary("page_0_" + this, "<<"),
            Button.secondary("page_0_" + this, "<"),
            Button.secondary("page_1_" + this, ">"),
            Button.secondary("page_" + this.pages.size() +"_" + this, ">>")
        );
        Response.Reply(response, message, mention);
    }

    public void CommandReply(@Nonnull GenericCommandInteractionEvent event) {
        net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message response = new net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message(pages.get(0).text);
        response.AddEmbed(pages.get(0).embed);
        response.AddActionRow(
            Button.secondary("page_0_" + this, "<<"),
            Button.secondary("page_0_" + this, "<"),
            Button.secondary("page_1_" + this, ">"),
            Button.secondary("page_" + this.pages.size() +"_" + this, ">>")
        );
        Response.CommandReply(response, event, false);
    }

    public void Send(@Nonnull MessageChannel channel) {
        net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message response = new net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message(pages.get(0).text);
        response.AddEmbed(pages.get(0).embed);
        response.AddActionRow(
            Button.secondary("page_0_" + this, "<<"),
            Button.secondary("page_0_" + this, "<"),
            Button.secondary("page_1_" + this, ">"),
            Button.secondary("page_" + this.pages.size() +"_" + this, ">>")
        );
        Response.Send(response, channel);
    }

}