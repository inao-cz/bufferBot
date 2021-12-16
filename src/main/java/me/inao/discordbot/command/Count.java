package me.inao.discordbot.command;

import me.inao.discordbot.Main;
import me.inao.discordbot.annotation.Permission;
import me.inao.discordbot.ifaces.ICommand;
import me.inao.discordbot.ifaces.IParameter;
import org.javacord.api.entity.message.Message;

import java.util.List;
@Permission(PermissionMask = 16)
public class Count implements ICommand {

    @Override
    public void onCommand(Main instance, Message message, List<IParameter> args) {
//        if(!instance.getPermissionable().hasPermission(message, this.getClass())){
//            new MessageSender("No Permissions", instance.getConfig().getMessage("generic", "no_perms"), Color.RED, message.getChannel());
//            return;
//        }
//        if(args.size() < 1){
//            new MessageSender("Not enough arguments", instance.getConfig().getMessage("generic", "no_args"), Color.RED, message.getChannel());
//            return;
//        }
//        long finish = 1337;
//        long prev = 0;
//        try{
//            finish = Long.decode(args[0]);
//            if(args.size() == 2){
//                prev = Long.decode(args[1]);
//            }
//        }catch (Exception e){
//            new ExceptionCatcher(e);
//        }
//        if(finish < 1){
//            return;
//        }
//        if(instance.getCountgame() != null){
//            return;
//        }
//        long finalFinish = finish;
//        long prevFinish = prev;
//        message.getServer().ifPresent(server-> instance.setCountgame(new Countgame(server.getChannelsByName(instance.getConfig().getCommandRoom(this.getClass().getSimpleName())).get(0).asServerTextChannel().orElseThrow(NoSuchServerTextChannelException::new), finalFinish, instance, prevFinish)));

    }

    @Override
    public Class<? extends IParameter>[] requiredParameters() {
        return new Class[0];
    }
}
