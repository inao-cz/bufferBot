package me.inao.discordbot.command;

import me.inao.discordbot.Main;
import me.inao.discordbot.annotation.Permission;
import me.inao.discordbot.commands.params.CountParam;
import me.inao.discordbot.ifaces.ICommand;
import me.inao.discordbot.ifaces.IParameter;
import me.inao.discordbot.util.ParamsUtil;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;

import java.util.List;

@Permission(PermissionMask = 8192)
public class Delete implements ICommand {
    @Override
    public void onCommand(Main instance, Message message, List<IParameter> args) {
        int count = ParamsUtil.filterObject(CountParam.class, args) != null ? ((CountParam) ParamsUtil.filterObject(CountParam.class, args).get(0)).getCount() : 0;
        if(count > 0){
            message.getChannel().getMessages(count + 1).thenAcceptAsync(MessageSet::deleteAll);
        }
    }

    @Override
    public Class<? extends IParameter>[] requiredParameters() {
        return new Class[]{ CountParam.class };
    }
}
