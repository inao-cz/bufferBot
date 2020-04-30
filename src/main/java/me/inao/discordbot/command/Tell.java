package me.inao.discordbot.command;

import me.inao.discordbot.Main;
import me.inao.discordbot.ifaces.ICommand;
import me.inao.discordbot.ifaces.Permissionable;
import org.javacord.api.entity.message.Message;

public class Tell extends Permissionable implements ICommand {
    @Override
    public void onCommand(Main instance, Message message, String[] args) {
        if(!hasPermission(instance, message, this.getClass())){
            return;
        }
    }

    @Override
    public String getUsage() {
        return "<channel name> <message>";
    }
}
