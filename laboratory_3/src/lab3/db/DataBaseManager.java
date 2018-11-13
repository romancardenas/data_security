package lab3.db;

import lab3.crypto.HashGenerator;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {

    /**
     * Database URL
     */
    private String url;

    /**
     * Database Manager constructor
     * @param url
     */
    public DataBaseManager(String url) {
        this.url = url;
    }

    /**
     * Returns hexadecimal string of a byte array
     * @param bytes
     * @return
     */
    private static String byteArrayToHex(byte[] bytes) {
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v/16];
            hexChars[j*2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }

    /**
     * Computes SHA-256 hash for a given password and salt
     * @param password
     * @return Hash of the input password
     * @throws NoSuchAlgorithmException
     */
    private String passwordToHash(String password, String salt) throws NoSuchAlgorithmException {
        HashGenerator hash = new HashGenerator();
        return new String(hash.getHash(salt.concat(password), "SHA-256"));
    }

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Connect to a sample database
     */
    private void createNewDatabase() {

        try (Connection conn = this.connect()) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create new tables in the database
     */
    private void createNewTables() {

        // SQL statements for creating a new table
        String sql1 = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL UNIQUE,\n"
                + " salt text NOT NULL,\n"
                + "	password text NOT NULL\n"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the users table
     *
     * @param name
     * @param password
     */
    public void insertUser(String name, String password) throws NoSuchAlgorithmException {
        HashGenerator hash_generator = new HashGenerator();
        // Create random string for salt
        String salt = hash_generator.generateSalt();
        // hash password pre-pending before the salt
        byte[] hashed_pwd = hash_generator.getHash(salt.concat(password), "SHA-256");

        // insert into database
        String sql = "INSERT INTO users(name, salt,password) VALUES(?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, salt);
            pstmt.setString(3, new String(hashed_pwd));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Check user authentication
     * @param user
     * @param password
     * @return
     */
    public boolean checkUserAuth(String user, String password) {
        boolean res = false;
        System.out.println("      Username: " + user);
        System.out.println("      Password: " + password);
        // Read salt and password from database
        String sql = "SELECT salt, password FROM users WHERE name = ?";
        try (Connection conn = this.connect(); PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            ResultSet rs  = pstmt.executeQuery();
            // loop through the result set
            if (!rs.next()) {                            //if rs.next() returns false
                //then there are no rows.
                System.out.println("      Username not found");
            } else {
                do {
                    String rcv_salt = rs.getString("salt");
                    String rcv_password = rs.getString("password");

                    // hash password sent by the client by pre-pending the salt stored in the database
                    String hashed_password = this.passwordToHash(password, rcv_salt);
                    // check that created hash and hashed password stored in the database matches

                    System.out.println("      SALT: " + byteArrayToHex(rcv_salt.getBytes()));
                    System.out.println("      Hashed password: " + byteArrayToHex(hashed_password.getBytes()));
                    System.out.println("      Hash stored in DB: " + byteArrayToHex(rcv_password.getBytes()));
                    res = rcv_password.equals(hashed_password);
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Run this method in order to populate the database
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String filename = "jdbc:sqlite:db/laboratory_3.db";  // path to SQLite database
        DataBaseManager db = new DataBaseManager(filename);

        // create new database and tables required for the laboratory
        db.createNewDatabase();
        db.createNewTables();

        // for usernames user1 and user2, create new user which password coincides with username (just for trial)
        List<String> usernames = new ArrayList<>();
        usernames.add("user1");
        usernames.add("user2");
        for (String user : usernames) {
            db.insertUser(user, user);
        }
    }
}
