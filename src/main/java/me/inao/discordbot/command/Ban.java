package me.inao.discordbot.command;

import me.inao.discordbot.Main;
import me.inao.discordbot.annotation.Permission;
import me.inao.discordbot.commands.params.SilentParam;
import me.inao.discordbot.commands.params.UserParam;
import me.inao.discordbot.exception.NoSuchServerException;
import me.inao.discordbot.exception.NoSuchServerTextChannelException;
import me.inao.discordbot.ifaces.ICommand;
import me.inao.discordbot.ifaces.IParameter;
import me.inao.discordbot.util.MessageSender;
import me.inao.discordbot.util.ParamsUtil;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.List;

@Permission(PermissionMask = 4)
public class Ban implements ICommand {
    @Override
    public void onCommand(Main instance, Message message, List<IParameter> args) {
        boolean silent = ParamsUtil.filterObject(SilentParam.class, args) != null;
        List<User> users = ParamsUtil.filterObject(UserParam.class, args) != null ? ((UserParam) ParamsUtil.filterObject(UserParam.class, args).get(0)).getParsedUsers() : null;
        if (users != null) {
            users.forEach(user -> {
                message.getServer().orElseThrow(NoSuchServerException::new).banUser(user);
                if (!silent) {
                    new MessageSender("User banned.", instance.getConfig().getMessage("ban", "success").replace("%_user_%", user.getDiscriminatedName()), Color.BLACK, message.getServer().get().getChannelsByName(instance.getConfig().getCommandRoom(this.getClass().getSimpleName())).get(0).asServerTextChannel().orElseThrow(NoSuchServerTextChannelException::new));
                }
            });
        }
    }

    @Override
    public Class<? extends IParameter>[] requiredParameters() {
        return new Class[]{
                UserParam.class
        };
    }
}
