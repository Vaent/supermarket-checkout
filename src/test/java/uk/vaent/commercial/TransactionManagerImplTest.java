package uk.vaent.commercial;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import uk.vaent.commercial.mock.TransactionFactorySpy;

class TransactionManagerImplTest {
    @Test
    void scanFirstItemCreatesNewTransaction() {
        TransactionFactorySpy transactionFactory = new TransactionFactorySpy();
        TransactionManagerImpl transactionManager = new TransactionManagerImpl();
        transactionManager.setTransactionFactory(transactionFactory);
        assertEquals(0, transactionFactory.getTransactionsSuppliedCount());
        transactionManager.scan('A');
        assertEquals(1, transactionFactory.getTransactionsSuppliedCount());
    }

    @Test
    void scanSecondItemDoesNotCreateNewTransaction() {
        TransactionFactorySpy transactionFactory = new TransactionFactorySpy();
        TransactionManagerImpl transactionManager = new TransactionManagerImpl();
        transactionManager.setTransactionFactory(transactionFactory);
        assertEquals(0, transactionFactory.getTransactionsSuppliedCount());
        transactionManager.scan('A');
        assertEquals(1, transactionFactory.getTransactionsSuppliedCount());
        transactionManager.scan('A');
        assertEquals(1, transactionFactory.getTransactionsSuppliedCount());
    }
}