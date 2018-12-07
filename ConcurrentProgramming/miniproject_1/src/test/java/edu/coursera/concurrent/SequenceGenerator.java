package edu.coursera.concurrent;

/**
 * A helper class for generating different types of integer sequences. These are
 * not necessarily random sequences.
 */
@SuppressWarnings("WeakerAccess")
public interface SequenceGenerator {

    /**
     * Get the number of integers this generator will generate
     *
     * @return Number of integers in this sequence.
     */
    int sequenceLength();

    /**
     * Get the next integer in this sequence.
     * 
     * @return Next int in the sequence.
     */
    int next();

    /**
     * Reset this sequence generator to generate the sequence from the
     * beginning.
     */
    void reset();

    /**
     * Get a human-readable string describing this sequence.
     *
     * @return Description of this sequence.
     */
    String getLabel();
}
