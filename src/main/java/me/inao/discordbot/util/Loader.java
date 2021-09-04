package me.inao.discordbot.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.inao.discordbot.Main;
import me.inao.discordbot.ifaces.ICommand;
import me.inao.discordbot.ifaces.IListener;
import me.inao.discordbot.ifaces.IParameter;
import org.apache.logging.log4j.Level;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.listener.GloballyAttachableListener;
import org.reflections.Reflections;

import java.util.*;

@RequiredArgsConstructor
public class Loader {
    @Getter
    private Map<String, ICommand> loadedCommands;

    @Getter
    private final HashMap<List<String>, IParameter> parameterList = new HashMap<>();

    private final Main main;

    public Runnable loadListeners(String prefix, DiscordApiBuilder builder) {
        return () -> {
            new Logger(main, "Initiating ListenerLoader..", Level.DEBUG);
            Reflections refl = new Reflections(prefix);
            Set<Class<? extends IListener>> classes = refl.getSubTypesOf(IListener.class);
            for (Class<? extends IListener> listener : classes) {
                try {
                    builder.addListener((GloballyAttachableListener) listener.getDeclaredConstructor(new Class[]{Main.class}).newInstance(main));
                    new Logger(main, "Loaded event: " + listener.getSimpleName(), Level.DEBUG);
                } catch (Exception e) {
                    new Logger(main, e.getMessage(), Level.ERROR);
                }
            }
            new Logger(main, "EventLoader done.", Level.DEBUG);
        };
    }

    public Runnable loadCommands(String prefix) {
        return () -> {
            new Logger(main, "Initiating CommandLoader..", Level.DEBUG);
            Reflections refl = new Reflections(prefix);
            Set<Class<? extends ICommand>> classes = refl.getSubTypesOf(ICommand.class);
            loadedCommands = new HashMap<>(classes.size());
            for (Class<? extends ICommand> command : classes) {
                if (main.getConfig().isCommandEnabled(command.getSimpleName())) {
                    try {
                        ICommand instance = command.getDeclaredConstructor().newInstance();
                        loadedCommands.put(command.getSimpleName(), instance);
                        if(main.getConfig().getCommand("slashCommands").getAsBoolean()){
                            String description = "Without description";
                            try{
                                description = main.getConfig().getCommand(command.getSimpleName()).getAsJsonObject().get("description").getAsString();
                            }catch (Exception ignored){}
                            SlashCommand.with(command.getSimpleName().toLowerCase(Locale.ROOT), description
                            ).createForServer(main.getApi().getServers().iterator().next()).get();
                        }
                        new Logger(main, "Loaded command: " + command.getSimpleName(), Level.DEBUG);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Logger(main, "Command loading failed: " + e.getLocalizedMessage(), Level.ERROR);
                    }
                }
            }
            new Logger(main, "CommandLoader done.", Level.DEBUG);
        };
    }

    public Runnable loadParams(String prefix) {
       return () -> {
           new Logger(main, "Initiating Parameters..", Level.DEBUG);
           Reflections reflections = new Reflections(prefix);
           Set<Class<? extends IParameter>> classes = reflections.getSubTypesOf(IParameter.class);

           for (Class<? extends IParameter> param : classes) {
               try {
                   IParameter instance = param.getDeclaredConstructor().newInstance();
                   parameterList.put(Arrays.asList(instance.getIdentifiers()), instance);
                   new Logger(main, "Loaded parameter: " + param.getSimpleName(), Level.DEBUG);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
           new Logger(main, "ParameterLoader done.", Level.DEBUG);
       };
    }
}
