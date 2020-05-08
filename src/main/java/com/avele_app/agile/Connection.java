package com.avele_app.agile;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class Connection
{
    private  Socket  mSocket = null;
    private  String  mHost   = null;
    private  int     mPort   = 0;

    public static final String LOG_TAG = "SOCKET";

    public Connection() {}

    public Connection (final String host, final int port)
    {
        this.mHost = host;
        this.mPort = port;
    }

    /**
     * Метод открытия сокета
     */
    public void openConnection() throws Exception
    {
        // Если сокет уже открыт, то он закрывается
        closeConnection();
        try {
            // Создание сокета
            mSocket = new Socket(mHost, mPort);
        } catch (IOException e) {
            throw new Exception("Невозможно создать сокет: "+e.getMessage());
        }
    }
    /**
     * Метод закрытия сокета
     */
    public void closeConnection()
    {
        /* Проверяем сокет. Если он не зарыт, то закрываем его и освобдождаем соединение.*/
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Невозможно закрыть сокет: " + e.getMessage());
            } finally {
                mSocket = null;
            }
        }
        mSocket = null;
    }
    /**
     * Метод отправки данных
     */
    public boolean sendData(byte[] data) throws Exception {
        // Проверка открытия сокета
        if (mSocket == null || mSocket.isClosed()) {
            return false;
        }
        // Отправка данных
        try {
            mSocket.getOutputStream().write(data);
            mSocket.getOutputStream().flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        closeConnection();
    }
}
