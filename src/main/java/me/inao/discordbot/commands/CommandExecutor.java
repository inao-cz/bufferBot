package me.inao.discordbot.commands;

import me.inao.discordbot.Main;
import me.inao.discordbot.annotation.Permission;
import me.inao.discordbot.ifaces.ICommand;
import me.inao.discordbot.ifaces.IParameter;
import me.inao.discordbot.util.MessageSender;
import org.javacord.api.entity.message.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandExecutor {
    /**
     * Command execution.
     * Using Reflection API with parameter parsing (automatically ;) )
     */
    public void execute(Message message, Main instance){
        CommandParser parser = new CommandParser();
        parser.setApi(instance.getApi());
        parser.setLoader(instance.getLoader());
        parser.setCommandPrefix(String.valueOf(instance.getConfig().getPrefix()));
        String[] parsed = parser.getParsedCommand(message.getContent());
        List<IParameter> parameterList = parser.getParsedValues(parsed);

        String cmd = "";
        for(String parse : parsed){
            if(parser.checkForCommandPair(parse)){
                cmd = parse;
                break;
            }
        }

        String finalCmd = cmd.replace(String.valueOf(instance.getConfig().getPrefix()), "");
        Optional<ICommand> command = instance.getLoader().getLoadedCommands().entrySet()
                .stream()
                .filter(entry -> finalCmd.equalsIgnoreCase(entry.getKey()))
                .map(Map.Entry::getValue)
                .findAny();

        if(command.isPresent()) {
            if(command.get().getClass().isAnnotationPresent(Permission.class)){
                if (!instance.getPermissionable().checkPermission(message, command.get().getClass()))
                    return;
            }
            List<Boolean> requiredAreProvided = new ArrayList<>();
            for(Class<? extends IParameter> param : command.get().requiredParameters()){
                if(parameterList.stream().anyMatch(param::isInstance)){
                    requiredAreProvided.add(true);
                }else{
                    new MessageSender("Missing arguments", "There is/are an argument(s) missing.", Color.RED, message.getChannel());
                    break;
                }
            }
            command.get().onCommand(instance, message, parameterList);
        } else {
            message.getChannel().sendMessage("Unknown command!");
        }
    }
}
