package org.delivery.storeadmin.domain.sse.connection.ifs;

public interface ConnectionPool<T, R> {

    void addSession(T uniqueKey, R session);

    R getSession(T uniqueKey);

    void onCompletionCallback(R session);
}
