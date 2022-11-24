package uk.vaent.commercial;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import uk.vaent.commercial.mock.*;

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

    @Test
    void whenScanItemAfterTransactionClosedThenNewTransactionCreated() {
        TransactionFactorySpy transactionFactory = new TransactionFactorySpy();
        TransactionManagerImpl transactionManager = new TransactionManagerImpl();
        transactionManager.setTransactionFactory(transactionFactory);
        assertEquals(0, transactionFactory.getTransactionsSuppliedCount());
        // create first transaction
        transactionManager.scan('A');
        assertEquals(1, transactionFactory.getTransactionsSuppliedCount());
        transactionFactory.getTransaction(0).close();
        // create second transaction
        transactionManager.scan('A');
        assertEquals(2, transactionFactory.getTransactionsSuppliedCount());
    }

    @Test
    void pricingSchemeServiceIsCalledWhenTransactionCreatedOnly() {
        PricingSchemeServiceSpy pricingSchemeService = new PricingSchemeServiceSpy();
        TransactionFactorySpy transactionFactory = new TransactionFactorySpy();
        TransactionManagerImpl transactionManager = new TransactionManagerImpl();
        transactionManager.setPricingSchemeService(pricingSchemeService);
        transactionManager.setTransactionFactory(transactionFactory);
        assertEquals(0, pricingSchemeService.getServiceCallsCount());
        // create first transaction
        transactionManager.scan('A');
        assertEquals(1, pricingSchemeService.getServiceCallsCount(), "First scan should create new transaction");
        transactionManager.scan('A');
        assertEquals(1, pricingSchemeService.getServiceCallsCount(), "Second scan should not create new transaction");
        transactionFactory.getTransaction(0).close();
        //create second transaction
        transactionManager.scan('A');
        assertEquals(2, pricingSchemeService.getServiceCallsCount(), "First scan after closing should create new transaction");
    }

    @Test
    void scanItemReturnsTransactionSubtotal() {
        MockTransaction transaction = new MockTransaction();
        MockTransactionFactory transactionFactory = new MockTransactionFactory(transaction);
        TransactionManagerImpl transactionManager = new TransactionManagerImpl();
        transactionManager.setTransactionFactory(transactionFactory);
        transaction.setRunningTotal(42);
        assertEquals(42, transactionManager.scan('A'));
        transaction.setRunningTotal(420);
        assertEquals(420, transactionManager.scan('A'));
    }

    @Test
    void checkoutReturnsTransactionSubtotal() {
        MockTransaction transaction = new MockTransaction();
        MockTransactionFactory transactionFactory = new MockTransactionFactory(transaction);
        TransactionManagerImpl transactionManager = new TransactionManagerImpl();
        transactionManager.setTransactionFactory(transactionFactory);
        transaction.setRunningTotal(42);
        transactionManager.scan('A');
        assertEquals(42, transactionManager.checkout());
    }

    @Test
    void whenPayThenTransactionIsClosed() {
        MockTransaction transaction = new MockTransaction();
        MockTransactionFactory transactionFactory = new MockTransactionFactory(transaction);
        TransactionManagerImpl transactionManager = new TransactionManagerImpl();
        transactionManager.setTransactionFactory(transactionFactory);
        transactionManager.scan('A');
        assertEquals(0, transaction.getCloseInvocationCount());
        assertTrue(transactionManager.pay(), "Pay method should return success response");
        assertEquals(1, transaction.getCloseInvocationCount(), "Transaction should be closed after payment");
        transactionManager.pay();
        assertEquals(1, transaction.getCloseInvocationCount(), "Transaction should not be closed more than once");
    }
}