package uk.vaent.commercial;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import uk.vaent.commercial.mock.PricingSchemeServiceSpy;
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
}