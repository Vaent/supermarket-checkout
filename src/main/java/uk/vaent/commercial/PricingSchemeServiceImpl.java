package uk.vaent.commercial;

import java.util.*;

public class PricingSchemeServiceImpl implements PricingSchemeService {
    private static final Set<ItemPrice> defaultPricingScheme;

    static {
        Set<ItemPrice> pricingScheme = new HashSet<>();
        pricingScheme.add(new ItemPrice('A', 50, Optional.of(new ItemMultiDeal(3, 130))));
        pricingScheme.add(new ItemPrice('B', 30, Optional.of(new ItemMultiDeal(2, 45))));
        pricingScheme.add(new ItemPrice('C', 20, Optional.empty()));
        pricingScheme.add(new ItemPrice('D', 15, Optional.empty()));
        defaultPricingScheme = pricingScheme;
    }

    public Set<ItemPrice> getCurrentScheme() {
        return defaultPricingScheme;
    }
}
