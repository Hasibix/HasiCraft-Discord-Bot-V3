package net.hasibix.hasicraft.discordbot.commands.info;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.Main;
import net.hasibix.hasicraft.discordbot.models.client.builders.Command;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Embed;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Response;

public class Ping {
    public static void register() {
        Command command = new Command(
            "ping",
            new String[]{},
            "Ping command",
            new Permission[]{},
            "Info",
            new OptionData[]{},
            (client, event, args, logger) -> {
                LocalDateTime currentTime = LocalDateTime.now();
                Duration duration = Duration.between(Main.startTime, currentTime);
                String uptime = String.format("%dd %dh %dm %ds", duration.toDays(),
                    duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());

                Runtime runtime = Runtime.getRuntime();
                int mb = 1024 * 1024;

                Embed embed = new Embed()
                .setColor(Color.BLACK)
                .setTitle("Pong!", null)

                .addField(":ping_pong: Ping", String.format("%s ms",client.getGatewayPing()), true)
                .addField(":clock1: Uptime", uptime, true)
                .addField(":file_cabinet: Memory", String.format("Total: %sMB,\nFree: %sMB,\nUsed: %sMB,\nMax: %sMB",
                    runtime.totalMemory() / mb,
                    runtime.freeMemory() / mb,
                    (runtime.totalMemory() - runtime.freeMemory()) / mb,
                    runtime.maxMemory() / mb
                ), true)
                .addBlankField(false)
                .addField(":homes: Guilds", String.format("%s", client.getGuildCache().size()), true)
                .addField(":busts_in_silhouette: Users", String.format("%s", client.getUserCache().size()), true)
                .addField(":control_knobs: API Latency", String.format("%s ms", client.getRestPing().complete()), true)
                .addBlankField(false)
                .addField(":robot: Bot Version", "SNAPSHOT-V3.0", true)
                .addField(":books: JDA Version", "5.0.0-alpha.8", true)
                .addField(":coffee: Java Version", "17.0.6-LTS", true);

                Response.Reply(new Message(null).addEmbed(embed), event.getMessage(), false);
            },
            (client, event, args, logger) -> {
                LocalDateTime currentTime = LocalDateTime.now();
                Duration duration = Duration.between(Main.startTime, currentTime);
                String uptime = String.format("%dd %dh %dm %ds", duration.toDays(),
                    duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());

                Runtime runtime = Runtime.getRuntime();
                int mb = 1024 * 1024;

                Embed embed = new Embed()
                .setColor(Color.BLACK)
                .setTitle("Pong!", null)

                .addField(":ping_pong: Ping", String.format("%s ms",client.getGatewayPing()), true)
                .addField(":clock1: Uptime", uptime, true)
                .addField(":file_cabinet: Memory", String.format("Total: %sMB,\nFree: %sMB,\nUsed: %sMB,\nMax: %sMB",
                    runtime.totalMemory() / mb,
                    runtime.freeMemory() / mb,
                    (runtime.totalMemory() - runtime.freeMemory()) / mb,
                    runtime.maxMemory() / mb
                ), true)
                .addBlankField(false)
                .addField(":homes: Guilds", String.format("%s", client.getGuildCache().size()), true)
                .addField(":busts_in_silhouette: Users", String.format("%s", client.getUserCache().size()), true)
                .addField(":control_knobs: API Latency", String.format("%s ms", client.getRestPing().complete()), true)
                .addBlankField(false)
                .addField(":robot: Bot Version", "SNAPSHOT-V3.0", true)
                .addField(":books: JDA Version", "5.0.0-alpha.8", true)
                .addField(":coffee: Java Version", "17.0.6-LTS", true);

                Response.CommandReply(new Message(null).addEmbed(embed), event, false);
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }
}
