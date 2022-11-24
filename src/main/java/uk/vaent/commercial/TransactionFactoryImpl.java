package uk.vaent.commercial;

import java.util.Set;

public class TransactionFactoryImpl implements TransactionFactory {
    public Transaction getNew(Set<ItemPrice> pricingScheme) {
        return new TransactionImpl(pricingScheme);
    }
}
