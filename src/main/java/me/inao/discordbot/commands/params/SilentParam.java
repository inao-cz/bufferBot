package me.inao.discordbot.commands.params;

import lombok.Getter;
import me.inao.discordbot.annotation.ParameterType;
import me.inao.discordbot.ifaces.IParameter;
import org.javacord.api.DiscordApi;

@ParameterType(type = me.inao.discordbot.enums.ParameterType.PROVIDE)
public class SilentParam implements IParameter {
    @Getter
    private boolean isProvided = false;

    @Override
    public void onParse(DiscordApi api, String value) {
        isProvided = true;
    }

    @Override
    public String[] getIdentifiers() {
        return new String[]{
                "s", "-silent"
        };
    }
}
