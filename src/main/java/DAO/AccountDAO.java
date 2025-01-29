package DAO;
import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;


public class AccountDAO {
    public Account newAccount(Account account){
        Connection conn = ConnectionUtil.getConnection();
        try{
            if (account.getUsername() == null || account.getPassword() == null) {
                throw new IllegalArgumentException("Username and password cannot be null.");
            }
            String setAcc = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(setAcc, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
            ResultSet accountRS = ps.getGeneratedKeys();
            if(accountRS.next()){
                int generatedID = (int) accountRS.getLong(1);
                return new Account(generatedID, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
