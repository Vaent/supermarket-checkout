package uk.vaent.commercial.mock;

import uk.vaent.commercial.*;

public class MockTransaction implements Transaction {
    protected int closeInvocationCount = 0;
    protected int runningTotal = 0;

    public int add(char scannedItem) {
        return runningTotal;
    }

    public void close() {
        closeInvocationCount += 1;
    }

    public int getCloseInvocationCount() {
        return closeInvocationCount;
    }


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
