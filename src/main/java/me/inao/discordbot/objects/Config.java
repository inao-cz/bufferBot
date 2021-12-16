package me.inao.discordbot.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import me.inao.discordbot.util.ExceptionCatcher;

public class Config {
    @SerializedName("apiKey")
    @Expose
    @Getter
    private String apiKey;

    @SerializedName("loggingLevel")
    @Expose
    @Getter
    private String loggingLevel;

    @SerializedName("prefix")
    @Expose
    @Getter
    private String prefix;

    @SerializedName("botState")
    @Expose
    @Getter
    private int state;

    @SerializedName("features")
    @Expose
    JsonObject features;

    @SerializedName("commands")
    @Expose
    JsonObject commands;

    @SerializedName("server")
    @Expose
    JsonObject server;

    @SerializedName("messages")
    @Expose
    JsonObject messages;

    @SerializedName("setup")
    @Expose
    @Getter
    JsonObject setup;

    public boolean isCommandEnabled(String name){
        try{
            return commands.getAsJsonObject(name).get("enabled").getAsBoolean();
        }catch (Exception e){
            System.out.println(name);
            e.printStackTrace();
        }
        return false;
    }

    public JsonPrimitive getCommand(String name){
        return commands.getAsJsonPrimitive(name);
    }

    public String getCommandMessage(String name){
        return commands.getAsJsonObject(name).get("message").getAsString();
    }
    public String getCommandRoom(String name){
        return commands.getAsJsonObject(name).get("channelName").getAsString();
    }
    public String getCommandPerms(String name){
        try{
            return commands.getAsJsonObject(name).get("perms").getAsString();
        }catch (Exception ignored){}
        return null;
    }
    public String getServerProperty(String name){
        return server.get(name).getAsString();
    }

    public String getServerGpgProperty(String property){
        return server.get("gpg").getAsJsonObject().getAsJsonPrimitive(property).getAsString();
    }

    public JsonArray getServerTokens(){
        return server.get("tokens").getAsJsonArray();
    }

    public boolean isFeatureEnabled(String name){
        try{
            return features.getAsJsonObject(name).get("enabled").getAsBoolean();
        }catch (Exception e){
            System.out.println(name);
            System.out.println("Feature not found in config :(");
        }
        return false;
    }
    public String getFeatureData(String name){
        try{
            return features.getAsJsonObject(name).get("data").getAsString();
        }catch (Exception e){
            System.out.println(name);
            System.out.println("Feature not found in config :(");
        }
        return null;
    }
    public String getFeatureChannel(String name){
        try{
            return features.getAsJsonObject(name).get("room").getAsString();
        }catch (Exception e) {
            System.out.println(name);
            System.out.println("No room set :(");
        }
        return null;
    }

    public Object getFeatureValue(String feature, String value){
        try{
            return features.getAsJsonObject(feature).get(value).getAsString();
        }catch (Exception e){
            new ExceptionCatcher(e);
        }
        return null;
    }

    public String getMessage(String module, String message){
        try{
            return messages.getAsJsonObject(module).get(message).getAsString();
        }catch (Exception e){
            System.out.println("Message not found :(");
        }
        return null;
    }
}
