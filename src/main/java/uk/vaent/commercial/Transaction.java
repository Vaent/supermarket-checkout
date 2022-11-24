package uk.vaent.commercial;

public interface Transaction {
    public int add(char scannedItem);
    public void close();
    public boolean isClosed();
    public int total();
}
