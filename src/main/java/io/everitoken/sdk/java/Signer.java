package io.everitoken.sdk.java;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

/**
 * EC-DSA as described in X9.62
 */
public class Signer implements ECConstants, DSA {
    private final DSAKCalculator kCalculator;

    private ECKeyParameters key;
    private SecureRandom random;

    /**
     * Configuration with an alternate, possibly deterministic calculator of K.
     *
     * @param kCalculator
     *                        a K value calculator.
     */
    public Signer(DSAKCalculator kCalculator) {
        this.kCalculator = kCalculator;
    }

    protected static BigInteger calculateE(BigInteger n, byte[] message) {
        int log2n = n.bitLength();
        int messageBitLength = message.length * 8;

        BigInteger e = new BigInteger(1, message);
        if (log2n < messageBitLength) {
            e = e.shiftRight(messageBitLength - log2n);
        }
        return e;
    }

    // 5.3 pg 28

    protected static ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    // 5.4 pg 29

    protected static SecureRandom initSecureRandom(boolean needed, SecureRandom provided) {
        return !needed ? null : (provided != null) ? provided : new SecureRandom();
    }

    @Override
    public void init(boolean forSigning, CipherParameters param) {
        SecureRandom providedRandom = null;

        if (forSigning) {
            if (param instanceof ParametersWithRandom) {
                ParametersWithRandom rParam = (ParametersWithRandom) param;

                key = (ECPrivateKeyParameters) rParam.getParameters();
                providedRandom = rParam.getRandom();
            } else {
                key = (ECPrivateKeyParameters) param;
            }
        } else {
            key = (ECPublicKeyParameters) param;
        }

        random = initSecureRandom(forSigning && !kCalculator.isDeterministic(), providedRandom);
    }

    /**
     * generate a signature for the given message using the key we were initialised
     * with. For conventional DSA the message should be a SHA-1 hash of the message
     * of interest.
     *
     * @param message
     *                    the message that will be verified later.
     */
    @Override
    public BigInteger[] generateSignature(byte[] message) {
        ECDomainParameters ec = key.getParameters();
        BigInteger n = ec.getN();
        BigInteger e = calculateE(n, message);
        BigInteger d = ((ECPrivateKeyParameters) key).getD();

        if (kCalculator.isDeterministic()) {
            kCalculator.init(n, d, message);
        } else {
            kCalculator.init(n, random);
        }

        BigInteger r, s;

        ECMultiplier basePointMultiplier = createBasePointMultiplier();

        int sLen = 0;
        // 5.3.2
        do // generate s
        {
            BigInteger k;
            int rLen = 0;
            do // generate r
            {
                k = kCalculator.nextK();

                ECPoint p = basePointMultiplier.multiply(ec.getG(), k).normalize();

                // 5.3.3
                r = p.getAffineXCoord().toBigInteger().mod(n);
                rLen = r.toByteArray().length;
            } while (r.equals(ZERO) || rLen != 32); // make sure that length of r is 32 bytes

            s = k.modInverse(n).multiply(e.add(d.multiply(r))).mod(n);
            sLen = s.toByteArray().length;
        } while (s.equals(ZERO) || sLen != 32);

        return new BigInteger[] { r, s };
    }

    /**
     * return true if the value r and s represent a DSA signature for the passed in
     * message (for standard DSA the message should be a SHA-1 hash of the real
     * message to be verified).
     */
    @Override
    public boolean verifySignature(byte[] message, BigInteger r, BigInteger s) {
        ECDomainParameters ec = key.getParameters();
        BigInteger n = ec.getN();
        BigInteger e = calculateE(n, message);

        // r in the range [1,n-1]
        if (r.compareTo(ONE) < 0 || r.compareTo(n) >= 0) {
            return false;
        }

        // s in the range [1,n-1]
        if (s.compareTo(ONE) < 0 || s.compareTo(n) >= 0) {
            return false;
        }

        BigInteger c = s.modInverse(n);

        BigInteger u1 = e.multiply(c).mod(n);
        BigInteger u2 = r.multiply(c).mod(n);

        ECPoint G = ec.getG();
        ECPoint Q = ((ECPublicKeyParameters) key).getQ();

        ECPoint point = ECAlgorithms.sumOfTwoMultiplies(G, u1, Q, u2).normalize();

        // components must be bogus.
        if (point.isInfinity()) {
            return false;
        }

        BigInteger v = point.getAffineXCoord().toBigInteger().mod(n);

        return v.equals(r);
    }
}
