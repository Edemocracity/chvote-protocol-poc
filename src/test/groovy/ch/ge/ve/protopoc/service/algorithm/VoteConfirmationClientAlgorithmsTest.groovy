package ch.ge.ve.protopoc.service.algorithm

import ch.ge.ve.protopoc.service.model.*
import ch.ge.ve.protopoc.service.model.polynomial.Point
import ch.ge.ve.protopoc.service.support.Hash
import ch.ge.ve.protopoc.service.support.RandomGenerator
import spock.lang.Specification

import static ch.ge.ve.protopoc.service.support.BigIntegers.*
import static java.math.BigInteger.ONE
import static java.math.BigInteger.ZERO

/**
 * Tests on the vote confirmation algorithms performed by the voting client
 */
class VoteConfirmationClientAlgorithmsTest extends Specification {
    PublicParameters publicParameters = Mock()
    EncryptionGroup encryptionGroup = Mock()
    IdentificationGroup identificationGroup = Mock()
    RandomGenerator randomGenerator = Mock()
    GeneralAlgorithms generalAlgorithms = Mock()
    Hash hash = Mock()
    PrimeField primeField = Mock()

    VoteConfirmationClientAlgorithms voteConfirmationClient

    void setup() {
        publicParameters.encryptionGroup >> encryptionGroup
        encryptionGroup.p >> SEVEN
        encryptionGroup.q >> THREE
        encryptionGroup.g >> TWO
        publicParameters.identificationGroup >> identificationGroup
        identificationGroup.p_circ >> ELEVEN
        identificationGroup.q_circ >> FIVE
        identificationGroup.g_circ >> THREE
        publicParameters.primeField >> primeField
        primeField.p_prime >> SEVEN
        publicParameters.s >> 4

        voteConfirmationClient = new VoteConfirmationClientAlgorithms(publicParameters, generalAlgorithms, randomGenerator, hash)
    }

    def "genConfirmation should generate the expected confirmation"() {
        given: "a given set of parameters"
        def voterIndex = 0
        def confirmationCode = [0x9A] as byte[] // ToInteger(Y) = 154
        def bold_P = [
                [new Point(FOUR, TWO)],
                [new Point(THREE, ONE)],
                [new Point(FIVE, ZERO)],
                [new Point(SIX, THREE)],

        ]
        def bold_k = [1]
        and: "known collaborators responses"
        hash.hash(_) >>> [
                [0x12] as byte[], // j = 1 --> y_1 = 18 mod 5 = 3
                [0x34] as byte[], // j = 2 --> y_2 = 52 mod 5 = 2
                [0x56] as byte[], // j = 3 --> y_3 = 86 mod 5 = 1
                [0x78] as byte[]  // j = 4 --> y_4 = 120 mod 5 = 0
        ]
        randomGenerator.randomInZq(FIVE) >> THREE // called by GenConfirmationNIZKP - omega
        generalAlgorithms.getNIZKPChallenge(_ as BigInteger[], _ as BigInteger[], _ as BigInteger) >> THREE // c

        // y = 154 + 3 + 2 + 1 + 0 mod 5 = 0
        // y_circ = g_circ ^ y mod p_circ = 3 ^ 0 mod 11 = 1
        // t = g_circ ^ omega mod p_circ = 3 ^ 3 mod 11 = 5
        // s = omega + c * y mod q_circ = 3 + 3 * 0 mod 11 = 3
        expect:
        voteConfirmationClient.genConfirmation(voterIndex, confirmationCode, bold_P, bold_k) ==
                new Confirmation(ONE, new NonInteractiveZKP([FIVE], [THREE]))
    }

    def "getValues should correctly get the values for each A_j(0)"() {
        expect:
        voteConfirmationClient.getValues(bold_P, bold_k) == bold_y

        where:
        bold_P | bold_k || bold_y
        [
                new Point(SIX, ONE),
                new Point(THREE, TWO),
                new Point(FIVE, ONE)
        ]      | [1, 2] || [ONE, ZERO]
    }

    def "getValue should correctly interpolate the value for A(0)"() {
        expect:
        voteConfirmationClient.getValue(points) == y

        where:
        points                                        || y
        [new Point(SIX, ONE)]                         || ONE
        [new Point(THREE, TWO), new Point(FIVE, ONE)] || ZERO // performed algorithm by hand, on paper.
    }

    def "genConfirmationNIZKP should generate a valid proof of knowledge for y"() {
        given: "a known random omega"
        randomGenerator.randomInZq(FIVE) >> FOUR // omega
        and: "a known challenge value"
        // t = g_circ ^ omega mod p_circ = 3 ^ 4 mod 11 = 4
        generalAlgorithms.getNIZKPChallenge([NINE] as BigInteger[], [FOUR] as BigInteger[], FIVE) >> THREE

        expect: "the generated NIZKP to have the expected value"
        // s = omega + c * y mod q_circ = 4 + 3 * 2 mod 5 = 0
        voteConfirmationClient.genConfirmationNIZKP(TWO, NINE) == new NonInteractiveZKP([FOUR], [ZERO])

    }

    def "getFinalizationCode should correctly combine the given finalization code parts"() {
        expect:
        voteConfirmationClient.getFinalizationCode([
                new FinalizationCodePart([0xDE, 0xAD] as byte[], []),
                new FinalizationCodePart([0xBE, 0xEF] as byte[], []),
                new FinalizationCodePart([0x01, 0x10] as byte[], []),
                new FinalizationCodePart([0xFA, 0xCE] as byte[], [])
        ]) == ([0x9B, 0x9C] as byte[])
    }
}
