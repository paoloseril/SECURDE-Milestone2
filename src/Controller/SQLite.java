package Controller;

import Model.History;
import Model.Logs;
import Model.Product;
import Model.User;
import Values.AES;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class SQLite {

    public int DEBUG_MODE = 0;
    private String driverURL = "jdbc:sqlite:" + "database.db";

    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database database.db created.");
            }
        } catch (Exception ex) {}
    }

    public void createHistoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS history (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " username TEXT NOT NULL,\n"
                + " name TEXT NOT NULL,\n"
                + " stock INTEGER DEFAULT 0,\n"
                + " timestamp TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db created.");
        } catch (Exception ex) {}
    }

    public void createLogsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS logs (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " event TEXT NOT NULL,\n"
                + " username TEXT NOT NULL,\n"
                + " desc TEXT NOT NULL,\n"
                + " timestamp TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db created.");
        } catch (Exception ex) {}
    }

    public void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS product (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL UNIQUE,\n"
                + " stock INTEGER DEFAULT 0,\n"
                + " price REAL DEFAULT 0.00\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db created.");
        } catch (Exception ex) {}
    }

    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " username TEXT NOT NULL UNIQUE,\n"
                + " password TEXT NOT NULL,\n"
                + " role INTEGER DEFAULT 2,\n"
                + " locked INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db created.");
        } catch (Exception ex) {}
    }

    public void dropHistoryTable() {
        String sql = "DROP TABLE IF EXISTS history;";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db dropped.");
        } catch (Exception ex) {}
    }

    public void dropLogsTable() {
        String sql = "DROP TABLE IF EXISTS logs;";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db dropped.");
        } catch (Exception ex) {}
    }

    public void dropProductTable() {
        String sql = "DROP TABLE IF EXISTS product;";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db dropped.");
        } catch (Exception ex) {}
    }

    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS users;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db dropped.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<History> getHistory(){
        String sql = "SELECT id, username, name, stock, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<History>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getString("timestamp")));
            }
        } catch (Exception ex) {}
        return histories;
    }

    public ArrayList<History> getHistory(String username, String product){
        String sql = "SELECT id, username, name, stock, timestamp FROM history where lower(username) = '" + username.toLowerCase()  + "' and lower(name) = '" + product.toLowerCase() + "'";
        ArrayList<History> histories = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getString("timestamp")));
            }
        } catch (Exception ex) {}
        return histories;
    }

    public ArrayList<History> getHistory(String username){
        String sql = "SELECT id, username, name, stock, timestamp FROM history where lower(username) = '" + username.toLowerCase() + "'";
        ArrayList<History> histories = new ArrayList<History>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getString("timestamp")));
            }
        } catch (Exception ex) {}
        return histories;
    }

    public ArrayList<Logs> getLogs(){
        String sql = "SELECT id, event, username, desc, timestamp FROM logs";
        ArrayList<Logs> logs = new ArrayList<Logs>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                logs.add(new Logs(rs.getInt("id"),
                        rs.getString("event"),
                        rs.getString("username"),
                        rs.getString("desc"),
                        rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return logs;
    }

    public ArrayList<Product> getProduct(){
        String sql = "SELECT id, name, stock, price FROM product";
        ArrayList<Product> products = new ArrayList<Product>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                products.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getFloat("price")));
            }
        } catch (Exception ex) {}
        return products;
    }

    public ArrayList<User> getUsers(){
        String sql = "SELECT id, username, password, role, locked FROM users";
        ArrayList<User> users = new ArrayList<User>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role"),
                        rs.getInt("locked")));
            }
        } catch (Exception ex) {}
        return users;
    }

    public void addHistory(String username, String name, int stock, String timestamp) {
        String sql = "INSERT INTO history(username,name,stock,timestamp) VALUES('" + username + "','" + name + "','" + stock + "','" + timestamp + "')";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }

    public void addLogs(String event, String username, String desc, String timestamp) {
        String sql = "INSERT INTO logs(event,username,desc,timestamp) VALUES('" + event + "','" + username + "','" + desc + "','" + timestamp + "')";

        String fileName = "logs.txt";
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(fileName));
            out.write("----------------------\n");
            out.write(event + "\n");
            out.write(username + "\n");
            out.write(desc + "\n");
            out.write(timestamp + "\n");
            out.write("----------------------\n");
            out.close();
        }
        catch (IOException e) {
            System.out.println("Exception Occurred" + e);
        }

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }

    public void searchLogsUser(String username)
    {
        String sql = "SELECT id, event, username, desc, timestamp FROM logs where username = '" + username +"'";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }

    public void searchLogsEvent(String event)
    {
        String sql = "SELECT id, event, username, desc, timestamp FROM logs where event = '" + event +"'";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }

    public void searchLogsTimeStamp(String time)
    {
        String sql = "SELECT id, event, username, desc, timestamp FROM logs where event LIKE '" + time +"'";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }

    public void deleteLogs(String timestamp) {
        String sql = "DELETE FROM logs WHERE timestamp = '" + timestamp + "'";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }



    public void deleteProduct(String name){

        String sql = "DELETE FROM product WHERE name = '" + name +"';";
        System.out.println(name);
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }

    public void addProduct(String name, int stock, double price) {
        String sql = "INSERT INTO product(name,stock,price) VALUES('" + name + "','" + stock + "','" + price + "')";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }

    public void editProduct(String name, int stock, double price){
        String sql = "UPDATE product SET stock = '" + stock + "', price = '" + price + "' WHERE name = '" + name + "'";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }

    public void  purchaseProduct(String name, int stockval){
        String sql = "UPDATE product SET stock = " + stockval + " WHERE name = '" + name + "'";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {}
    }
    
    public void addUser(String username, String password) {

        String encrypted_password = AES.encrypt(password);
        String sql = "INSERT INTO users(username,password) VALUES('" + username + "', '" + encrypted_password + "')";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            
//  For this activity, we would not be using prepared statements first.
//      String sql = "INSERT INTO users(username,password) VALUES(?,?)";
//      PreparedStatement pstmt = conn.prepareStatement(sql)) {
//      pstmt.setString(1, username);
//      pstmt.setString(2, password);
//      pstmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isAcceptable(String username, String password) {
        if (password.length() < 8 || password.length() > 15
        || username.length() < 6 || username.length() > 15) {
            return false;
        }
        else return usernameValidity(username) && passwordValidity(password);
    }

    private boolean usernameValidity(String username) {
        if (username.contains("'") || username.contains("\"")) {
            return false;
        }
        else {
            return username.matches("[a-zA-Z0-9_]+");
        }
    }

    public boolean passwordValidity(String password) {
        int ucCount = 0;
        int lcCount = 0;
        int numCount = 0;
        int symCount = 0;
        char[] sym = password.toCharArray();
        for (char c: sym) {
            // quotes are not allowed in the password
            if (c == '"' || c == '\'') {
                return false;
            }
            else if (c >= 'a' && c <= 'z') {
                lcCount += 1;
            }
            else if (c >= 'A' && c <= 'Z') {
                ucCount += 1;
            }
            else if (c >= '0' && c <= '9') {
                numCount += 1;
            }
            else if ("!#$%&()*+,-./:;<=>?@[\\\\]^_`{}~".contains(String.valueOf(c))) {
                symCount += 1;
            }
        }
        return ucCount >= 2 && lcCount >= 3 && numCount >= 2 && symCount >= 1;
    }

    public User getUser(String username) {
        if (!(username.contains("'") || username.contains("\""))) {

            String sql = "SELECT id, username, password, role, locked FROM users where lower(username) = " + username.toLowerCase();

            try (Connection conn = DriverManager.getConnection(driverURL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    return new User(rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("role"),
                            rs.getInt("locked"));
                }
            } catch (Exception ex) {

            }
            return null;
        }
        return null;
    }
    public void addUser(String username, String password, int role) {

        String encrypted_password = AES.encrypt(password);

        String sql = "INSERT INTO users(username,password,role) VALUES('" + username + "','" + encrypted_password + "','" + role + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean userExists(String username) {
        if (!(username.contains("'") || username.contains("\""))) {
            String sql = "select * from users where lower(username) = '" + username.toLowerCase() + "'";
            try (Connection conn = DriverManager.getConnection(driverURL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
        else {
            return false;
        }
    }
    public int authenticate(String username, String password) {

        // refuse password if it contains single or double quotes (SQL injection prevention)
        if (username.contains("'") || username.contains("\"") || password.contains("'") || password.contains("\"")) {
            return -99;
        }
        String sql = "select * from users where lower(username) = '" + username.toLowerCase() + "'";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String the_p = AES.decrypt(rs.getString("password"));
                if (the_p.equals(password)) {
                    return rs.getInt("role");
                }
                return -99;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -99;
    }

    public void changeRole(String username, int role){
        String sql = "UPDATE users SET role = '" + role + "' WHERE username='" + username + "'";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removeUser(String username) {
        String sql = "DELETE FROM users WHERE username='" + username + "');";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("User " + username + " has been deleted.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Product getProduct(String name){
        if (!(name.contains("'") || name.contains("\""))) {
            String sql = "SELECT name, stock, price FROM product WHERE name='" + name + "';";
            Product product = null;
            try (Connection conn = DriverManager.getConnection(driverURL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)){
                product = new Product(rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getFloat("price"));
            } catch (Exception ex) {}
            return product;
        }
        else {
            return null;
        }
    }

    public void setPassword(String user, String password) {
        try (Connection conn = DriverManager.getConnection(driverURL)
        ){
            String query = "UPDATE users SET password = ? WHERE lower(username) = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, AES.encrypt(password));
            preparedStmt.setString(2, user);

            preparedStmt.executeUpdate();

        } catch (Exception ex) {}
    }
    public void setLockout(String user, int lockout) {
        try (Connection conn = DriverManager.getConnection(driverURL)
             ){
            String query = "UPDATE users SET locked = ? WHERE lower(username) = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt   (1, lockout);
            preparedStmt.setString(2, user);

            preparedStmt.executeUpdate();

        } catch (Exception ex) {}
    }

    public boolean isLocked(String user) {
        if (!(user.contains("'") || user.contains("\""))) {
            String sql = "select * from users where lower(username) = '" + user.toLowerCase() + "' and locked = 1";
            try (Connection conn = DriverManager.getConnection(driverURL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
        else {
            return false;
        }
    }
}
