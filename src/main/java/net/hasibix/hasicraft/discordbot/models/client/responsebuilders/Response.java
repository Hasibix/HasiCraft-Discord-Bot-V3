package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Response {
    public static void Send(Message message, MessageChannel channel) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : message.embeds) {
            embedList.add(embed.build());
        }
        net.dv8tion.jda.api.entities.Message response = message.build();
        MessageAction action = channel.sendMessage(response);
        
        if(message.actionRows.size() > 0) {
            action.setActionRows(message.actionRows);
        }

        action.queue();
    }

    public static void Reply(Message message, net.dv8tion.jda.api.entities.Message referenceMessage, boolean mention) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : message.embeds) {
            embedList.add(embed.build());
        }
        net.dv8tion.jda.api.entities.Message response = message.build();
        MessageAction action = referenceMessage.reply(response).mentionRepliedUser(mention);

        if(message.actionRows.size() > 0) {
            action.setActionRows(message.actionRows);
        }

        action.queue();
        
    }

    public static void EditMsg(Message newMessage, net.dv8tion.jda.api.entities.Message message) {
        MessageAction action = message.editMessage(newMessage.build());
        if(newMessage.actionRows.size() > 0) {
            action.setActionRows(newMessage.actionRows);
        }
        action.queue();
    }

    public static void CommandReply(Message message, GenericCommandInteractionEvent event, boolean ephemeral) {
        net.dv8tion.jda.api.entities.Message response = message.build();
        ReplyCallbackAction action = event.reply(response);
        
        if(message.actionRows.size() > 0) {
            action.addActionRows(message.actionRows);
        }

        action.setEphemeral(ephemeral).queue();
    }

    public static void CommandReply(Message message, GenericCommandInteractionEvent event) {
        net.dv8tion.jda.api.entities.Message response = message.build();
        ReplyCallbackAction action = event.reply(response);
        
        if(message.actionRows.size() > 0) {
            action.addActionRows(message.actionRows);
        }

        action.queue();
    }
    
    public static void EditCommandReply(Message newMessage, InteractionHook message) {
        net.dv8tion.jda.api.entities.Message response = newMessage.build();
        WebhookMessageUpdateAction<net.dv8tion.jda.api.entities.Message> action = message.editOriginal(response);

        if(newMessage.actionRows.size() > 0) {
            action.setActionRows(newMessage.actionRows);
        }

        action.queue();
    }

    public static void Send(Message message, MessageChannel channel, @Nullable Consumer<? super net.dv8tion.jda.api.entities.Message> success) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : message.embeds) {
            embedList.add(embed.build());
        }
        net.dv8tion.jda.api.entities.Message response = message.build();
        MessageAction action = channel.sendMessage(response);
        
        if(message.actionRows.size() > 0) {
            action.setActionRows(message.actionRows);
        }

        action.queue(success);
    }

    public static void Reply(Message message, net.dv8tion.jda.api.entities.Message referenceMessage, boolean mention, @Nullable Consumer<? super net.dv8tion.jda.api.entities.Message> success) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : message.embeds) {
            embedList.add(embed.build());
        }
        net.dv8tion.jda.api.entities.Message response = message.build();
        MessageAction action = referenceMessage.reply(response).mentionRepliedUser(mention);

        if(message.actionRows.size() > 0) {
            action.setActionRows(message.actionRows);
        }

        action.queue(success);
    }

    public static void EditMsg(Message newMessage, net.dv8tion.jda.api.entities.Message message, @Nullable Consumer<? super net.dv8tion.jda.api.entities.Message> success) {
        MessageAction action = message.editMessage(newMessage.build());
        if(newMessage.actionRows.size() > 0) {
            action.setActionRows(newMessage.actionRows);
        }
        action.queue(success);
    }

    public static void CommandReply(Message message, GenericCommandInteractionEvent event, boolean ephemeral, @Nullable Consumer<? super InteractionHook> success) {
        net.dv8tion.jda.api.entities.Message response = message.build();
        ReplyCallbackAction action = event.reply(response).setEphemeral(ephemeral);

        action.queue(success);
    }

    public static void CommandReply(Message message, GenericCommandInteractionEvent event, @Nullable Consumer<? super InteractionHook> success) {
        net.dv8tion.jda.api.entities.Message response = message.build();
        ReplyCallbackAction action = event.reply(response);
        
        if(message.actionRows.size() > 0) {
            action.addActionRows(message.actionRows);
        }

        action.queue(success);
    }

    public static void EditCommandReply(Message newMessage, InteractionHook message, @Nullable Consumer<? super net.dv8tion.jda.api.entities.Message> success) {
        net.dv8tion.jda.api.entities.Message response = newMessage.build();
        WebhookMessageUpdateAction<net.dv8tion.jda.api.entities.Message> action = message.editOriginal(response);

        action.queue(success);
    }
}
