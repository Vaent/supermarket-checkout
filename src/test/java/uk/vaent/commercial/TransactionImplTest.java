package uk.vaent.commercial;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class TransactionImplTest {
    @Test
    void addItemReturnsUpdatedTotalCost() throws ItemNotDefinedException {
        Set<ItemPrice> pricingSchema = new HashSet<>();
        pricingSchema.add(new ItemPrice('A', 10, Optional.empty()));
        pricingSchema.add(new ItemPrice('B', 25, Optional.empty()));
        Transaction transaction = new TransactionImpl(pricingSchema);
        assertEquals(0, transaction.total(), "Initial total should be zero");
        assertEquals(10, transaction.add('A'), "Transaction.add should return the updated total");
        assertEquals(10, transaction.total(), "Total should equal item price after adding item");
        assertEquals(35, transaction.add('B'), "Adding a different item should increase the total by its given price");
        assertEquals(45, transaction.add('A'), "Adding a duplicate item should increase the total by its given price");
    }

    @Test
    void addItemThrowsExceptionIfItemDoesNotExist() {
        Set<ItemPrice> pricingSchema = new HashSet<>();
        pricingSchema.add(new ItemPrice('A', 10, Optional.empty()));
        Transaction transaction = new TransactionImpl(pricingSchema);
        assertThrows(ItemNotDefinedException.class, () -> transaction.add('X'));
    }
}