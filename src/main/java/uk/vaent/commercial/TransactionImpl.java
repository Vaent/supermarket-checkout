package uk.vaent.commercial;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TransactionImpl implements Transaction {
    private boolean isClosed = false;
    private final Map<Character, Integer> itemQuantities = new HashMap<>();
    private final Set<ItemPrice> pricingScheme;
    private int runningTotal = 0;

    public TransactionImpl(Set<ItemPrice> pricingScheme) {
        this.pricingScheme = pricingScheme;
    }

    public int add(char scannedItem) throws ItemNotDefinedException {
        runningTotal += pricingScheme.stream()
            .filter(itemPrice -> itemPrice.item() == scannedItem)
            .map(ItemPrice::unitPriceInPence)
            .findAny().orElseThrow(ItemNotDefinedException::new);
        return runningTotal;
    }

    public void close() {
        isClosed = true;
    }

    public int total() {
        return runningTotal;
    }
}
