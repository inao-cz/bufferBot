package me.inao.discordbot.commands.params;

import lombok.Getter;
import me.inao.discordbot.annotation.ParameterType;
import me.inao.discordbot.ifaces.IParameter;
import org.javacord.api.DiscordApi;

@ParameterType(type = me.inao.discordbot.enums.ParameterType.NUMBER)
public class CountParam implements IParameter {
    @Getter
    private int count = 0;

    @Override
    public void onParse(DiscordApi api, String value) {
        try{
            count = Integer.parseInt(value);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Invalid integer provided.");
        }
    }

    @Override
    public String[] getIdentifiers() {
        return new String[]{
                "c", "-count"
        };
    }
}
