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
    public Account getAccountByUsername(String username) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
