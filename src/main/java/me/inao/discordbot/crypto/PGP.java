package me.inao.discordbot.crypto;

import lombok.RequiredArgsConstructor;
import me.inao.discordbot.Main;
import me.inao.discordbot.util.ExceptionCatcher;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.bcpg.sig.Features;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;
import org.bouncycastle.util.encoders.Base64;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

@RequiredArgsConstructor
public class PGP {
    private final Main main;

    public static final String PGP_PATH = "./generated/pgp/";
    private final String pgpPublicKeyFile = "public_key";
    private final String pgpPrivateKeyFile = "private_key";
    private final String pgpFingerprint = "fingerprint";

    public boolean checkIfFilesExists() {
        return ((new File(PGP_PATH).exists()) && (new File(PGP_PATH + pgpPublicKeyFile).exists() && (new File(PGP_PATH + pgpPrivateKeyFile)).exists()));
    }

    public void generateKeys() {
        if(checkIfFilesExists()) return;

        char[] pass = main.getConfig().getServerGpgProperty("pass").toCharArray();
        try{
            if(!(new File(PGP_PATH).exists())){
                if((new File(PGP_PATH)).mkdir()){
                    System.out.println("PGP Directory created.");
                }
            }
            PGPKeyRingGenerator gen = pgpKeyRingGenerator(main.getConfig().getServerGpgProperty("email"), pass);

            PGPPublicKeyRing pgpPublicKey = gen.generatePublicKeyRing();
            ArmoredOutputStream outputStream = new ArmoredOutputStream(new BufferedOutputStream(new FileOutputStream(PGP_PATH + pgpPublicKeyFile)));
            pgpPublicKey.encode(outputStream);
            outputStream.close();

            PGPSecretKeyRing pgpPrivateKey = gen.generateSecretKeyRing();
            outputStream = new ArmoredOutputStream(new BufferedOutputStream(new FileOutputStream(PGP_PATH + pgpPrivateKeyFile)));
            pgpPrivateKey.encode(outputStream);
            outputStream.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(PGP_PATH + pgpFingerprint));
            writer.write(new String(Base64.encode(pgpPublicKey.getPublicKey().getFingerprint())));
            writer.close();

        }catch (Exception e){
             new ExceptionCatcher(e);
        }
    }

    private PGPKeyRingGenerator pgpKeyRingGenerator(String email, char[] pass) throws Exception{
        RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        generator.init(new RSAKeyGenerationParameters(BigInteger.valueOf(0x1001), new SecureRandom(), 4096, 12));
        PGPKeyPair pgpSigningKey = new BcPGPKeyPair(PGPPublicKey.RSA_SIGN, generator.generateKeyPair(), new Date());
        PGPKeyPair pgpEncryptionKey = new BcPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, generator.generateKeyPair(), new Date());
        PGPSignatureSubpacketGenerator signatureSubpacketGenerator = new PGPSignatureSubpacketGenerator();
        signatureSubpacketGenerator.setKeyFlags(false, KeyFlags.SIGN_DATA|KeyFlags.CERTIFY_OTHER);
        signatureSubpacketGenerator.setPreferredSymmetricAlgorithms(false, new int[]{
                SymmetricKeyAlgorithmTags.AES_256,
                SymmetricKeyAlgorithmTags.AES_192
        });
        signatureSubpacketGenerator.setPreferredHashAlgorithms(false, new int[]{
                HashAlgorithmTags.SHA512,
                HashAlgorithmTags.SHA256
        });
        signatureSubpacketGenerator.setFeature(false, Features.FEATURE_MODIFICATION_DETECTION);
        PGPSignatureSubpacketGenerator signatureSubpacketGenerator1 = new PGPSignatureSubpacketGenerator();
        signatureSubpacketGenerator1.setKeyFlags(false, KeyFlags.ENCRYPT_COMMS|KeyFlags.ENCRYPT_STORAGE);
        PGPDigestCalculator sha1Digest = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA1);
        PGPDigestCalculator sha512Digest = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA512);
        PBESecretKeyEncryptor pbeSecretKeyEncryptor = ((new BcPBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256, sha512Digest, 255)).build(pass));
        PGPKeyRingGenerator keyRingGen =
                new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, pgpSigningKey,
                        email, sha1Digest, signatureSubpacketGenerator.generate(), null,
                        new BcPGPContentSignerBuilder(pgpSigningKey.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1), pbeSecretKeyEncryptor);
        keyRingGen.addSubKey(pgpEncryptionKey, signatureSubpacketGenerator1.generate(), null);
        return keyRingGen;
    }
}
