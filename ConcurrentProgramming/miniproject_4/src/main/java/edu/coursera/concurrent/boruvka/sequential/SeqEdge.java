package edu.coursera.concurrent.boruvka.sequential;

import edu.coursera.concurrent.boruvka.Edge;

/**
 * An edge class used in the sequential Boruvka implementation.
 */
@SuppressWarnings("WeakerAccess")
public final class SeqEdge extends Edge<SeqComponent> implements Comparable<Edge> {

    /**
     * Source component.
     */
    private SeqComponent fromComponent;

    /**
     * Destination component.
     */
    private SeqComponent toComponent;

    /**
     * Weight of this edge.
     */
    private double weight;

    /**
     * Constructor.
     *
     * @param from From edge.
     * @param to To edges.
     * @param w Weight of this edge.
     */
    protected SeqEdge(
            final SeqComponent from,
            final SeqComponent to,
            final double w
    ) {
        fromComponent = from;
        toComponent = to;
        weight = w;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeqComponent fromComponent() {
        return fromComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeqComponent toComponent() {
        return toComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double weight() {
        return weight;
    }

    /**
     * {@inheritDoc}
     */
    public SeqComponent getOther(final SeqComponent from) {
        if (fromComponent == from) {
            assert (toComponent != from);
            return toComponent;
        }

        if (toComponent == from) {
            return fromComponent;
        }
        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Edge e) {
        return Double.compare(weight, e.weight());
    }

    /**
     * {@inheritDoc}
     */
    public SeqEdge replaceComponent(
            final SeqComponent from,
            final SeqComponent to
    ) {
        if (fromComponent == from) {
            fromComponent = to;
        }
        if (toComponent == from) {
            toComponent = to;
        }
        return this;
    }
}
