package org.lojoso.sudie.mesh.center.kernel.server.analysis;

import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.List;

public interface Analysis<T extends Dg> {

    List<T> analysis(List<Dg> datas);
}
