package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;
import edu.rice.pcdp.PCDP;

import java.util.ArrayList;
import java.util.List;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 * Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
@SuppressWarnings("WeakerAccess")
public final class SieveActor extends Sieve {

    private static final int MAX_LOCAL_PRIMES = 10;

    /**
     * {@inheritDoc}
     * Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        SieveActorActor sieveActorActor = new SieveActorActor(2);
        PCDP.finish(() -> {
            for (int i = 3; i <= limit; i += 2) {
                sieveActorActor.send(i);
            }
            sieveActorActor.send(0);
        });
        int numPrimes = 0;
        SieveActorActor loopActor = sieveActorActor;
        while (loopActor != null) {
            numPrimes += loopActor.getTotalPrimes();
            loopActor = loopActor.getNextActor();
        }
        return numPrimes;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {

        private final List<Integer> localPrimes;
        private SieveActorActor nextActor;

        public SieveActorActor(int localPrime) {
            this.localPrimes = new ArrayList<>(MAX_LOCAL_PRIMES);
            this.nextActor = null;
            localPrimes.add(localPrime);
        }

        public int getTotalPrimes() {
            return localPrimes.size();
        }

        public SieveActorActor getNextActor() {
            return nextActor;
        }

        /**
         * Process a single message sent to this actor.
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            final int candidate = (Integer) msg;
            if(candidate <= 0) {
                if(nextActor != null) {
                    nextActor.send(msg);
                }
            } else {
                if(localPrimes.stream().noneMatch(localPrime -> candidate % localPrime == 0)) {
                    if(localPrimes.size() < MAX_LOCAL_PRIMES) {
                        localPrimes.add(candidate);
                    } else if(nextActor == null) {
                        nextActor = new SieveActorActor(candidate);
                    } else {
                        nextActor.send(candidate);
                    }
                }
            }
        }
    }
}
