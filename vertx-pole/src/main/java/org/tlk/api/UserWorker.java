package org.tlk.api;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.annotations.infix.Mongo;
import io.vertx.up.ce.Envelop;
import org.vie.util.Jackson;

@Queue
public class UserWorker {

    @Mongo
    private transient MongoClient client;

    @Address("ZERO://USER")
    public Envelop reply(final Envelop message) {
        final User user = message.data(User.class);
        System.out.println(user);
        return Envelop.success(user);
    }

    @Address("ZERO://ROLE")
    public void async(final Message<Envelop> message) {
        final User user = Envelop.data(message, User.class);
        final JsonObject userData = new JsonObject(Jackson.serialize(user));
        this.client.save("DB_USER", userData, res -> {
            if (res.succeeded()) {
                message.reply(Envelop.success("Hello World"));
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}