package me.inao.discordbot.server;

import me.inao.discordbot.enums.KeyExchangeType;
import me.inao.discordbot.ifaces.IKeyExchange;
import org.reflections.Reflections;

import java.util.Set;

public class KeyExchangeDecide {
    public IKeyExchange getKeyExchange(String keyExchange){
        KeyExchangeType type = KeyExchangeType.valueOf(keyExchange);
        Reflections reflections = new Reflections("me.inao.discordbot.server.packets");
        Set<Class<? extends IKeyExchange>> classSet = reflections.getSubTypesOf(IKeyExchange.class);
        return null;
    }
}
