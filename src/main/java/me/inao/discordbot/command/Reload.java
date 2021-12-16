package me.inao.discordbot.command;

import me.inao.discordbot.Main;
import me.inao.discordbot.annotation.Permission;
import me.inao.discordbot.ifaces.ICommand;
import me.inao.discordbot.ifaces.IParameter;
import org.javacord.api.entity.message.Message;

import java.util.List;

@Permission(PermissionMask = 8)
public class Reload implements ICommand {
    @Override
    public void onCommand(Main instance, Message message, List<IParameter> args) {
        instance.loadConfig();
        System.out.println("Config reloaded!");
    }

    @Override
    public Class<? extends IParameter>[] requiredParameters() {
        return new Class[0];
    }
}
