package ch.ge.ve.protopoc.service.support;

import ch.ge.ve.protopoc.service.exception.DigestInitialisationException;
import ch.ge.ve.protopoc.service.model.SecurityParameters;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

/**
 * This class manages all the hashing operations and combinations
 */
public class Hash {
    private final String digestAlgorithm, digestProvider;
    private final Conversion conversion;
    private final SecurityParameters securityParameters;

    public Hash(String digestAlgorithm, String digestProvider, SecurityParameters securityParameters, Conversion conversion) {
        this.digestAlgorithm = digestAlgorithm;
        this.digestProvider = digestProvider;
        this.conversion = conversion;
        this.securityParameters = securityParameters;

        MessageDigest messageDigest = newMessageDigest();
        if (messageDigest.getDigestLength() * 8 < securityParameters.getL()) {
            throw new IllegalArgumentException(
                    String.format(
                            "The length of the message digest should be greater or equal to the expected output " +
                                    "length. Got %d expected %d",
                            messageDigest.getDigestLength() * 8,
                            securityParameters.getL()));
        }
    }

    private MessageDigest newMessageDigest() {
        try {
            return MessageDigest.getInstance(digestAlgorithm, digestProvider);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new DigestInitialisationException(e);
        }
    }

    /**
     * VarArgs version of {@link #recHash_L(Object)}
     *
     * @param objects an array of objects to hash
     * @return The recursive hash as defined in section 4.3
     */
    public byte[] recHash_L(Object... objects) {
        MessageDigest messageDigest = newMessageDigest();
        byte[] digest;
        if (objects.length == 0) {
            digest = messageDigest.digest();
        } else if (objects.length == 1) {
            return recHash_L(objects[0]);
        } else {
            for (Object object : objects) {
                messageDigest.update(recHash_L(object));
            }
            digest = messageDigest.digest();
        }
        return ByteArrayUtils.truncate(digest, securityParameters.getL() / 8);
    }

    /**
     * This method performs the necessary casts and conversions for the hashing to be compliant to the definition in
     * section 4.3.
     * <p>Tuples are represented as arrays of Objects and need to be cast afterwards. Diversity of inputs means that
     * ensuring type-safety is much more complex.</p>
     * <p>The <em>traditional</em> risks and downsides of casting and using the <tt>instanceof</tt> operator are
     * mitigated by centralizing the calls and handling the case where no type matches.</p>
     *
     * @param object the element which needs to be cast
     * @return the recursive hash as defined in section 4.3
     */
    public byte[] recHash_L(Object object) {
        if (object instanceof String) {
            return hash_L((String) object);
        } else if (object instanceof BigInteger) {
            return hash_L((BigInteger) object);
        } else if (object instanceof byte[]) {
            return hash_L((byte[]) object);
        } else if (object instanceof Hashable) {
            return recHash_L(((Hashable) object).elementsToHash());
        } else if (object instanceof List) {
            return recHash_L(((List) object).toArray());
        } else if (object instanceof Object[]) {
            return recHash_L((Object[]) object);
        } else {
            throw new IllegalArgumentException(String.format("Could not determine the type of object %s", object));
        }
    }

    /**
     * Use the underlying digest algorithm to obtain a hash of the byte array, truncated to length L
     *
     * @param byteArray
     * @return
     */
    public byte[] hash_L(byte[] byteArray) {
        MessageDigest messageDigest = newMessageDigest();
        byte[] digest = messageDigest.digest(byteArray);
        return ByteArrayUtils.truncate(digest, securityParameters.getL() / 8);
    }

    public byte[] hash_L(String s) {
        return hash_L(conversion.toByteArray(s));
    }

    public byte[] hash_L(BigInteger integer) {
        return hash_L(conversion.toByteArray(integer));
    }

    /**
     * Additional method introduced for performance reasons
     * <p>The algorithm 7.6 GetChallenges calls n times the recHash_L function, with only
     * a suffix number changing.</p>
     * <p>This made the function run in quadratic time with respect to the number of votes, with a noticeable
     * impact on performance when the data set grew large.</p>
     * <p>This method can be used to improve the performance by computing recHash_L(v) only once, since
     * <tt>recHash_optimised(recHash_L(v), i) == recHash_L(v, i)</tt> for all tuples / arrays v.</p>
     *
     * @param prefixHash the hash of the prefix
     * @param suffix     the element to be added to the hash
     * @return the combined hash of the prefix and suffix
     */
    public byte[] recHash_optimised(byte[] prefixHash, Object suffix) {
        MessageDigest messageDigest = newMessageDigest();
        messageDigest.update(prefixHash);
        messageDigest.update(recHash_L(suffix));

        return ByteArrayUtils.truncate(messageDigest.digest(), securityParameters.getL() / 8);
    }

    /**
     * This interface is used to facilitate hashing of objects representing tuples, so that the relevant elements can
     * be included in the the hash, in a predictable and coherent order.
     */
    public interface Hashable {
        /**
         * Get this object as a vector (or array) of its properties
         *
         * @return the array of the properties to be included for hashing
         */
        Object[] elementsToHash();
    }
}
