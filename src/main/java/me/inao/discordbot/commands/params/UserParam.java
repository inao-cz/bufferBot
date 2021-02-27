package me.inao.discordbot.commands.params;

import lombok.Getter;
import me.inao.discordbot.ifaces.IParameter;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.DiscordRegexPattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

@Getter
public class UserParam implements IParameter {
    private List<User> parsedUsers = new ArrayList<>();

    /**
     * Parse algorithm. Possible delimiters ',:;-_/'
     */
    @Override
    public void onParse(DiscordApi api, String value) {
        System.out.println(value);
        String[] values = value.split("[,:;\\-_/\\s]");
        for (String val : values){
            String id = parseIdFromMention(val);
            if(id != null){
                parsedUsers.add( api.getUserById(id).join());
            }
        }
        System.out.println(Arrays.toString(parsedUsers.toArray()));
    }

    /**
     * Parse Client ID from Mention string.
     */
    private String parseIdFromMention(String value){
        String val = null;
        if(value.matches(DiscordRegexPattern.USER_MENTION.pattern())){
            Matcher matcher = DiscordRegexPattern.USER_MENTION.matcher(value);
            matcher.find();
            return matcher.group("id");
        }
        return val;
    }

    @Override
    public String[] getIdentifiers() {
        return new String[]{"u", "-user", "-users"};
    }
}
