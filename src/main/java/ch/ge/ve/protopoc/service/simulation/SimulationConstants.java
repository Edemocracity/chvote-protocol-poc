/*-------------------------------------------------------------------------------------------------
 - #%L                                                                                            -
 - chvote-protocol-poc                                                                            -
 - %%                                                                                             -
 - Copyright (C) 2016 - 2017 République et Canton de Genève                                       -
 - %%                                                                                             -
 - This program is free software: you can redistribute it and/or modify                           -
 - it under the terms of the GNU Affero General Public License as published by                    -
 - the Free Software Foundation, either version 3 of the License, or                              -
 - (at your option) any later version.                                                            -
 -                                                                                                -
 - This program is distributed in the hope that it will be useful,                                -
 - but WITHOUT ANY WARRANTY; without even the implied warranty of                                 -
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                                   -
 - GNU General Public License for more details.                                                   -
 -                                                                                                -
 - You should have received a copy of the GNU Affero General Public License                       -
 - along with this program. If not, see <http://www.gnu.org/licenses/>.                           -
 - #L%                                                                                            -
 -------------------------------------------------------------------------------------------------*/

package ch.ge.ve.protopoc.service.simulation;

import ch.ge.ve.protopoc.service.support.BigIntegers;

import java.math.BigInteger;

/**
 * This interface holds the constants used for the simulation of the protocol
 */
public interface SimulationConstants {
    int default_n_max = 1678;

    BigInteger p_RC0e = BigInteger.valueOf(167L);
    BigInteger q_RC0e = BigInteger.valueOf(83L);
    BigInteger g_RC0e = BigIntegers.FOUR;
    BigInteger h_RC0e = BigIntegers.SEVEN;

    BigInteger p_hat_RC0s = BigInteger.valueOf(149L);
    BigInteger q_hat_RC0s = BigInteger.valueOf(37L);
    BigInteger g_hat_RC0s = BigInteger.valueOf(16L);

    BigInteger p_RC1e = new BigInteger("89884656743115795386465259539451236680898848947115328636715040578" +
            "86633790275048156635423866120376801056005693993569667882939488440" +
            "72083112464237153197370621888839467124327426381511098006230470597" +
            "26541476042502884419075341171231440736956555270413618581675255342" +
            "293149119973622969239858152417678164812113740223");
    BigInteger q_RC1e = new BigInteger("44942328371557897693232629769725618340449424473557664318357520289" +
            "43316895137524078317711933060188400528002846996784833941469744220" +
            "36041556232118576598685310944419733562163713190755549003115235298" +
            "63270738021251442209537670585615720368478277635206809290837627671" +
            "146574559986811484619929076208839082406056870111");
    BigInteger g_RC1e = BigIntegers.FOUR;

    BigInteger p_hat_RC1s = new BigInteger("89884656743115795386465259539451236680898848947115328636715040578" +
            "86633790275048156635423866120376801056005693993569667882939488440" +
            "72083112464237153197370621888839467124327426381511098006230470597" +
            "26541476042502884419075341171231440736956555270413618581675255529" +
            "365358698328774708775703215219351545329613875969");
    BigInteger q_hat_RC1s = new BigInteger("730750818665451459101842416358141509827966271787");
    BigInteger g_hat_RC1s = new BigInteger("43753966268956158683794141044609048074944399463497118601009260015" +
            "27890794479339688887265479743667915617170483526334209874722984198" +
            "29635508715574476834043594463776486457518569138292805779343848313" +
            "81295103182368037001170314531189658120206052644043469275562473160" +
            "989451140877931368137440524162645073654512304068");


    BigInteger p2048 = new BigInteger(
            "00e9b7769f39ff08d055aac85d89a0" +
                    "b3c977bd8341364090553d6ab6b365" +
                    "aec235a0a99b276a60568903fcff67" +
                    "6c60a2032bddfe79276904f9ac8872" +
                    "ca776cc359d7ecbd53267c012f53b4" +
                    "a71e88deeb7e228d7494ca7b1523d3" +
                    "cfb3a809a4e5db18eea37c7d85ed5b" +
                    "b690d1e06ae83fe67aadc1cb2752a7" +
                    "51115f3c7f925fc1a1c9579f721c58" +
                    "43c4e36906d12db4d7814a56c6c45b" +
                    "1808ba020fa9ff7e29364e44b88664" +
                    "e617cf5c6d731568c1464ce2f82a8e" +
                    "713bcad9b71e5a7a2c2af03c8a3434" +
                    "df9b37b0ae7ccc154600a72c572a80" +
                    "2e7f110c89ece220b799688c2a4acf" +
                    "0f4031854462a3d3a3d91349fb2689" +
                    "4e2b078603704909544b50c3979e57" +
                    "438b", 16);

    BigInteger p_hat_2048 = new BigInteger("0086252d9bc72dad4c1dbeebbf2ed8" +
            "dfab21c3baf17dcc57aba4aa1f7297" +
            "a570e92496928719c10795c0227a9e" +
            "dbd680515c4aea5fcfe688321e7125" +
            "0798d05536c95ccdb01976c6fed157" +
            "20a537de509b3797852a9494bf5656" +
            "68f409cb300c68f9aeeb716af8aed9" +
            "d31a3dcfe382f5503820bf644b0a79" +
            "a0ba5f731951b73ddec67fdbc06381" +
            "008a1bd064a825023802114eb0e6d5" +
            "779e15acf55a581e55f2b24d925148" +
            "c20ab7433d5d69877eb1c8e6633249" +
            "d8e3aaa370923de00a93d8721e1450" +
            "dbe14d912cbde4b34cb5ab421bdc52" +
            "221d4aeff0fa33a6a862698d7f84da" +
            "5bcbde6e6ed03c2924b4acedd2794f" +
            "4bd4ab2a607efe6a3398f28b7d9146" +
            "d123", 16);
}
