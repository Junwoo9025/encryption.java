import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

class PasswordManager {

    public static String[] retrieveAccounts(String filename) {
        ArrayList<String> accounts = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                accounts.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return accounts.toArray(new String[0]);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String filename = "accounts.txt";
        System.out.println("Welcome to the password manager. This program will help you create a new account "
                + "to store in a file or make changes to existing accounts.\n");

        String user_input = "";
        while (true) {
            System.out.print("Do you want to create a new account or manage an existing one? "
                    + "(Type 'create' to create an account or 'manage' to manage one.) ");
            user_input = input.nextLine().toLowerCase();
            if (user_input.equals("create") || user_input.equals("manage")) {
                break;
            }
            System.out.println("Invalid response. Please provide a valid answer.");
        }

        if (user_input.equals("create")) {
            while (true) {
                System.out.print("\nCreate a username: ");
                String user = input.nextLine();
                if (user.length() < 8) {
                    System.out.println("Username must be at least 8 characters long");
                } else if (usernameExists(filename, user)) {
                    System.out.println("Username already exists. Please choose a different username.");
                } else {
                    String password = getPassword(input);
                    System.out.println("\nYour account has been created.");
                    System.out.printf("Account details:\nUsername: %s\nPassword: %s\n", user, password);
                    storeAccount(filename, user, password);
                    break;
                }
            }
        } else if (user_input.equals("manage")) {
            System.out.println("\nEnter 'v' to view all your accounts.\n");

            while (true) {
                System.out.print("How would you like to manage your account? ");
                String account_manage = input.nextLine().toLowerCase();
                if (account_manage.equals("v")) {
                    String[] account_list = retrieveAccounts(filename);
                    System.out.println("\nUsername: Password");
                    for (String account : account_list) {
                        System.out.println(account);
                    }
                    break;
                } else {
                    System.out.println("Invalid answer. Please provide a valid answer.");
                }
            }
        }

        System.out.println("\nThanks for using our program!");
        input.close();
    }
    private static boolean usernameExists(String filename, String user) {
        try {
            FileReader file = new FileReader(filename);
            BufferedReader reader = new BufferedReader(file);
            String line = reader.readLine();
            while (line != null) {
                String[] account = line.split(":");
                if (account[0].equals(user)) {
                    reader.close();
                    return true;
                }
                line = reader.readLine();
            }
            reader.close();
            return false;
        } catch (IOException e) {
            System.out.println("An error occurred while checking the existing usernames: " + e.getMessage());
            System.exit(1);
            return false;
        }
    }

    private static String getPassword(Scanner input) {
        String password;
        while (true) {
            System.out.print("Create a password: ");
            password = input.nextLine();
            if (password.length() < 12 || !password.matches(".*\\d.*")) {
                System.out.println("Password must be at least 12 characters long and contain at least one digit.");
            } else {
                return password;
            }
        }
    }
    

    private static void storeAccount(String filename, String user, String password) {
        try {
            FileWriter file = new FileWriter(filename, true);
            String encrypted_password = EncryptionUtil.encryptPassword(password);
            file.write(user + ":" + encrypted_password + "\n");
            file.close();
            System.out.println("Account details saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the account details: " + e.getMessage());
            System.exit(1);
        }
    }
    
    class EncryptionUtil {
        public static String encryptPassword(String password) {
            // implementation code goes here
            String encryptedPassword = "";
            for (int i = 0; i < 16; i++) {
                encryptedPassword += (char) (Math.random() * 26 + 'a');
            }
            return encryptedPassword;
        }
    }
    
    
    
    
    
}