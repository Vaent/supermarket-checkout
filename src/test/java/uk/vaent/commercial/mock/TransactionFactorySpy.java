package uk.vaent.commercial.mock;

import java.util.*;
import uk.vaent.commercial.*;

public class TransactionFactorySpy implements TransactionFactory {
    private final List<Transaction> transactionsSupplied = new ArrayList<>();
    private int transactionsSuppliedCount = 0;

    public Transaction getNew(Set<ItemPrice> pricingScheme) {
        TransactionImpl transaction = new TransactionImpl(pricingScheme);
        transactionsSupplied.add(transaction);
        transactionsSuppliedCount += 1;
        return transaction;
    }

    public Transaction getTransaction(int index) {
        return transactionsSupplied.get(index);
    }

    public int getTransactionsSuppliedCount() {
        return transactionsSuppliedCount;
    }
}
