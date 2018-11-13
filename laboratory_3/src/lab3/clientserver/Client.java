package lab3.clientserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        String username = "user1";
        String password = "user1";
        PrintService service= (PrintService) Naming.lookup("rmi://localhost:1234/printer");

        System.out.println("STARTING PRINTING SERVER");
        service.start(username, password);
        System.out.println("(print) SERVER RESPONSE: " + service.print("file1", "printer1", username, password));
        System.out.println("(print) SERVER RESPONSE: " + service.print("file2", "printer1", username, password));
        List<String> queue = service.queue(username, password);
        System.out.println("(queue) SERVER RESPONSE: ");
        for (int i = 0; i < queue.size(); i++) {
            System.out.println("   - ".concat(String.valueOf(i)).concat(" ").concat(queue.get(i)));
        }
        System.out.println("(topQueue) SERVER RESPONSE: " + service.topQueue(1, username, password));
        System.out.println("STOPING PRINTING SERVER");
        service.stop(username, password);
        System.out.println("(status) SERVER RESPONSE: " + service.status(username, password));
        System.out.println("STARTING PRINTING SERVER");
        service.start(username, password);
        System.out.println("(status) SERVER RESPONSE: " + service.status(username, password));
        System.out.println("RESTARTING PRINTING SERVER");
        service.restart(username, password);
        System.out.println("(status) SERVER RESPONSE: " + service.status(username, password));
        System.out.println("SETTING SERVER CONFIGURATION");
        service.setConfig("sides-per-page", "2", username, password);
        System.out.println("SERVER RESPONSE: " + service.readConfig("sides-per-page", username, password));

        // Check that server don't accept requests if authentication failure
        password = "user0";
        System.out.println("(print) SERVER RESPONSE: " + service.print("file1", "printer1", username, password));
        username = "user0";
        System.out.println("(print) SERVER RESPONSE: " + service.print("file1", "printer1", username, password));
    }
}
