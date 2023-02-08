package net.hasibix.hasicraft.discordbot.handlers;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Pagination;

public class PaginationHandler extends ListenerAdapter {
    public List<Pagination> paginations;

    public PaginationHandler() {
        this.paginations = new ArrayList<Pagination>();
    }

    public void Add(Pagination pagination) {
        this.paginations.add(pagination);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {

    }
}
