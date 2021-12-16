package me.inao.discordbot.ifaces;

public interface IRequest {
    void onRequest(String ip, IPacket packet);
    int getType();
}
