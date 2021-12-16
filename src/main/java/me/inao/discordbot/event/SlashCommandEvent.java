package me.inao.discordbot.event;

import lombok.RequiredArgsConstructor;
import me.inao.discordbot.Main;
import me.inao.discordbot.ifaces.IListener;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.HashMap;

@RequiredArgsConstructor
public class SlashCommandEvent implements SlashCommandCreateListener, IListener {
    private final Main instance;
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction slashInteraction = event.getSlashCommandInteraction();
//        slashInteraction.respondLater();
        HashMap<SlashCommandOptionType, Object> arguments = new HashMap<>();
    }
}
