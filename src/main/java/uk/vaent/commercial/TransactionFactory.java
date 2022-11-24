package uk.vaent.commercial;

import java.util.Set;

public interface TransactionFactory {
    public Transaction getNew(Set<ItemPrice> pricingScheme);
}
