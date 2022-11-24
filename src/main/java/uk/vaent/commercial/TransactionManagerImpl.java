package uk.vaent.commercial;

public class TransactionManagerImpl {
    PricingSchemeService pricingSchemeService = new PricingSchemeServiceImpl();
    TransactionFactory transactionFactory = new TransactionFactoryImpl();

    public int checkout() {
        return 0;
    }

    public boolean pay() {
        return true;
    }

    public int scan(char itemCode) {
        return 0;
    }

    public void setPricingSchemeService(PricingSchemeService pricingSchemeService) {
        this.pricingSchemeService = pricingSchemeService;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
