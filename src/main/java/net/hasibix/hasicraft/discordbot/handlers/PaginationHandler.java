package net.hasibix.hasicraft.discordbot.handlers;

import java.util.ArrayList;
import java.util.List;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Pagination;

public class PaginationHandler {
    public List<Pagination> paginations;
    private JDA client;

    public PaginationHandler(JDA client) {
        this.paginations = new ArrayList<Pagination>();
        this.client = client;
    }

    public void add(Pagination pagination) {
        this.paginations.add(pagination);
        EventWaiter waiter = new EventWaiter();
        pagination.setWaiter(waiter);
        this.client.addEventListener(waiter);
    }
    
}
