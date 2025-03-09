import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// AccountBank class to hold account details
class AccountBank {
    int accountNumber;
    String accountHolderName;
    double balance;
    int cibilScore;

    public AccountBank(int accountNumber, String accountHolderName, double balance, int cibilScore) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.cibilScore = cibilScore;
    }
}

// BankManagementSystem class to manage account operations
class BankManagementSystem {
    List<AccountBank> accounts;

    public BankManagementSystem() {
        accounts = new ArrayList<>();
    }

    public void addAccount(AccountBank account) {
        accounts.add(account);
    }

    public AccountBank findAccount(int accountNumber) {
        for (AccountBank account : accounts) {
            if (account.accountNumber == accountNumber) {
                return account;
            }
        }
        return null;
    }

    public void deleteAccount(int accountNumber) {
        AccountBank account = findAccount(accountNumber);
        if (account != null) {
            accounts.remove(account);
        }
    }

    public void deposit(int accountNumber, double amount) {
        AccountBank account = findAccount(accountNumber);
        if (account != null) {
            account.balance += amount;
            account.cibilScore += (int) (amount / 1000) * 10;  // Increase CIBIL score for every ₹1000 deposited
        }
    }

    public void withdraw(int accountNumber, double amount) {
        AccountBank account = findAccount(accountNumber);
        if (account != null && account.balance >= amount) {
            account.balance -= amount;
            account.cibilScore -= 10; // Decrease CIBIL score for every withdrawal
        }
    }

    public List<AccountBank> getAllAccounts() {
        return accounts;
    }
}

// Admin GUI class for admin interactions
class AdminGUI extends JFrame implements ActionListener {
    BankManagementSystem bank;
    JButton addAccountButton, viewAllAccountsButton, deleteAccountButton, backButton;

    public AdminGUI(BankManagementSystem bank) {
        this.bank = bank;
        setLayout(new FlowLayout());

        addAccountButton = new JButton("Add New Account");
        addAccountButton.addActionListener(this);
        add(addAccountButton);

        viewAllAccountsButton = new JButton("View All Accounts");
        viewAllAccountsButton.addActionListener(this);
        add(viewAllAccountsButton);

        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(this);
        add(deleteAccountButton);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        add(backButton);

        setTitle("Admin Panel");
        setSize(300, 200);
        getContentPane().setBackground(Color.PINK); // Set background color to pink
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == addAccountButton) {
                String accNumInput = JOptionPane.showInputDialog(this, "Enter Account Number:");
                int accountNumber = Integer.parseInt(accNumInput);

                String accHolderName = JOptionPane.showInputDialog(this, "Enter Account Holder Name:");
                String balanceInput = JOptionPane.showInputDialog(this, "Enter Initial Balance:");
                double balance = Double.parseDouble(balanceInput);

                int cibilScore = 650;  // Default CIBIL score

                AccountBank newAccount = new AccountBank(accountNumber, accHolderName, balance, cibilScore);
                bank.addAccount(newAccount);

                JOptionPane.showMessageDialog(this, "New Account Added!");

            } else if (e.getSource() == viewAllAccountsButton) {
                List<AccountBank> accounts = bank.getAllAccounts();
                StringBuilder sb = new StringBuilder();
                for (AccountBank account : accounts) {
                    sb.append("Account Number: " + account.accountNumber + "\n" +
                            "Account Holder: " + account.accountHolderName + "\n" +
                            "Balance: " + account.balance + "\n" +
                            "CIBIL Score: " + account.cibilScore + "\n\n");
                }
                if (sb.length() == 0) {
                    JOptionPane.showMessageDialog(this, "No accounts available.");
                } else {
                    JOptionPane.showMessageDialog(this, sb.toString());
                }
            } else if (e.getSource() == deleteAccountButton) {
                String accNumInput = JOptionPane.showInputDialog(this, "Enter Account Number to Delete:");
                if (accNumInput != null && !accNumInput.isEmpty()) {
                    int accountNumber = Integer.parseInt(accNumInput);

                    AccountBank account = bank.findAccount(accountNumber);
                    if (account != null) {
                        bank.deleteAccount(accountNumber);
                        JOptionPane.showMessageDialog(this, "Account Deleted Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Account not found.");
                    }
                }
            } else if (e.getSource() == backButton) {
                dispose();  // Close the Admin Panel
                new BankSystemApp();  // Reopen the main menu
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values.");
        }
    }
}

// User GUI class for user interactions
class UserGUI extends JFrame implements ActionListener {
    BankManagementSystem bank;  // Reference to the Bank Management System
    JButton viewAccountButton, depositButton, withdrawButton, cibilScoreButton, loanEligibilityButton, backButton;

    public UserGUI(BankManagementSystem bank) {
        this.bank = bank;
        setLayout(new FlowLayout());

        // Creating buttons for the user panel
        viewAccountButton = new JButton("View Account");
        viewAccountButton.addActionListener(this);
        add(viewAccountButton);

        depositButton = new JButton("Deposit Money");
        depositButton.addActionListener(this);
        add(depositButton);

        withdrawButton = new JButton("Withdraw Money");
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        cibilScoreButton = new JButton("View CIBIL Score");
        cibilScoreButton.addActionListener(this);
        add(cibilScoreButton);

        loanEligibilityButton = new JButton("Am I Eligible for Loan?");
        loanEligibilityButton.addActionListener(this);
        add(loanEligibilityButton);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        add(backButton);

        // Set properties of the User Panel
        setTitle("User Panel");
        setSize(300, 300);
        getContentPane().setBackground(Color.PINK); // Set background color to pink
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == viewAccountButton) {
                String accNumInput = JOptionPane.showInputDialog(this, "Enter Account Number:");
                if (accNumInput == null || accNumInput.isEmpty()) return;
                int accountNumber = Integer.parseInt(accNumInput);

                AccountBank account = bank.findAccount(accountNumber);
                if (account != null) {
                    JOptionPane.showMessageDialog(this, "Account Number: " + account.accountNumber + "\n" +
                            "Account Holder: " + account.accountHolderName + "\n" +
                            "Balance: " + account.balance + "\n" +
                            "CIBIL Score: " + account.cibilScore);
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found.");
                }
            } else if (e.getSource() == depositButton) {
                String accNumInput = JOptionPane.showInputDialog(this, "Enter Account Number:");
                int accountNumber = Integer.parseInt(accNumInput);

                String amountInput = JOptionPane.showInputDialog(this, "Enter Deposit Amount:");
                double amount = Double.parseDouble(amountInput);

                bank.deposit(accountNumber, amount);
                JOptionPane.showMessageDialog(this, "Deposit Successful! CIBIL score updated.");
            } else if (e.getSource() == withdrawButton) {
                String accNumInput = JOptionPane.showInputDialog(this, "Enter Account Number:");
                int accountNumber = Integer.parseInt(accNumInput);

                String amountInput = JOptionPane.showInputDialog(this, "Enter Withdrawal Amount:");
                double amount = Double.parseDouble(amountInput);

                bank.withdraw(accountNumber, amount);
                JOptionPane.showMessageDialog(this, "Withdrawal Successful! CIBIL score updated.");
            } else if (e.getSource() == cibilScoreButton) {
                String accNumInput = JOptionPane.showInputDialog(this, "Enter Account Number:");
                int accountNumber = Integer.parseInt(accNumInput);

                AccountBank account = bank.findAccount(accountNumber);
                if (account != null) {
                    JOptionPane.showMessageDialog(this, "Your CIBIL Score: " + account.cibilScore);
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found.");
                }
            } else if (e.getSource() == loanEligibilityButton) {
                String accNumInput = JOptionPane.showInputDialog(this, "Enter Account Number:");
                int accountNumber = Integer.parseInt(accNumInput);

                AccountBank account = bank.findAccount(accountNumber);
                if (account != null) {
                    if (account.cibilScore > 1500) {
                        JOptionPane.showMessageDialog(this, "You are eligible for a loan.");
                    } else {
                        JOptionPane.showMessageDialog(this, "You are not eligible for a loan.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found.");
                }
            } else if (e.getSource() == backButton) {
                dispose();  // Close the User Panel
                new BankSystemApp();  // Reopen the main menu
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values.");
        }
    }
}

// Main application class with login
public class BankSystemApp extends JFrame implements ActionListener {
    JButton userButton, adminButton;
    JTextField usernameField, passwordField;

    public BankSystemApp() {
        setLayout(new FlowLayout());

        usernameField = new JTextField(15);
        add(usernameField);

        passwordField = new JPasswordField(15);
        add(passwordField);

        userButton = new JButton("Login as User");
        userButton.addActionListener(this);
        add(userButton);

        adminButton = new JButton("Login as Admin");
        adminButton.addActionListener(this);
        add(adminButton);

        setTitle("Bank System Login");
        setSize(300, 150);
        getContentPane().setBackground(Color.PINK); // Set background color to pink
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (e.getSource() == userButton) {
            if (username.equals("user") && password.equals("password")) {
                dispose();  // Close the login window
                new UserGUI(new BankManagementSystem());  // Open the User Panel
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        } else if (e.getSource() == adminButton) {
            if (username.equals("admin") && password.equals("admin")) {
                dispose();  // Close the login window
                new AdminGUI(new BankManagementSystem());  // Open the Admin Panel
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        }
    }

    public static void main(String[] args) {
        new BankSystemApp();
    }
}
