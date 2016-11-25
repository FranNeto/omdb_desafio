package br.com.zup.omdbdesafio.controller.component;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Thread used to realize internet test connection.
 *
 * @author
 */
public class ConnectionTestThread extends Thread {

    /**
     * Instance of the Handler.
     */
    protected final transient Handler handler;
    protected transient boolean responded = false;
    private static final transient int TIMEOUT = 8000;
    private final static Object SYNC_INST = new Object();
    private static final String SITE_1 = "www.google.com";
    private static final String SITE_2 = "www.yahoo.com";
    private static final String SITE_3 = "en.wikipedia.org";
    private static final String SITE_4 = "stackoverflow.com";
    private Context context;
    private static final transient int TIMEOUT_END = 20000;
    ;

    /**
     * Constructor
     *
     * @param context
     * @param handler
     */
    public ConnectionTestThread(final Context context, final ConnectionTestHandler handler) {
        super();
        this.handler = handler;
        this.context = context;
    }


    @Override
    public void run() {

        synchronized (SYNC_INST) {
            new Thread() {

                @Override
                public void run() {
                    Log.e(" connecting...", getClass().getName());
                    //boolean response = false;
                    try {
                        SocketAddress addr = new InetSocketAddress(SITE_1, 80); // Set IP/Host and Port
                        Socket socket = new Socket();
                        //Connect socket to address, and set a time-out to 5 sec
                        socket.connect(addr, TIMEOUT);
                        socket.close();
                        responded = true;

                    } catch (UnknownHostException e) {
                    } catch (IOException e) {
                    }

                    try {
                        SocketAddress addr = new InetSocketAddress(SITE_2, 80); // Set IP/Host and Port
                        Socket socket = new Socket();
                        //Connect socket to address, and set a time-out to 5 sec
                        socket.connect(addr, TIMEOUT);
                        socket.close();
                        responded = true;
                    } catch (UnknownHostException e) {
                    } catch (IOException e) {
                    }

                    try {
                        SocketAddress addr = new InetSocketAddress(SITE_3, 80); // Set IP/Host and Port
                        Socket socket = new Socket();
                        //Connect socket to address, and set a time-out to 5 sec
                        socket.connect(addr, TIMEOUT);
                        socket.close();
                        responded = true;
                    } catch (UnknownHostException e) {
                    } catch (IOException e) {
                    }

                    try {
                        SocketAddress addr = new InetSocketAddress(SITE_4, 80); // Set IP/Host and Port
                        Socket socket = new Socket();
                        //Connect socket to address, and set a time-out to 5 sec
                        socket.connect(addr, TIMEOUT);
                        socket.close();
                        responded = true;
                    } catch (UnknownHostException e) {
                    } catch (IOException e) {
                    }

                }

            }.start();

            try {
                int waited = 0;
                ConnectivityManager connManager = null;

                if (context != null) {
                    connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                }

                if (connManager == null) {
                    responded = false;
                } else {
//
                    NetworkInfo info = connManager.getActiveNetworkInfo();
                    final boolean status = (info != null && info.isAvailable() && info.isConnected());
                    if (status) {

                        while (!responded && (waited < TIMEOUT_END)) {
                            sleep(100);
                            if (!responded) {
                                waited += 100;
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
            } finally {
                if (!responded) {
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }
    }
}
