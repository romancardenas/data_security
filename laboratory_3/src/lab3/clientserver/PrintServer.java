package lab3.clientserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrintServer {
    public static void main(String[] args) throws RemoteException {
        String db_url = "jdbc:sqlite:db/laboratory_3.db";
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind("printer", new PrintServant(db_url));
    }
}
