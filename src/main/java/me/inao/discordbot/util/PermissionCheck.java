package me.inao.discordbot.util;

import lombok.AllArgsConstructor;
import me.inao.discordbot.Main;
import me.inao.discordbot.annotation.Permission;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

@AllArgsConstructor
public class PermissionCheck{
    private final Main main;

    public boolean hasPermission(Server server, User user, String command){
        Permission permission = main.getLoader().getLoadedCommands().get(command).getClass().getDeclaredAnnotation(Permission.class);
        int configPerms = main.getConfig().getCommandPerms(command) == null ? -1 : Integer.parseInt(main.getConfig().getCommandPerms(command));
        if(configPerms == -1 && permission == null){
            return true;
        }
        else if(configPerms > 0){
            return server.hasPermission(user, Permissions.fromBitmask(configPerms).getAllowedPermission().iterator().next());
        }else if(configPerms == 0){
            return true;
        }
        return server.hasPermission(user, Permissions.fromBitmask(permission.PermissionMask()).getAllowedPermission().iterator().next());
    }
}
