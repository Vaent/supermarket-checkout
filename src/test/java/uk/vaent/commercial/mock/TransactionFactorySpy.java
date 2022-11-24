package uk.vaent.commercial.mock;

import java.util.Set;
import uk.vaent.commercial.*;

public class TransactionFactorySpy implements TransactionFactory {
    private int transactionsSuppliedCount = 0;

    public Transaction getNew(Set<ItemPrice> pricingScheme) {
        transactionsSuppliedCount += 1;
        return new TransactionImpl(pricingScheme);
    }

    public int getTransactionsSuppliedCount() {
        return transactionsSuppliedCount;
    }
}
