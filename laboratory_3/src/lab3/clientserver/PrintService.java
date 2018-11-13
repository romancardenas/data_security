package lab3.clientserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PrintService extends Remote {
    String print(String filename, String printer, String username, String password) throws RemoteException;
    List<String> queue(String username, String password) throws RemoteException;
    String topQueue(int job, String username, String password) throws RemoteException;
    void start(String username, String password) throws RemoteException;
    void stop(String username, String password) throws RemoteException;
    void restart(String username, String password) throws RemoteException;
    String status(String username, String password) throws RemoteException;
    String readConfig(String parameter, String username, String password) throws RemoteException;
    void setConfig(String parameter, String value, String username, String password) throws RemoteException;
}
