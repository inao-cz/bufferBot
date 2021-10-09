package me.inao.discordbot.server.packets;

import com.google.gson.JsonObject;
import me.inao.discordbot.enums.PacketType;
import me.inao.discordbot.ifaces.IPacket;

public class InitPacket implements IPacket {
    private JsonObject packet;
    @Override
    public PacketType packetType() {
        return PacketType.UNSECURE;
    }
}
