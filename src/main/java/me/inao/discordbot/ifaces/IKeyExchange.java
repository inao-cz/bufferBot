package me.inao.discordbot.ifaces;


import me.inao.discordbot.enums.KeyExchangeType;
import me.inao.discordbot.server.Session;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface IKeyExchange {
    KeyExchangeType keyExchangeType();
    void initKeys();
    void createKeyAgreement(BufferedReader reader, PrintWriter writer, Session session);
}
