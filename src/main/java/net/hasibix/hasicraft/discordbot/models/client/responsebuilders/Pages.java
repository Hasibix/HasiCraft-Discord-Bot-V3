package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Pages {

    private String previousMessageId;

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

    public Pages() {
        this.pages = new ArrayList<Page>();
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
            Button.secondary("firstPageButton", "<<"),
            Button.secondary("previousPageButton", "<"),
            Button.secondary("nextPageButton", ">"),
            Button.secondary("lastPageButton", ">>")
        );
        if (previousMessageId != null) {
            Message prevMsg = message.getChannel().retrieveMessageById(previousMessageId).complete();
            MessageBuilder builder = new MessageBuilder(prevMsg);
            List<Button> btns = prevMsg.getButtons();
            List<Button> newbtns = new ArrayList<Button>();
            for (Button btn : btns) {
                if (btn.getId().equals("firstPageButton") | btn.getId().equals("previousPageButton") | btn.getId().equals("nextPageButton") | btn.getId().equals("previousPageButton") | btn.getId().equals("lastPageButton")) {
                    btn.isDisabled();
                    newbtns.add(btn);
                }
            }
            builder.setActionRows().setActionRows(ActionRow.of(newbtns));
            prevMsg.editMessage(builder.build()).queue();
            
        }
        previousMessageId = Response.Reply(response, message, mention).getId();
    }

    public void CommandReply(@Nonnull GenericCommandInteractionEvent event) {
        net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message response = new net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message(pages.get(0).text);
        response.AddEmbed(pages.get(0).embed);
        response.AddActionRow(
            Button.secondary("firstPageButton", "<<"),
            Button.secondary("previousPageButton", "<"),
            Button.secondary("nextPageButton", ">"),
            Button.secondary("lastPageButton", ">>")
        );
        // if (previousMessageId != null) {
        //     Message prevMsg = event.getMessageChannel().retrieveMessageById(previousMessageId).complete();
        //     MessageBuilder builder = new MessageBuilder(prevMsg);
        //     List<Button> btns = prevMsg.getButtons();
        //     List<Button> newbtns = new ArrayList<Button>();
        //     for (Button btn : btns) {
        //         if (btn.getId().equals("firstPageButton") | btn.getId().equals("previousPageButton") | btn.getId().equals("nextPageButton") | btn.getId().equals("previousPageButton") | btn.getId().equals("lastPageButton")) {
        //             btn.isDisabled();
        //             newbtns.add(btn);
        //         }
        //     }
        //     builder.setActionRows().setActionRows(ActionRow.of(newbtns));
        //     prevMsg.editMessage(builder.build()).queue();
            
        // }
        // previousMessageId = Response.CommandReply(response, event, false).;
    }

    public void Send(@Nonnull MessageChannel channel) {
        net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message response = new net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message(pages.get(0).text);
        response.AddEmbed(pages.get(0).embed);
        response.AddActionRow(
            Button.secondary("firstPageButton", "<<"),
            Button.secondary("previousPageButton", "<"),
            Button.secondary("nextPageButton", ">"),
            Button.secondary("lastPageButton", ">>")
        );
        if (previousMessageId != null) {
            Message prevMsg = channel.retrieveMessageById(previousMessageId).complete();
            MessageBuilder builder = new MessageBuilder(prevMsg);
            List<Button> btns = prevMsg.getButtons();
            List<Button> newbtns = new ArrayList<Button>();
            for (Button btn : btns) {
                if (btn.getId().equals("firstPageButton") | btn.getId().equals("previousPageButton") | btn.getId().equals("nextPageButton") | btn.getId().equals("previousPageButton") | btn.getId().equals("lastPageButton")) {
                    newbtns.add(btn.asDisabled());
                }
            }
            builder.setActionRows().setActionRows(ActionRow.of(newbtns));
            prevMsg.editMessage(builder.build()).queue();
        }
        previousMessageId = Response.Send(response, channel).getId();
    }

}