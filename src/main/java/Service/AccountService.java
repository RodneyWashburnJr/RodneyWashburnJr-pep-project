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
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (account.getPassword() == null || account.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        // Delegate to DAO
        Account newAccount = accountDAO.newAccount(account);
        if (newAccount == null) {
            System.out.println("Account creation failed.");
        }
        return newAccount;
    }
}
