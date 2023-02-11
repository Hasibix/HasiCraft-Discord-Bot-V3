package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Response {
    public static void Send(Message message, MessageChannel channel) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : message.embeds) {
            embedList.add(embed.Build());
        }
        net.dv8tion.jda.api.entities.Message response = message.Build();
        MessageAction action = channel.sendMessage(response);
        
        if(message.actionRows.size() > 0) {
            action.setActionRows(message.actionRows);
        }

        action.queue();
    }

    public static void Reply(Message message, net.dv8tion.jda.api.entities.Message referenceMessage, boolean mention) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : message.embeds) {
            embedList.add(embed.Build());
        }
        net.dv8tion.jda.api.entities.Message response = message.Build();
        MessageAction action = referenceMessage.reply(response).mentionRepliedUser(mention);

        if(message.actionRows.size() > 0) {
            action.setActionRows(message.actionRows);
        }

        action.queue();
        
    }

    public static void EditMsg(Message newMessage, net.dv8tion.jda.api.entities.Message message) {
        MessageAction action = message.editMessage(newMessage.Build());
        if(newMessage.actionRows.size() > 0) {
            action.setActionRows(newMessage.actionRows);
        }
        action.queue();
    }

    public static void CommandReply(Message message, GenericCommandInteractionEvent event, boolean ephemeral) {
        net.dv8tion.jda.api.entities.Message response = message.Build();
        ReplyCallbackAction action = event.reply(response).setEphemeral(ephemeral);

        action.queue();
    }

}
