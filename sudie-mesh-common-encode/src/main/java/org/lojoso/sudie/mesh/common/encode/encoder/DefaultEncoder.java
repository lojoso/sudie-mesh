package org.lojoso.sudie.mesh.common.encode.encoder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public interface DefaultEncoder {

    byte[] encode(Method method, Object[] args);
}
