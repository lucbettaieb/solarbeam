/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_2;

import java.net.*;
import java.io.*;

/**
 *
 * @author Andrew Powell
 */
public class Communication {
    private InetAddress ipaddress = null;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    
    public Communication(String serverIPAddress, int port) throws IOException {
        socket = new Socket(serverIPAddress, port);
        setup();
    }
    
    public Communication(int port, int timeout_ms) throws SocketTimeoutException, IOException {  
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(timeout_ms);
        socket = serverSocket.accept();
        setup();
    }
    
    public Communication(int port) throws SocketTimeoutException, IOException {  
        this(port, 0);
    }
    
    private void setup() throws IOException {
        ipaddress = socket.getInetAddress();
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
    }
    
    public void write(byte[] data, int offset, int length) throws IOException {
        out.write(data, offset, length);
    }
    
    public void write(byte[] data) throws IOException {
        out.write(data);
    }
    
    public void writeByte(byte data) throws IOException {
        out.writeByte(data);
    }
    
    public void writeShort(short data) throws IOException {
        out.writeShort(data);
    }
    
    public void writeInt(int data) throws IOException {
        out.writeInt(data);
    }
    
    public void writeLong(long data) throws IOException {
        out.writeLong(data);
    }
    
    public void read(byte[] data, int offset, int length) throws IOException {
        in.read(data, offset, length);
    }    
    
    public void read(byte[] data) throws IOException {
        in.read(data);
    }
    
    public int read() throws IOException {
        return in.read();
    }
    
    public byte readByte() throws IOException {
        return in.readByte();
    }
    
    public int readInt() throws IOException {
        return in.readInt();
    }
    
    public short readShort() throws IOException {
        return in.readShort();
    }
    
    public long readLong() throws IOException {
        return in.readLong(); 
    }
    
    public int available() throws IOException {
        return in.available();
    }
    
    public boolean isReachable(int timeoutMs) throws IOException {
        return ipaddress.isReachable(timeoutMs);
    }
    
    public boolean isClosedByClient() throws IOException {
        return socket.isClosed();
    }
    
    @Override
    public void finalize() throws Throwable
    {
        socket.close();
        if (serverSocket != null) {
            serverSocket.close();
            serverSocket = null;
        }
        super.finalize();
    }
}
