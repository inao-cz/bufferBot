package me.inao.discordbot;

import com.google.gson.Gson;
import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;
import lombok.Setter;
import me.inao.discordbot.objects.Config;
import me.inao.discordbot.objects.Countgame;
import me.inao.discordbot.server.Server;
import me.inao.discordbot.util.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.user.UserStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Getter
public class Main {
    private Config config;
    private DiscordApi api;
    @Setter
    private Countgame countgame = null;
    private final SQLite sqlite = new SQLite();
    private final Logger logger = LogManager.getLogger("me.inao.discordbot");
    private final boolean debug = false;
    @Getter
    Loader loader;
    @Getter
    Permissionable permissionable = new Permissionable(this);

    private final UserStatus[] status = {UserStatus.ONLINE, UserStatus.IDLE, UserStatus.DO_NOT_DISTURB, UserStatus.INVISIBLE};

    public static void main(String[] args) {
        new Main().starter();
    }

    public void starter() {
        loadConfig();
        DiscordApiBuilder apiBuilder = new DiscordApiBuilder().setToken(config.getApiKey());
        apiBuilder.setIntents(Intent.GUILDS, Intent.GUILD_BANS, Intent.GUILD_MESSAGE_REACTIONS, Intent.GUILD_MESSAGES, Intent.GUILD_MEMBERS);
        loader = new Loader(this);
        setLogger(config.getLoggingLevel());
        try {
            loader.loadListeners("me.inao.discordbot.event", apiBuilder).run();
            new Thread(loader.loadParams("me.inao.discordbot.commands.params")).start();
            loader.loadCommands("me.inao.discordbot.command").run();
        } catch (Exception e) {
            new ExceptionCatcher(e);
            new me.inao.discordbot.util.Logger(this, true, false, "Loading problem", "Cannot load bot (Event/Command). Shutting down!", Level.FATAL);
            System.exit(0);
        }
        this.api = apiBuilder.login().join();
        api.updateStatus(status[config.getState()]);
        api.updateActivity(ActivityType.PLAYING, (debug) ? "Launched in debug mode " + EmojiParser.parseToUnicode(":bug:") : "with Raspberry and Java" + EmojiParser.parseToUnicode(":yum:"));
        api.setAutomaticMessageCacheCleanupEnabled(true);
        new Server(this).start();
        api.setMessageCacheSize(15, 3600);
        new me.inao.discordbot.util.Logger(this, true, true, "Bot start", "Loaded. Invite: " + api.createBotInvite().replace("permissions=0", "permissions=8"), Level.INFO);
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
        System.gc();
    }

    public void loadConfig() {
        Gson gson = new Gson();
        String[] files = {"config.local.json", "config.json"};
        try {
            BufferedReader reader = null;
            for (String file : files) {
                File f = new File(file);
                if (f.exists()) {
                    reader = new BufferedReader(new FileReader(f.getName()));
                    break;
                }
            }
            if (reader == null) {
                System.exit(-2);
            }
            config = gson.fromJson(reader, Config.class);
        } catch (Exception e) {
            new ExceptionCatcher(e);
        }
    }

    private void setLogger(String value) {
        Configurator.setAllLevels(logger.getName(), Level.getLevel(value));
    }
}
