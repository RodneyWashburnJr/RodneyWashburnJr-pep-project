package Service;
import DAO.AccountDAO;
import Model.Account;
public class AccountService {
    private AccountDAO accountDAO;
     

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    public Account addAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty()) {
            System.out.println("Username cannot be null or empty.");
            return null;
        }
        if (account.getPassword() == null || account.getPassword().isEmpty() || account.getPassword().length() < 4) {
            System.out.println("Username cannot be null or empty.");
            return null;
        }

        // Delegate to DAO
        Account newAccount = accountDAO.newAccount(account);
        if (newAccount == null) {
            System.out.println("Account creation failed.");
        }
        return newAccount;
    }
    public Account authenticateUser(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null; 
    }
}
