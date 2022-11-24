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

## Design

The core system will comprise three main components:
- a pricing scheme supplier;
- transactions;
- a controller/API.

These components should be sufficiently decoupled that they can be maintained independently.

The controller stands for a point of sale device (checkout/register); as such it will be initialised on application startup and will be available as long as the application is running. Its state will be limited to a single transaction instance which it will initialise, update and close based on user input. It will provide feedback to the API consumer on every operation.

New transactions will be supplied to the controller on demand. Every transaction must be provided with a pricing scheme on initialisation. Transaction state may be updated repeatedly before a final commit operation.

Pricing schemes are to be provided by a separate service, which will be stubbed as a simple Java class for the proof of concept work. Prices will not be highly variable on transaction timescales so could be made cacheable although that is beyond the current scope. Regardless, the controller will need to query the provider every time a new transaction is opened.

The CLI will be developed only after the API is completed.

### Messaging

```
User ----> transactionManager.scan(item)
  |                            |
  |                        getTransaction()
  |                            |
  |                            +------> transaction.add(item)
  |                            |                      |
  |                            |<-- {running total} --+
  |                            |
  |<----- {running total} -----+
```

```
User ----> transactionManager.checkout()
  |                            |
  |                            +------> transaction.total()
  |                            |                      |
  |                            |<-- {running total} --+
  |                            |
  |<----- {running total} -----+
```

```
User ----> transactionManager.pay()
  |                            |
  |                        closeTransaction() --> transaction.close()
  |                            |
  |<---- {confirm payment} ----+
```
Note: no validation of payment is performed since the brief does not include any payment processing requirement. Similarly, no process is defined to cancel a transaction without payment. The payment step is stubbed here to allow closing the transaction in a controlled manner, with the expectation that it would be expanded later.

```
TransactionManager ----> PricingSchemeService.getCurrentScheme()
  |                                              |
  |<--------- {default pricing scheme } ---------+
```

### TransactionManager interface / TransactionManagerImpl class

Additional implementation details to be considered alongside the messaging diagrams above.

#### protected getTransaction() : Transaction

- if no transaction instance is currently open:
  - get current pricing scheme from PricingSchemeService
  - create new transaction using the current pricing scheme
  - store the new transaction
- return the current open transaction

#### protected closeTransaction()

- if no transaction instance is currently open, return early
- invoke close() on the current stored transaction
- drop the reference to the transaction instance

### PricingSchemeService interface / PricingSchemeServiceImpl class 

The stubbed service will always return the following data:

item | unitPriceInPence | multiDealQuantity | multiDealPriceInPence
-----|------------------|-------------------|---
A    | 50               | 3                 | 130
B    | 30               | 2                 | 45
C    | 20               | -                 | -
D    | 15               | -                 | -

#### public getCurrentScheme() : Set\<ItemPrice\>

- returns the current pricing scheme

ItemPrice is a record with the following fields:
- item : char
- unitPriceInPence : int
- multiDeal : Optional\<ItemMultiDeal\>

ItemMultiDeal is a record with the following fields:
- multiDealQuantity : int
- multiDealPriceInPence : int

Note: record types are available in JDK 14 and higher versions.

### Transaction interface / TransactionImpl class

TransactionImpl class excludes the default no-args constructor since the pricing scheme must be loaded up front.

#### public TransactionImpl(Set\<ItemPrice\> pricingScheme)

- store pricingScheme
- initialise and store an empty Map\<char, int\> which will record quantities of scanned items
- initialise an integer = 0 representing the running total in pence
- initialise a boolean = false representing closed status of the transaction

#### public add(char scannedItem) : int

- if this transaction is closed, throw a checked exception
- if the stored pricingScheme does not contain a definition for scannedItem, throw a checked exception
- get current quantity (if any) of scannedItem from stored TransactionStatus
- increment quantity
- put new quantity of scannedItem in stored Map
- updateRunningTotal(scannedItem)
- return the current running total

#### protected updateRunningTotal(char scannedItem)

Assumption: this method is used to update the running total whenever a single item is added to the transaction, and there are no other operations which alter item quantities.

- if the stored priceScheme defines an ItemMultiDeal for scannedItem, and the current recorded quantity of scannedItem is an integer multiple of the multiDealQuantity:
    - increment amount = multiDealPriceInPence - (unitPrice * (multiDealQuantity - 1))
- else:
  - increment amount = unitPriceInPence
- increment the running total per the above conditionals

#### public total() : int

- return the current running total

Note: since the running total is recalculated after every change there should be no need to calculate the final total from scratch. This assumption will rely on thorough testing to ensure the running total is always correct.

#### public close()

- update closed status = true, making this instance unavailable for further mutation
