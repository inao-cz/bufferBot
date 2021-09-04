package me.inao.discordbot.ifaces;


import me.inao.discordbot.enums.KeyExchangeType;

public interface IKeyExchange {
    KeyExchangeType keyExchangeType();
    void initKeys();
    void createKeyAgreement();
}
