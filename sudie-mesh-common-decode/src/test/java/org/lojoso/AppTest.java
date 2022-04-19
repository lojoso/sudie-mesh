package org.lojoso;

import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.lojoso.sudie.mesh.common.decode.decoder.DgDecoder;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws DecoderException {
        byte[] data = Hex.decodeHex("6801005c9700010f4a0920002d6f72672e6c6f6a6f736f2e73756469652e6d6573682e636f6d6d6f6e2e747376632e5465737453657276696365000873617948656c6c6f0005fc01ff11620014fc12302e353035313431303339393431323039");
        DgDecoder dgDecoder = new DgDecoder();
        dgDecoder.pubDecode(data);
    }
}
