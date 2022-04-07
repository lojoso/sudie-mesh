package org.lojoso.sudie.mesh.common.decode.decoder;

import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.List;

public interface ChainDecoder {

    List<Object> chainDecoder(List<Dg> data);
}
