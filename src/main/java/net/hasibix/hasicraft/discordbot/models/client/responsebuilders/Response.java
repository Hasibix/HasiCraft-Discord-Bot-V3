package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Response {
    public static net.dv8tion.jda.api.entities.Message Send(Message message, MessageChannel channel) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : message.embeds) {
            embedList.add(embed.Build());
        }
        net.dv8tion.jda.api.entities.Message response = message.Build();
        MessageAction action = channel.sendMessage(response);
        
        if(message.actionRows.size() > 0) {
            action.setActionRows(message.actionRows);
        }
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<net.dv8tion.jda.api.entities.Message> future = executor.submit(new Callable<net.dv8tion.jda.api.entities.Message>() {
            public net.dv8tion.jda.api.entities.Message call() throws Exception {
                return action.complete();
            }
        });

        net.dv8tion.jda.api.entities.Message msg;

        try {
            msg = future.get();
            executor.shutdown();
            return msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        return null;
    }

    public static net.dv8tion.jda.api.entities.Message Reply(Message message, net.dv8tion.jda.api.entities.Message referenceMessage, boolean mention) {
        List<MessageEmbed> embedList = new ArrayList<MessageEmbed>();
        for (Embed embed : message.embeds) {
            embedList.add(embed.Build());
        }
        net.dv8tion.jda.api.entities.Message response = message.Build();
        MessageAction action = referenceMessage.reply(response).mentionRepliedUser(mention);

        if(message.actionRows.size() > 0) {
            action.setActionRows(message.actionRows);
        }
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<net.dv8tion.jda.api.entities.Message> future = executor.submit(new Callable<net.dv8tion.jda.api.entities.Message>() {
            public net.dv8tion.jda.api.entities.Message call() throws Exception {
                return action.complete();
            }
        });

        net.dv8tion.jda.api.entities.Message msg;

        try {
            msg = future.get();
            executor.shutdown();
            return msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        return null;
    }

    public static net.dv8tion.jda.api.entities.Message EditMsg(Message newMessage, net.dv8tion.jda.api.entities.Message message) {
        MessageAction action = message.editMessage(newMessage.Build());
        if(newMessage.actionRows.size() > 0) {
            action.setActionRows(newMessage.actionRows);
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<net.dv8tion.jda.api.entities.Message> future = executor.submit(new Callable<net.dv8tion.jda.api.entities.Message>() {
            public net.dv8tion.jda.api.entities.Message call() throws Exception {
                return action.complete();
            }
        });

        net.dv8tion.jda.api.entities.Message msg;

        try {
            msg = future.get();
            executor.shutdown();
            return msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        return null;
    }

    public static InteractionHook CommandReply(Message message, GenericCommandInteractionEvent event, boolean ephemeral) {
        net.dv8tion.jda.api.entities.Message response = message.Build();
        ReplyCallbackAction action = event.reply(response).setEphemeral(ephemeral);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<InteractionHook> future = executor.submit(new Callable<InteractionHook>() {
            public InteractionHook call() throws Exception {
                return action.complete();
            }
        });

        InteractionHook msg;

        try {
            msg = future.get();
            executor.shutdown();
            return msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        return null;
    }
}
