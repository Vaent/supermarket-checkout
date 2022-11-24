package uk.vaent.commercial.mock;

import java.util.Set;
import uk.vaent.commercial.*;

public class MockTransactionFactory extends TransactionFactorySpy {
    protected MockTransaction mockTransaction;

    public MockTransactionFactory(MockTransaction mockTransaction) {
        this.mockTransaction = mockTransaction;
    }

    @Override
    public Transaction getNew(Set<ItemPrice> pricingScheme) {
        transactionsSupplied.add(mockTransaction);
        transactionsSuppliedCount += 1;
        return mockTransaction;
    }
}
