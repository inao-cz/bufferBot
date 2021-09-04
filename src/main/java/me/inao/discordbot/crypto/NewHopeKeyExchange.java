package me.inao.discordbot.crypto;

import lombok.Getter;
import lombok.Setter;
import me.inao.discordbot.enums.KeyExchangeType;
import me.inao.discordbot.ifaces.IKeyExchange;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.newhope.NHAgreement;
import org.bouncycastle.pqc.crypto.newhope.NHKeyPairGenerator;
import org.bouncycastle.pqc.crypto.newhope.NHPublicKeyParameters;
import org.bouncycastle.util.encoders.Base64;

import java.security.SecureRandom;

public class NewHopeKeyExchange implements IKeyExchange {
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

    public void createKeyAgreement() {
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
