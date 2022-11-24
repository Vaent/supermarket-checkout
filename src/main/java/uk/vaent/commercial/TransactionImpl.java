package uk.vaent.commercial;

import java.util.*;

public class TransactionImpl implements Transaction {
    private boolean isClosed = false;
    private final Map<Character, Integer> itemQuantities = new HashMap<>();
    private final Set<ItemPrice> pricingScheme;
    private int runningTotal = 0;

    public TransactionImpl(Set<ItemPrice> pricingScheme) {
        this.pricingScheme = pricingScheme;
    }

    public int add(char scannedItem) throws ItemNotDefinedException, TransactionClosedException {
        if (isClosed) throw new TransactionClosedException();
        itemQuantities.put(scannedItem, 1 + itemQuantities.getOrDefault(scannedItem, 0));
        updateRunningTotal(scannedItem);
        return runningTotal;
    }

    public void close() {
        isClosed = true;
    }

    public int total() {
        return runningTotal;
    }

    public void updateRunningTotal(char scannedItem) throws ItemNotDefinedException {
        ItemPrice scannedItemPrice = pricingScheme.stream()
            .filter(itemPrice -> itemPrice.item() == scannedItem)
            .findAny().orElseThrow(ItemNotDefinedException::new);
        if (scannedItemPrice.multiDeal().isPresent()
                && (itemQuantities.get(scannedItem) % scannedItemPrice.multiDeal().get().multiDealQuantity() == 0)) {
            runningTotal += scannedItemPrice.multiDeal().get().multiDealPriceInPence()
                - (scannedItemPrice.unitPriceInPence() * (scannedItemPrice.multiDeal().get().multiDealQuantity() - 1));
        } else {
            runningTotal += scannedItemPrice.unitPriceInPence();
        }
    }
}
