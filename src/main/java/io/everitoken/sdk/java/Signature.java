package io.everitoken.sdk.java;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.apache.commons.lang3.ArrayUtils;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;

import io.everitoken.sdk.java.exceptions.InvalidSignatureException;
import io.everitoken.sdk.java.exceptions.PublicKeyRecoverFailureException;
import io.everitoken.sdk.java.exceptions.RecoverIDNotFoundException;

public class Signature {
    private static final String K1_PREFIX = "SIG_K1_";
    public static int BUFFER_LENGTH = 65;
    private ECKey.ECDSASignature signature;
    private Integer recId;

    public Signature(BigInteger r, BigInteger s) {
        signature = new ECKey.ECDSASignature(r, s);
    }

    /**
     * @param signatureBytes
     *            Signature in bytes
     * @return {@link Signature}
     */
    public static Signature of(byte[] signatureBytes) {

        int recId = (int) signatureBytes[0] - 4 - 27;
        BigInteger r = new BigInteger(ArrayUtils.subarray(signatureBytes, 1, 33));
        BigInteger s = new BigInteger(ArrayUtils.subarray(signatureBytes, 33, signatureBytes.length));
        Signature sig = new Signature(r, s);
        sig.setRecId(recId);

        return sig;
    }

    /**
     * @param signature
     *            Signature in string
     * @return {@link Signature}
     */
    public static Signature of(String signature) {
        if (!signature.startsWith(K1_PREFIX)) {
            throw new InvalidSignatureException(
                    String.format("Only support signature prefixed with \"%s\"", K1_PREFIX));
        }

        String signatureWithoutPrefix = signature.substring(K1_PREFIX.length());
        byte[] signatureBytes = Utils.base58CheckDecode(signatureWithoutPrefix, "K1");

        if (signatureBytes.length != BUFFER_LENGTH) {
            throw new InvalidSignatureException(String.format("Content of signature must be %s", BUFFER_LENGTH));
        }

        return Signature.of(signatureBytes);
    }

    public static Signature signHash(byte[] hash, @NotNull PrivateKey key) {
        checkHashLength(hash);

        // init deterministic k calculator
        Signer signer = new Signer(new HMacDSAKCalculator(new SHA256Digest()));
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(key.getD(), ECKey.CURVE);

        signer.init(true, privateKeyParameters);
        BigInteger[] components = signer.generateSignature(hash);

        Signature sig = new Signature(components[0], components[1]).toCanonicalised();

        // find the recId and store in signature for public key recover later
        PublicKey publicKey = key.toPublicKey();
        int recId = getRecId(sig, hash, publicKey);

        if (recId == -1) {
            throw new RecoverIDNotFoundException();
        }

        sig.setRecId(recId);

        return sig;
    }

    /**
     * Sign 32 bits hash with private key and store the recId into signature
     *
     * @param data
     *            Data hash with 32 bits
     * @param key
     *            PrivateKey
     * @return {@link Signature}
     */
    public static Signature sign(byte[] data, @NotNull PrivateKey key) {
        return signHash(Utils.hash(data), key);
    }

    private static int getRecId(Signature signature, byte[] hash, @NotNull PublicKey publicKey) {
        Sha256Hash dataHash = Sha256Hash.wrap(hash);

        String refPubKey = publicKey.getEncoded(true);

        int recId = -1;
        for (int i = 0; i < 4; i++) {
            ECKey k = ECKey.recoverFromSignature(i, signature.get(), dataHash, true);
            try {
                if (k != null && Utils.HEX.encode(k.getPubKey()).equals(refPubKey)) {
                    return i;
                }
            } catch (Exception ex) {
                // no need to handle anything here
            }
        }

        return recId;
    }

    /**
     * Verify hash message with signature and public key
     *
     * @param data
     *            Data hash with 32 bits
     * @param signature
     *            Signature object
     * @param publicKey
     *            PublicKey object
     * @return boolean
     */
    public static boolean verify(byte[] data, @NotNull Signature signature, @NotNull PublicKey publicKey) {
        return verifyHash(Utils.hash(data), signature, publicKey);
    }

    public static boolean verifyHash(byte[] hash, @NotNull Signature signature, @NotNull PublicKey publicKey) {
        checkHashLength(hash);

        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));

        ECPublicKeyParameters publicKeyParams = new ECPublicKeyParameters(publicKey.getPoint().get(), // ECPoint
                ECKey.CURVE);

        signer.init(false, publicKeyParams);

        return signer.verifySignature(hash, signature.getR(), signature.getS());
    }

    /**
     * Recover public key from signature and original data byte[]. Note: one always
     * need to compare the public key recovered from signature match with whe
     * reference public key
     *
     * @param data
     *            original data signed by the private key
     * @param signature
     *            signature from sign method
     * @return {@link PublicKey}
     */
    @NotNull
    @Contract("_, _ -> new")
    public static PublicKey recoverPublicKey(byte[] data, @NotNull Signature signature) {
        Sha256Hash dataHash = Sha256Hash.wrap(data);

        ECKey k = ECKey.recoverFromSignature(signature.getRecId(), signature.get(), dataHash, true);
        if (k == null) {
            throw new PublicKeyRecoverFailureException();
        }

        return new PublicKey(k.getPubKey());
    }

    private static void checkHashLength(@NotNull byte[] hash) {
        if (hash.length != 32) {
            throw new IllegalArgumentException("Input hash must should be of length 32");
        }
    }

    public byte[] getBytes() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_LENGTH);
        byteBuffer.put(0, (byte) (getRecId() + 4 + 27));
        byteBuffer.position(1);
        byteBuffer.put(getR().toByteArray(), 0, 32);
        byteBuffer.position(33);
        byteBuffer.put(getS().toByteArray(), 0, 32);

        return byteBuffer.array();
    }

    @Override
    public String toString() {
        return String.format("%s%s", K1_PREFIX, Utils.base58Check(getBytes(), "K1"));
    }

    @Contract(pure = true)
    private ECKey.ECDSASignature get() {
        return signature;
    }

    public BigInteger getR() {
        return signature.r;
    }

    public BigInteger getS() {
        return signature.s;
    }

    public int getRecId() {
        return recId;
    }

    private void setRecId(int id) {
        recId = id;
    }

    public boolean isCanonical() {
        return signature.isCanonical();
    }

    @Contract(" -> this")
    private Signature toCanonicalised() {
        signature = signature.toCanonicalised();
        return this;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Signature)) {
            return false;
        }

        Signature s = (Signature) obj;

        return getR().equals(s.getR()) && getS().equals(s.getS()) && getRecId() == s.getRecId();
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + getR().hashCode();
        result = 31 * result + getS().hashCode();
        result = 31 * result + getRecId();
        return result;
    }
}
