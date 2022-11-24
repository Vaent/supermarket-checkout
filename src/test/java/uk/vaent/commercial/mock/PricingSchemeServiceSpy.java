package uk.vaent.commercial.mock;

import java.util.Set;
import uk.vaent.commercial.ItemPrice;
import uk.vaent.commercial.PricingSchemeServiceImpl;

public class PricingSchemeServiceSpy extends PricingSchemeServiceImpl {
    private int serviceCallsCount = 0;

    public Set<ItemPrice> getCurrentScheme() {
        serviceCallsCount +=1;
        return super.getCurrentScheme();
    }

    public int getServiceCallsCount() {
        return serviceCallsCount;
    }
}
