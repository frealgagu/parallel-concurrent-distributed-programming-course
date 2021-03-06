package edu.coursera.concurrent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Wrapper class for two lock-based concurrent list implementations.
 */
@SuppressWarnings("WeakerAccess")
public final class CoarseLists {
    /**
     * An implementation of the ListSet interface that uses Java locks to
     * protect against concurrent accesses.
     *
     * Implement the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java ReentrantLock object
     * to protect against those concurrent accesses. You may refer to
     * SyncList.java for help understanding the list management logic, and for
     * guidance in understanding where to place lock-based synchronization.
     */
    public static final class CoarseList extends ListSet {

        private final ReentrantLock reentrantLock = new ReentrantLock();
        private final Set<Integer> coarseSet = new HashSet<>();

        /**
         * Default constructor.
         */
        public CoarseList() {
            super();
        }

        /**
         * {@inheritDoc}
         *
         */
        @Override
        boolean add(final Integer object) {
            try {
                reentrantLock.lock();

                if(!coarseSet.add(object)) {
                    return false;
                }

                Entry curr = this.head;
                while (curr.next.object < object) {
                    curr = curr.next;
                }
                if (curr.next.object.intValue() == object) {
                    return false;
                } else {
                    final Entry entry = new Entry(object);
                    entry.next = curr.next;
                    curr.next = entry;
                    return true;
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        /**
         * {@inheritDoc}
         *
         */
        @Override
        boolean remove(final Integer object) {
            try {
                reentrantLock.lock();

                if(!coarseSet.remove(object)) {
                    return false;
                }

                Entry curr = this.head;
                while (curr.next.object < object) {
                    curr = curr.next;
                }
                if (curr.next.object.intValue() == object) {
                    curr.next = curr.next.next;
                    return true;
                } else {
                    return false;
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        /**
         * {@inheritDoc}
         *
         */
        @Override
        boolean contains(final Integer object) {
            try {
                reentrantLock.lock();
                return coarseSet.contains(object);
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /**
     * An implementation of the ListSet interface that uses Java read-write
     * locks to protect against concurrent accesses.
     *
     * Implement the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java
     * ReentrantReadWriteLock object to protect against those concurrent
     * accesses. You may refer to SyncList.java for help understanding the list
     * management logic, and for guidance in understanding where to place
     * lock-based synchronization.
     */
    public static final class RWCoarseList extends ListSet {

        private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private final Set<Integer> coarseSet = new HashSet<>();

        /**
         * Default constructor.
         */
        public RWCoarseList() {
            super();
        }

        /**
         * {@inheritDoc}
         *
         */
        @Override
        boolean add(final Integer object) {
            try {
                reentrantReadWriteLock.writeLock().lock();

                if(!coarseSet.add(object)) {
                    return false;
                }

                Entry curr = this.head;
                while (curr.next.object < object) {
                    curr = curr.next;
                }
                if (curr.next.object.intValue() == object) {
                    return false;
                } else {
                    final Entry entry = new Entry(object);
                    entry.next = curr.next;
                    curr.next = entry;
                    return true;
                }
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
        }

        /**
         * {@inheritDoc}
         *
         */
        @Override
        boolean remove(final Integer object) {
            try {
                reentrantReadWriteLock.writeLock().lock();

                if(!coarseSet.remove(object)) {
                    return false;
                }

                Entry curr = this.head;
                while (curr.next.object < object) {
                    curr = curr.next;
                }
                if (curr.next.object.intValue() == object) {
                    curr.next = curr.next.next;
                    return true;
                } else {
                    return false;
                }
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
        }

        /**
         * {@inheritDoc}
         *
         */
        @Override
        boolean contains(final Integer object) {
            try {
                reentrantReadWriteLock.readLock().lock();
                return coarseSet.contains(object);
            } finally {
                reentrantReadWriteLock.readLock().unlock();
            }
        }
    }
}
