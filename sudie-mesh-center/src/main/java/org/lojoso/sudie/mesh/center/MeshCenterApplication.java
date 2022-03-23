package org.lojoso.sudie.mesh.center;

import org.lojoso.sudie.mesh.center.config.SudieBaseConfig;
import org.lojoso.sudie.mesh.center.kernel.server.SudieAIOServer;
import org.lojoso.sudie.mesh.center.utils.FlagArgs;

public class MeshCenterApplication {

    public static void main(String[] args) {
        SudieBaseConfig config = new SudieBaseConfig(FlagArgs.getValue(args, "-p", "60001"),
                FlagArgs.getValue(args, "-s", "localhost:60001"));
        config.setRetry(10);
        SudieAIOServer.start(config);
    }
}
