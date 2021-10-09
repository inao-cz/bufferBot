package me.inao.discordbot.server.packets;

import me.inao.discordbot.enums.KeyExchangeType;
import me.inao.discordbot.enums.PacketType;
import me.inao.discordbot.ifaces.IKeyExchange;
import me.inao.discordbot.ifaces.IPacket;
import me.inao.discordbot.server.Session;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.KeyAgreement;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class DiffieHellmanKeyExchangePacket implements IPacket, IKeyExchange {
    private KeyPair keyPair;

    @Override
    public PacketType packetType() {
        return PacketType.KEY_EXCHANGE;
    }

    @Override
    public KeyExchangeType keyExchangeType() {
        return KeyExchangeType.DIFFIE_HELLMAN;
    }

    @Override
    public void initKeys() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDH", "BC");
            generator.initialize(new ECGenParameterSpec("prime256v1"), new SecureRandom());
            keyPair = generator.generateKeyPair();
        } catch (Exception e) {
            keyPair = null;
            e.printStackTrace();
        }
    }

    @Override
    public void createKeyAgreement(BufferedReader reader, PrintWriter writer, Session session) {

    }

    public PublicKey getPubKeyFromBytes(byte[] raw) {
        ECParameterSpec paramsSpec = ECNamedCurveTable.getParameterSpec("prime256v1");
        ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(paramsSpec.getCurve().decodePoint(raw), paramsSpec);
        try {
            KeyFactory factory = KeyFactory.getInstance("ECDH", "BC");
            return factory.generatePublic(pubKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey getPrivKeyFromBytes(byte[] raw) {
        org.bouncycastle.jce.spec.ECParameterSpec paramsSpec = ECNamedCurveTable.getParameterSpec("prime256v1");
        ECPrivateKeySpec privKeySpec = new ECPrivateKeySpec(new BigInteger(raw), paramsSpec);
        try {
            KeyFactory factory = KeyFactory.getInstance("ECDH", "BC");
            return factory.generatePrivate(privKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] calculateKey(byte[] privKey, byte[] pubKey) {
        try {
            KeyAgreement secretAgreement = KeyAgreement.getInstance("ECDH", "BC");
            secretAgreement.init(getPrivKeyFromBytes(privKey));
            secretAgreement.doPhase(getPubKeyFromBytes(pubKey), true);
            return secretAgreement.generateSecret();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] calculateKey(byte[] pubKey) {
        try {
            KeyAgreement secretAgreement = KeyAgreement.getInstance("ECDH", "BC");
            secretAgreement.init(keyPair.getPrivate());
            secretAgreement.doPhase(getPubKeyFromBytes(pubKey), true);
            return secretAgreement.generateSecret();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encodeBase64(byte[] raw) {
        return new String(Base64.encode(raw));
    }
}
