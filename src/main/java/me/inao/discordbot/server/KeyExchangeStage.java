package me.inao.discordbot.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.inao.discordbot.enums.ClientStatus;
import me.inao.discordbot.ifaces.IKeyExchange;

@RequiredArgsConstructor
@Getter
@Setter
public class KeyExchangeStage {
    private ClientStatus status;
    private final Session session;
    private IKeyExchange keyExchange;

    public Object sendDataByStage() {
        switch (status){
            case CONNECTED:
                //return getDataWhenClientConnects();
            case PGP_VERIFIED:

        }
        return null;
    }
}
