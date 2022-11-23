# supermarket-checkout

## Requirements

Implement code for a checkout system which can:
- keep track of scanned items;
- apply the appropriate prices to each item having regard to any applicable multi-unit special offers;
- calculate running totals and final total.

Items are referenced by letters of the alphabet: [A,B,C,D] each with an associated unit price and optional multi-unit special price. Example: for item A the individual price is 50p, 3 units cost £1.30.

The pricing scheme is variable, to be loaded at the start of every new transaction.

Items are scanned individually with no guaranteed order. Multi-unit prices are applied based on the total quantity of each item per transaction, independent of how they are scanned.

A command line interface will be provided which:
- allows items to be input;
- displays the running total after each input item;
- displays the final calculated total.

The solution is to be coded in Java (language version not specified).
