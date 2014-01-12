package tacticulous.tira.datastructure;

import java.util.Iterator;

/**
 * Replacement for arraylist.
 *
 * @author O
 * @param <T> class of stuff stored in a particular instance of the list.
 */
public class TacList<T> implements Iterable<T> {

    private T[] container;
    private int size;
    private int maxSize;

    /**
     * Adds thingy at the end of the list. If necessary, list will increase it's container
     * array's size which will take longer.
     *
     * @param thingy hello thingy!
     */
    public void add(T thingy) {
        if (size == maxSize) {
            maxSize = maxSize * 2;
            T[] tempContainer = (T[]) new Object[maxSize];
            System.arraycopy(container, 0, tempContainer, 0, size);
            container = tempContainer;
        }
        container[size] = thingy;
        size++;
    }

    /**
     * Gets (does not remove) the thingy stored at the provided index.
     *
     * @param index where at yo
     * @return wanted thingy
     */
    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return container[index];
    }

    /**
     * Construcs a new list with an initial size.
     *
     * @param maxSize initial size
     */
    public TacList(int maxSize) {
        if (maxSize < 1) {
            container = (T[]) new Object[1];
            this.maxSize = 1;
        } else {
            container = (T[]) new Object[maxSize];
            this.maxSize = maxSize;
        }


        size = 0;
    }

    /**
     * Checks if provided thingy is in the list.
     *
     * @param t thingly whose existance is in question
     * @return true if found
     */
    public boolean contains(T t) {
        int i = 0;
        while (i < size) {
            if (container[i] == t) {
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * removes a thingy from the list. Takes a while, since thingy must first
     * be found and then everything after it will be moved a slot backwards.
     *
     * @param t thingy to be removed.
     */
    public void remove(T t) {
        int i = 0;
        while (i < size) {
            if (container[i] == t) {
                size--;
                break;
            }
            i++;
        }
        while (i < size) {
            container[i] = container[i + 1];
            i++;
        }
    }

    /**
     * Removes everything from the list. Well, that was a lie. Actually only tells
     * other methods to ignore everything.
     *
     */
    public void clear() {
        size = 0;
    }

    /**
     * Returns the number of thingies on the list.
     *
     * @return how many thingies
     */
    public int size() {
        return size;
    }

    /**
     * Checks if there are any thingies on the list.
     *
     * @return true if no thingies, false if even a single thingy!!!!!!
     */
    public boolean isEmpty() {
        return size <= 0;
    }

    /**
     * Used for nifty forlooping.
     *
     * @return horror.
     */
    @Override
    public Iterator iterator() {
        TacIterator tac = new TacIterator();
        return tac;
    }

    private class TacIterator implements Iterator {

        private int next = 0;

        @Override
        public boolean hasNext() {
            return next < size;
        }

        @Override
        public T next() {
            T ret = (T) container[next];
            next++;
            return ret;
        }

        @Override
        public void remove() {
        }

    }

    /**
     * Thingys from this list.
     *
     * @return a string with thingy toStrings on it!
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        int i = 0;
        while (i < size - 1) {
            sb.append(container[i]).append(", ");
            i++;
        }
        if (size > 0) {
            sb.append(container[i]);
        }
        return sb + "]";

    }

}
