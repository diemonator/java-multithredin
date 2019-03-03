package movingballsfx;

public class ReaderWriterMonitor {

    private int writersActive = 0;
    private int writersWaiting = 0;
    private int readersActive = 0;
    private int readersWaiting = 0;

    private boolean isWriterPriorityOn = false;
    private boolean isReaderPriorityOn = false;

    public synchronized void enterReader() {
        try {
            while (writersActive == 1 || (isWriterPriorityOn && writersWaiting > 0)) {
                readersWaiting++;
                wait();
                readersWaiting--;
            }
            readersActive++;
        } catch (InterruptedException e) {
            readersWaiting--;
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void exitReader() {
        readersActive--;
        checkForWaitingBalls();
    }

    public synchronized void enterWriter() {
        try {
            while ((readersActive > 0 || writersActive == 1) || (isReaderPriorityOn && readersWaiting > 0)) {
                writersWaiting++;
                wait();
                writersWaiting--;
            }
            writersActive++;
        } catch (InterruptedException e) {
            writersWaiting--;
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void exitWriter() {
        writersActive--;
        checkForWaitingBalls();
    }

    private void checkForWaitingBalls() {
        if (writersWaiting > 0 || readersWaiting > 0) notifyAll();
    }

    public void setWriterPriority() {
        isWriterPriorityOn = !isWriterPriorityOn;
    }

    public void setReaderPriorityOn() {
        isReaderPriorityOn = !isReaderPriorityOn;
    }

}
