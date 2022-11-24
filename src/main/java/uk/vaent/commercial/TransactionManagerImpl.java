package uk.vaent.commercial;

public class TransactionManagerImpl implements TransactionManager {
    Transaction currentTransaction;
    PricingSchemeService pricingSchemeService = new PricingSchemeServiceImpl();
    TransactionFactory transactionFactory = new TransactionFactoryImpl();

    public int checkout() {
        return getTransaction().total();
    }

    protected void closeTransaction() {
        if (currentTransaction != null) {
            currentTransaction.close();
            currentTransaction = null;
        }
    }

    protected Transaction getTransaction() {
        if (currentTransaction == null || currentTransaction.isClosed()) {
            currentTransaction = transactionFactory.getNew(pricingSchemeService.getCurrentScheme());
        }
        return currentTransaction;
    }

    public boolean pay() {
        closeTransaction();
        return true;
    }

    public int scan(char itemCode) throws ItemNotDefinedException, TransactionClosedException {
        return getTransaction().add(itemCode);
    }

    public void setPricingSchemeService(PricingSchemeService pricingSchemeService) {
        this.pricingSchemeService = pricingSchemeService;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
