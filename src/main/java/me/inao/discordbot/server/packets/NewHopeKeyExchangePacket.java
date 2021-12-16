package me.inao.discordbot.server.packets;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import me.inao.discordbot.enums.KeyExchangeType;
import me.inao.discordbot.enums.PacketType;
import me.inao.discordbot.ifaces.IKeyExchange;
import me.inao.discordbot.ifaces.IPacket;
import me.inao.discordbot.server.Session;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.newhope.NHAgreement;
import org.bouncycastle.pqc.crypto.newhope.NHKeyPairGenerator;
import org.bouncycastle.pqc.crypto.newhope.NHPublicKeyParameters;
import org.bouncycastle.util.encoders.Base64;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.security.SecureRandom;

public class NewHopeKeyExchangePacket implements IPacket, IKeyExchange {
    @Setter
    private JsonObject packet;
    @Override
    public PacketType packetType() {
        return PacketType.KEY_EXCHANGE;
    }

    @Getter
    private AsymmetricCipherKeyPair exchangePair;

    @Getter
    @Setter
    private String clientEncodedPubKey = null;

    @Getter
    private byte[] sharedSecret = null;

    @Override
    public KeyExchangeType keyExchangeType() {
        return KeyExchangeType.NEWHOPE;
    }

    public void initKeys() {
        NHKeyPairGenerator nhKeyPairGenerator = new NHKeyPairGenerator();
        nhKeyPairGenerator.init(new KeyGenerationParameters(new SecureRandom(), 2048));
        exchangePair = nhKeyPairGenerator.generateKeyPair();
    }

    @Override
    public void createKeyAgreement(BufferedReader reader, PrintWriter writer, Session session) {
        initKeys();
        writer.println(getPublicKeyEncodedString());
        try{
            String ret = reader.readLine();
            setClientEncodedPubKey(ret);
            calculateSharedSecret();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void calculateSharedSecret() {
        NHPublicKeyParameters parameters = new NHPublicKeyParameters(Base64.decode(clientEncodedPubKey));
        NHAgreement nhAgreement = new NHAgreement();
        nhAgreement.init(exchangePair.getPrivate());
        sharedSecret = nhAgreement.calculateAgreement(parameters);
        exchangePair = null;
        clientEncodedPubKey = null;
    }

    public String getPublicKeyEncodedString() {
        return new String(Base64.encode(((NHPublicKeyParameters) exchangePair.getPublic()).getPubData()));
    }

    public byte[] getPublicKeyEncodedBytes() {
        return Base64.encode(((NHPublicKeyParameters) exchangePair.getPublic()).getPubData());
    }

    public NHPublicKeyParameters getNhPublicKeyParametersFromString(String base64) {
        return new NHPublicKeyParameters(Base64.decode(base64));
    }

    public String encodeBase64(byte[] raw) {
        return new String(Base64.encode(raw));
    }
}
