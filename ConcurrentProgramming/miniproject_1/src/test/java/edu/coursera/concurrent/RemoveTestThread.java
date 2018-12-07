package edu.coursera.concurrent;

/**
 * A Runnable class used to test the performance of each concurrent ListSet
 * implementation. This thread simply hits the list with a large number of
 * remove.
 */
@SuppressWarnings("WeakerAccess")
public class RemoveTestThread extends TestThread implements Runnable {

    private int nSuccessful = 0;
    private int nFailed = 0;

    public RemoveTestThread(final SequenceGenerator seq, final int seqToUse, final ListSet setL) {
        super(seq, seqToUse, setL);
    }

    @Override
    public void run() {
        for (Integer num : nums) {
            if (l.remove(num)) {
                nSuccessful++;
            } else {
                nFailed++;
            }
        }
    }

    public int getNSuccessful() {
        return nSuccessful;
    }

    public int getNFailed() {
        return nFailed;
    }
}
