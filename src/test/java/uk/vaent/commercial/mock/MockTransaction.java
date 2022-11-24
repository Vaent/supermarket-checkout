package uk.vaent.commercial.mock;

import uk.vaent.commercial.*;

public class MockTransaction implements Transaction {
    protected int runningTotal = 0;

    public int add(char scannedItem) throws ItemNotDefinedException, TransactionClosedException {
        return runningTotal;
    }

    public void close() {}

    public boolean isClosed() {
        return false;
    }

    public void setRunningTotal(int value) {
        runningTotal = value;
    }

    public int total() {
        return runningTotal;
    }
}
