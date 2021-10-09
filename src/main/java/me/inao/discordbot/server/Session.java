package me.inao.discordbot.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.inao.discordbot.ifaces.IKeyExchange;

import java.util.Date;

@Getter
@RequiredArgsConstructor
public class Session {
    @Setter
    private Date validity;

    @Setter
    private String token;

    @Setter
    private byte[] secret;

    private final IKeyExchange keyExchange;
}