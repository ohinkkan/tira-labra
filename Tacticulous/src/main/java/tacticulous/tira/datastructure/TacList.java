package tacticulous.tira.datastructure;

import java.util.Iterator;

/**
 *
 * @author O
 * @param <T>
 */
public class TacList<T> implements Iterable<T> {

    private T[] container;
    private int size;
    private int maxSize;

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

    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return container[index];
    }

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

    public void clear() {
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size <= 0;
    }

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
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

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
