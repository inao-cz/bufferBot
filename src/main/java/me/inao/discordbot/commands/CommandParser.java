package me.inao.discordbot.commands;


import lombok.Getter;
import lombok.Setter;
import me.inao.discordbot.ifaces.IParameter;
import org.javacord.api.DiscordApi;

import java.util.*;

public class CommandParser {
    @Getter
    @Setter
    private HashMap<List<String>, IParameter> map = null;

    @Setter
    private DiscordApi api;

    @Setter
    private String commandPrefix;

    public List<IParameter> getParsedValues(String[] parts){
        List<IParameter> parameters = new ArrayList<>();
        if(map == null) return null;
        System.out.println(Arrays.toString(parts));
        for (String part : parts){
            if(checkForCommandPair(part)) continue;
            String[] partSplit = part.split("\\s");
            Optional<IParameter> parameter = map.entrySet().stream().filter(listIParameterEntry -> listIParameterEntry.getKey().stream().anyMatch(entry -> entry.matches(partSplit[0]))).map(Map.Entry::getValue).findAny();
            if(parameter.isPresent()){
                String[] modifiedArray = Arrays.copyOfRange(partSplit, 1, partSplit.length);
                IParameter param = parameter.get();
                param.onParse(this.api, String.join(" ", Arrays.asList(modifiedArray)));
                parameters.add(param);
            }
        }
        return parameters;
    }

    public String[] getParsedCommand(String message){
        String[] parsedCommand = message.split("\\s-");
        return parsedCommand;
    }

    public boolean checkForParamPair(String splitMessage){
        return splitMessage.matches("^[\\-].+\\s.+$");
    }

    public boolean checkForCommandPair(String splitMessage){
        return splitMessage.matches("^" + commandPrefix + ".+(\\s.+)?$");
    }
}
