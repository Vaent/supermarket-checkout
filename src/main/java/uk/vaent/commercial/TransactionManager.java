package uk.vaent.commercial;

public interface TransactionManager {
    public int checkout();
    public boolean pay();
    public int scan(char itemCode) throws ItemNotDefinedException, TransactionClosedException;
}
