package me.inao.discordbot.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.inao.discordbot.Main;
import me.inao.discordbot.commands.CommandExecutor;
import me.inao.discordbot.commands.CommandParser;
import me.inao.discordbot.exception.NoSuchServerTextChannelException;
import me.inao.discordbot.ifaces.IListener;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

@Getter
@RequiredArgsConstructor
public class MessageEvent implements MessageCreateListener, IListener {
    private final Main instance;
    private final CommandExecutor executor = new CommandExecutor();
    private final CommandParser parser = new CommandParser();

    @Override
    public void onMessageCreate(MessageCreateEvent e) {
        if(instance.getConfig().getCommand("slashCommands").getAsBoolean() || e.getMessageAuthor().isBotUser() || e.getMessage().isPrivateMessage()){
            return;
        }
        if (instance.getConfig().isFeatureEnabled("channelLimit") || instance.getConfig().isCommandEnabled("Count")) {
            if (e.getMessage().getAuthor().isBotUser()) {
                e.getMessage().delete();
                return;
            }
            if (e.getMessageContent().startsWith(String.valueOf(instance.getConfig().getPrefix()))) {
                if (!(e.getMessageAuthor().canManageMessagesInTextChannel()) && !(e.getServerTextChannel().orElseThrow(NoSuchServerTextChannelException::new).getName().equals(instance.getConfig().getFeatureData("channelLimit")))) {
                    e.getMessage().delete();
                    return;
                }
            }
            if (e.getMessage().getChannel().asServerTextChannel().orElseThrow(NoSuchServerTextChannelException::new).getName().equals(instance.getConfig().getCommandRoom("Count"))) {
//                if (instance.getConfig().isFeatureEnabled("antiLink")) {
//                    Role muteRole = e.getServer().orElseThrow(NoSuchServerException::new).getRolesByName(instance.getConfig().getFeatureData("tempMute")).get(0);
//                    e.getMessageAuthor().asUser().orElseThrow(NoSuchUserException::new).addRole(muteRole);
//                    new Timer().schedule(new UnmuteBuffer(e.getMessageAuthor().asUser().orElseThrow(NoSuchUserException::new), muteRole), (1000 * 60 * 60));
//                }
                if (!(e.getMessageAuthor().canManageMessagesInTextChannel())) {
                    if (instance.getCountgame() == null) return;
                }
                if (!(e.getMessageContent().contains(String.valueOf(instance.getConfig().getPrefix()))) && !(instance.getCountgame() == null))
                    instance.getCountgame().addCount(e.getMessage());
            }
        }
        if (!e.getMessageContent().isEmpty() && e.getMessage().getContent().startsWith(String.valueOf(instance.getConfig().getPrefix()))) {
            executor.execute(e.getMessage(), instance);
        }
    }
}
