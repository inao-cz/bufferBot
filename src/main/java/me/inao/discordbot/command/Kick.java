package me.inao.discordbot.command;

import me.inao.discordbot.Main;
import me.inao.discordbot.annotation.Permission;
import me.inao.discordbot.commands.params.UserParam;
import me.inao.discordbot.exception.NoSuchServerException;
import me.inao.discordbot.ifaces.ICommand;
import me.inao.discordbot.ifaces.IParameter;
import me.inao.discordbot.util.ParamsUtil;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.util.List;

@Permission(PermissionMask = 2)
public class Kick implements ICommand {

    @Override
    public void onCommand(Main instance, Message message, List<IParameter> args) {
        List<User> users = ParamsUtil.filterObject(UserParam.class, args) != null ? ((UserParam) ParamsUtil.filterObject(UserParam.class, args).get(0)).getParsedUsers() : null;
        if (users != null) {
            users.forEach(user -> message.getServer().orElseThrow(NoSuchServerException::new).kickUser(user));
        }
    }

    @Override
    public Class<? extends IParameter>[] requiredParameters() {
        return new Class[]{
                UserParam.class
        };
    }
}
