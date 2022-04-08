package org.lojoso.sudie.mesh.common.decode.decoder;

import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.List;

public interface ChainDecoder {

    List<? extends Dg> chainDecoder(List<Dg> data);
}
