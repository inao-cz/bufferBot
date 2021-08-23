package me.inao.discordbot.command;

import me.inao.discordbot.Main;
import me.inao.discordbot.annotation.Permission;
import me.inao.discordbot.ifaces.ICommand;
import me.inao.discordbot.ifaces.IParameter;
import me.inao.discordbot.util.MessageSender;
import org.javacord.api.entity.message.Message;

import java.awt.*;
import java.util.List;

@Permission(PermissionMask = 8)
public class Ping implements ICommand {
    @Override
    public void onCommand(Main instance, Message message, List<IParameter> args) {
        new MessageSender("pong", "pong", Color.RED, message.getChannel());
    }

    @Override
    public Class<? extends IParameter>[] requiredParameters() {
        return new Class[0];
    }
}
