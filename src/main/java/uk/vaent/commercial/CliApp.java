package uk.vaent.commercial;

import java.util.Scanner;

public class CliApp {
    private Scanner scanner = new Scanner(System.in);
    private TransactionManager transactionManager = new TransactionManagerImpl();
    private String validItemsPattern = "^[A-D]$";

    public static void main(String... args) {
        new CliApp().run();
    }

    private void run() {
        System.out.println();
        System.out.println("Enter the word 'quit' at any time to exit the app.");
        while (true) {
            promptUserForInput();
            String input = scanner.nextLine().strip();
            if (input.isEmpty()) continue;
            if (input.equalsIgnoreCase("quit")) return;
            handleTransactionCommand(input);
        }
    }

    private String formatPriceAsPounds(int priceInPence) {
        return String.format("£%.2f", (float)priceInPence/100);
    }

    private void handleTransactionCommand(String input) {
        if (input.equalsIgnoreCase("checkout")) {
            System.out.println("The current subtotal is " + formatPriceAsPounds(transactionManager.checkout()));
        } else if (input.equalsIgnoreCase("pay")) {
            if (transactionManager.pay()) {
                System.out.println("The transaction has been paid");
            } else {
                System.out.println("There was a problem with the payment");
            }
        } else if (input.matches(validItemsPattern)) {
            int subtotal = transactionManager.scan(input.charAt(0));
            System.out.println("The current subtotal is " + formatPriceAsPounds(subtotal));
        } else {
            System.out.println("Input not recognised.");
            if (input.matches(validItemsPattern.toLowerCase())) System.out.println("Make sure you type the exact character (upper/lower case).");
        }
    }

    private void promptUserForInput() {
        System.out.println();
        System.out.println("Enter the word 'checkout' to view the total cost of this transaction.");
        System.out.println("Enter the word 'pay' to finish this transaction");
        System.out.println("Enter a character (A, B, C or D) to scan a new item");
        System.out.print("Your selection: ");
    }
}
