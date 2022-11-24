package uk.vaent.commercial;

import java.util.Scanner;

public class CliApp {
    public static void main(String... args) {
        TransactionManager transactionManager = new TransactionManagerImpl();
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter the word 'quit' at any time to exit the app.");
        do {
            System.out.println();
            System.out.println("Enter the word 'checkout' to view the total cost of this transaction.");
            System.out.println("Enter the word 'pay' to finish this transaction");
            System.out.println("Enter a character (A, B, C or D) to scan a new item");
            System.out.print("Your selection: ");
            String input = scanner.nextLine().strip();
            if (input.isEmpty()) continue;
            if (input.equalsIgnoreCase("quit")) return;
            if (input.equalsIgnoreCase("checkout")) {
                System.out.println("The current subtotal is " + transactionManager.checkout());
            } else if (input.equalsIgnoreCase("pay")) {
                if (transactionManager.pay()) {
                    System.out.println("The transaction has been paid");
                } else {
                    System.out.println("There was a problem with the payment");
                }
            } else if (input.matches("^[A-D]$")) {
                int subtotal = transactionManager.scan(input.charAt(0));
                System.out.println("The current subtotal is " + subtotal);
            } else {
                System.out.println("Input not recognised.");
                if (input.matches("^[a-d]$")) System.out.println("Make sure you type the exact character (upper/lower case).");
            }
        } while (true);
    }
}
