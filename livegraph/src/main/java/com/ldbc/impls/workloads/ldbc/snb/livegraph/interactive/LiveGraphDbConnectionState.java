package com.ldbc.impls.workloads.ldbc.snb.livegraph.interactive;

import com.ldbc.driver.DbConnectionState;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.LinkedList;
import java.util.Map;

public class LiveGraphDbConnectionState extends DbConnectionState {

    // static final int INITIAL_CAPACITY = 2048;
    protected String hostname;
    protected int port;
    LinkedList<TTransport> pool = new LinkedList<>();

    public LiveGraphDbConnectionState(Map<String, String> properties) throws TException {

        hostname = properties.get("hostname");
        port = Integer.parseInt(properties.get("port"));
        int num_clients = Integer.parseInt(System.getenv("LIVEGRAPH_NUM_CLIENTS"));

        for (int i = 0; i < num_clients; i++) {
            TTransport transport = new TSocket(hostname, port);
            transport.open();
            pool.add(transport);
        }
    }

    public synchronized TTransport getConnection() throws TException {
        if (pool.isEmpty()) {
            TTransport transport = new TSocket(hostname, port);
            transport.open();
            pool.add(transport);
        }
        return pool.pop();
    }

    public synchronized void returnConnection(TTransport connection) {
        pool.push(connection);
    }

    @Override
    public void close() {
        while (!pool.isEmpty()) {
            pool.pop().close();
        }
    }
}

// public class LiveGraphDbConnectionState extends DbConnectionState {
// 
//     static final int INITIAL_CAPACITY = 512;
//     protected String hostname;
//     protected int port;
//     ThreadLocal<TTransport> connection = new ThreadLocal<>();
// 
//     public LiveGraphDbConnectionState(Map<String, String> properties) throws TException {
// 
//         hostname = properties.get("hostname");
//         port = Integer.parseInt(properties.get("port"));
// 
//         // for (int i = 0; i < INITIAL_CAPACITY; i++) {
//         //     TTransport transport = new TSocket(hostname, port);
//         //     transport.open();
//         //     pool.add(transport);
//         // }
//     }
// 
//     public TTransport getConnection() throws TException {
//         if (connection.get() == null) {
//             TTransport transport = new TSocket(hostname, port);
//             transport.open();
//             connection.set(transport);
//         }
//         return connection.get();
//     }
// 
//     public void returnConnection(TTransport connection) {
//     }
// 
//     @Override
//     public void close() {
//         // while (!pool.isEmpty()) {
//         //     pool.pop().close();
//         // }
//     }
// }
