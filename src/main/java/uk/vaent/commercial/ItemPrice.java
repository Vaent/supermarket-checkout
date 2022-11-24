package uk.vaent.commercial;

import java.util.Optional;

public record ItemPrice(char item, int unitPriceInPence, Optional<ItemMultiDeal> multiDeal) {}
