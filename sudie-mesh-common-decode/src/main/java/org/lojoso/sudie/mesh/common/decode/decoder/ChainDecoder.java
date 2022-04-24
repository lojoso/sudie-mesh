package org.lojoso.sudie.mesh.common.decode.decoder;

import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface ChainDecoder {

    ChainDecoder selected(DecoderType type);

    List<? extends Dg> chainDecoder(List<Dg> data);

    enum DecoderType{
        REQ,RES
    }
}
