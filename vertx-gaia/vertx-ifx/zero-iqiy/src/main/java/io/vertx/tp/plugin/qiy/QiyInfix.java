package io.vertx.tp.plugin.qiy;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.func.Fn;
import io.vertx.up.plugin.Infix;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("all")
public class QiyInfix implements Infix {
    private static final String NAME = "ZERO_QIY_POOL";

    private static final ConcurrentMap<String, QiyClient> CLIENTS
            = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        Fn.pool(CLIENTS, name,
                () -> Infix.initTp("qiy",
                        (config) -> QiyClient.createShared(vertx, config),
                        QiyInfix.class));
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    @Override
    public QiyClient get() {
        return getClient();
    }

    public static QiyClient getClient() {
        return CLIENTS.get(NAME);
    }
}
