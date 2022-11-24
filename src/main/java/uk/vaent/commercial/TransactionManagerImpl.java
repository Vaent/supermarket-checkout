package uk.vaent.commercial;

public class TransactionManagerImpl implements TransactionManager {
    Transaction currentTransaction;
    PricingSchemeService pricingSchemeService = new PricingSchemeServiceImpl();
    TransactionFactory transactionFactory = new TransactionFactoryImpl();

    public int checkout() {
        return 0;
    }

    protected Transaction getTransaction() {
        if (currentTransaction == null) {
            currentTransaction = transactionFactory.getNew(pricingSchemeService.getCurrentScheme());
        }
        return currentTransaction;
    }

    public boolean pay() {
        return true;
    }

    public int scan(char itemCode) {
        getTransaction();
        return 0;
    }

    public void setPricingSchemeService(PricingSchemeService pricingSchemeService) {
        this.pricingSchemeService = pricingSchemeService;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
