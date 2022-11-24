package uk.vaent.commercial;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class TransactionImplTest {
    @Test
    void addItemReturnsUpdatedTotalCost() {
        Set<ItemPrice> pricingScheme = new HashSet<>();
        pricingScheme.add(new ItemPrice('A', 10, Optional.empty()));
        pricingScheme.add(new ItemPrice('B', 25, Optional.empty()));
        Transaction transaction = new TransactionImpl(pricingScheme);
        assertEquals(0, transaction.total(), "Initial total should be zero");
        assertEquals(10, transaction.add('A'), "Transaction.add should return the updated total");
        assertEquals(10, transaction.total(), "Total should equal item price after adding item");
        assertEquals(35, transaction.add('B'), "Adding a different item should increase the total by its given price");
        assertEquals(45, transaction.add('A'), "Adding a duplicate item should increase the total by its given price");
    }

    @Test
    void addItemThrowsExceptionIfItemDoesNotExist() {
        Set<ItemPrice> pricingScheme = new HashSet<>();
        pricingScheme.add(new ItemPrice('A', 10, Optional.empty()));
        Transaction transaction = new TransactionImpl(pricingScheme);
        assertThrows(ItemNotDefinedException.class, () -> transaction.add('X'));
    }

    @Test
    void addItemThrowsExceptionIfTransactionIsClosed() {
        Set<ItemPrice> pricingScheme = new HashSet<>();
        pricingScheme.add(new ItemPrice('A', 10, Optional.empty()));
        Transaction transaction = new TransactionImpl(pricingScheme);
        transaction.close();
        assertThrows(TransactionClosedException.class, () -> transaction.add('A'));
    }

    @Test
    void addMultiDealItemRecognisesDealAtRelevantQuantity() {
        Set<ItemPrice> pricingScheme = new HashSet<>();
        pricingScheme.add(new ItemPrice('A', 10, Optional.of(new ItemMultiDeal(3, 25))));
        Transaction transaction = new TransactionImpl(pricingScheme);
        assertEquals(10, transaction.add('A'));
        assertEquals(20, transaction.add('A'));
        assertEquals(25, transaction.add('A'), "Third item added should trigger multi-unit price");
        assertEquals(35, transaction.add('A'));
        assertEquals(45, transaction.add('A'));
        assertEquals(50, transaction.add('A'), "Deal should be applied for every multiple of the multiDealQuantity");
    }

    @Test
    void addMultiDealItemRecognisesDealWhenSequenceIsInterrupted() {
        Set<ItemPrice> pricingScheme = new HashSet<>();
        pricingScheme.add(new ItemPrice('A', 10, Optional.of(new ItemMultiDeal(3, 25))));
        pricingScheme.add(new ItemPrice('B', 100, Optional.empty()));
        Transaction transaction = new TransactionImpl(pricingScheme);
        transaction.add('A');
        assertEquals(20, transaction.add('A'));
        assertEquals(120, transaction.add('B'));
        assertEquals(125, transaction.add('A'), "Third A should trigger multi-unit price");
    }

    @Test
    void addMultiDealItemHandlesDifferentDeals() {
        Set<ItemPrice> pricingScheme = new HashSet<>();
        pricingScheme.add(new ItemPrice('A', 10, Optional.of(new ItemMultiDeal(3, 25))));
        pricingScheme.add(new ItemPrice('B', 100, Optional.of(new ItemMultiDeal(2, 125))));
        Transaction transaction = new TransactionImpl(pricingScheme);
        assertEquals(100, transaction.add('B'));
        assertEquals(110, transaction.add('A'));
        assertEquals(135, transaction.add('B'), "Second B should trigger multi-unit price");
        assertEquals(145, transaction.add('A'));
        assertEquals(150, transaction.add('A'), "Third A should trigger multi-unit price");
    }
}