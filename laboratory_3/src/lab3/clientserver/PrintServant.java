package lab3.clientserver;

import lab3.db.DataBaseManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintServant extends UnicastRemoteObject implements PrintService {

    /**
     * Indicates if the printer is activated (true) or not
     */
    private boolean status;

    /**
     * Ordered list with the files to be printed, with format "<filename>-<printer>"
     */
    private List<String> printing_list;

    /**
     * Hash map with printing options
     */
    private Map<String, String> conf_map;

    /**
     * Database utility for accessing to authentication database server
     */
    private DataBaseManager db;

    /**
     * Constructor
     * @throws RemoteException
     */
    protected PrintServant(String url) throws RemoteException {
        super();
        this.status = false;
        this.printing_list = new ArrayList<>();
        this.conf_map = new HashMap<>();
        this.db = new DataBaseManager(url);
    }

    /**
     * authenticates user
     * @param username: client username
     * @param password: client password
     * @return boolean indicating whether the client is authenticated or not
     */
    private boolean user_authenticated(String username, String password) {
        System.out.println("   CHECKING USER AUTHENTICITY:");
        boolean res = this.db.checkUserAuth(username, password);
        if (res)
            System.out.println("   User ".concat(username).concat( " authenticated"));
        else
            System.out.println("   User ".concat(username).concat( " NOT authenticated"));
        return res;
    }

    /**
     * prints file filename on the specified printer
     * @param filename file
     * @param printer printer that will print the file
     * @return String that estates the printing status
     * @throws RemoteException
     */
    @Override
    public String print(String filename, String printer, String username, String password) throws RemoteException {
        System.out.println("REMOTE CALL: print");  // Log function call
        String aux = "DENIED";
        if (user_authenticated(username, password)) {
            System.out.println("   Processing request");
            aux = "APPROVED-OFF";
            if (this.status) {
                aux = filename.concat("-").concat(printer);
                this.printing_list.add(aux);  // If user authenticated and printer activated, insert file to print list
            }
        }
        return aux;
    }

    /**
     * lists the print queue on the user's display in lines of the form <job number>   <file name>
     * @return list of strings with filenames
     * @throws RemoteException
     */
    @Override
    public List<String> queue(String username, String password) throws RemoteException {
        System.out.println("REMOTE CALL: queue");
        if (user_authenticated(username, password)) {
            System.out.println("   Processing request");
            return this.printing_list;
        } else {
            List<String> aux = new ArrayList<>();
            aux.add("DENIED");
            return aux;
        }
    }

    /**
     * moves job to the top of the queue
     * @param job job number to be moved
     * @return filename associated to job
     * @throws RemoteException
     */
    @Override
    public String topQueue(int job, String username, String password) throws RemoteException {
        System.out.println("REMOTE CALL: topQueue");
        String aux = "DENIED";
        if (user_authenticated(username, password)) {
            System.out.println("   Processing request");
            aux = "APPROVED-OFF";
            if (this.status) {
                aux = "IndexOutOfBounds";
                if (this.printing_list.size() > job) {
                    aux = this.printing_list.get(job);
                    this.printing_list.remove(job);
                    this.printing_list.add(0, aux);
                }
            }
        }
        return aux;
    }

    /**
     * starts the print server
     * @throws RemoteException
     */
    @Override
    public void start(String username, String password) throws RemoteException {
        System.out.println("REMOTE CALL: start");
        if (user_authenticated(username, password)) {
            System.out.println("   Processing request");
            this.status = true;
        }
    }

    /**
     * stops the print server
     * @throws RemoteException
     */
    @Override
    public void stop(String username, String password)  throws RemoteException{
        System.out.println("REMOTE CALL: stop");
        if (user_authenticated(username, password)) {
            System.out.println("   Processing request");
            this.printing_list.clear();
            this.status = false;
        }
    }

    /**
     * stops the print server, clears the print queue and starts the print server again
     * @throws RemoteException
     */
    @Override
    public void restart(String username, String password) throws RemoteException {
        System.out.println("REMOTE CALL: restart");
        if (user_authenticated(username, password)) {
            System.out.println("   Processing request");
            this.conf_map.clear();
            this.printing_list.clear();
            this.status = false;
            this.status = true;
        }
    }

    /**
     * prints status of printer on the user's display
     * @return
     * @throws RemoteException
     */
    @Override
    public String status(String username, String password) throws RemoteException {
        System.out.println("REMOTE CALL: status");
        if (user_authenticated(username, password)) {
            System.out.println("   Processing request");
            return String.valueOf(this.status);
        } else
            return "DENIED";
    }

    /**
     * prints the value of the parameter on the user's display
     * @param parameter
     * @return
     * @throws RemoteException
     */
    @Override
    public String readConfig(String parameter, String username, String password) throws RemoteException {
        System.out.println("REMOTE CALL: readConfig");
        String aux = "DENIED";
        if (user_authenticated(username, password)) {
            System.out.println("   Processing request");
            aux = this.conf_map.getOrDefault(parameter, "APPROVED-NULL");
        }
        return aux;
    }

    /**
     * sets the parameter to value
     * @param parameter
     * @param value
     * @throws RemoteException
     */
    @Override
    public void setConfig(String parameter, String value, String username, String password) throws RemoteException {
        System.out.println("REMOTE CALL: setConfig");
        if (user_authenticated(username, password)) {
            System.out.println("   Processing request");
            this.conf_map.put(parameter, value);
        }
    }
}
