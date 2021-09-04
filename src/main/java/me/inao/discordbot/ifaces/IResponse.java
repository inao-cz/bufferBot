package me.inao.discordbot.ifaces;

import org.javacord.api.DiscordApi;

public interface IResponse {
    void onReceive(DiscordApi api);
}
