package uk.vaent.commercial;

public interface Transaction {
    public int add(char scannedItem) throws ItemNotDefinedException, TransactionClosedException;
    public void close();
    public int total();
}
