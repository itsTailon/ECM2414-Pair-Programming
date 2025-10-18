package cardgame;

import java.util.NoSuchElementException;

//TODO: Potentially update queue implementation to be atomic (Issue however with updating object size)

/**
 * Implementation of the Queue data structure (FIFO)
 *
 * @param <T> The type of object to be stored in the queue
 */
public class Queue<T> {
    /**
     * Array to hold the items in the queue.
     */
    private volatile T[] items;

    /**
     * Used to track the number of the items in the queue.
     */
    private volatile int size;

    /**
     * Used to keep track of the index of the items array at which to enqueue new items.
     */
    private volatile int rear;

    public Queue() {
        this.items = (T[]) new Object[0];
        this.size = 0;
        this.rear = 0;
    }

    /**
     * Enqueues an item.
     *
     * @param item The item to enqueue.
     */
    public synchronized void enqueue(T item) {
        // Resize items to fit items.length + 1 items
        T[] temp = this.items;
        this.items = (T[]) new Object[this.items.length + 1];
        System.arraycopy(temp, 0, this.items, 0, temp.length);

        // Enqueue the new item
        this.items[this.rear] = item;

        this.rear++;
        this.size++;
    }

    /**
     * Dequeues an item — i.e. returns the item at the front of the queue, and removes it from the queue.
     *
     * @throws NoSuchElementException If the queue is empty.
     *
     * @return The item at the front of the queue.
     */
    public synchronized T dequeue() throws NoSuchElementException {
        // Check if the queue is empty
        if (this.size < 1) {
            // There is nothing to dequeue
            throw new NoSuchElementException("Cannot dequeue an item from an empty queue.");
        }

        // Temporarily hold the item to be dequeued, so that the items array can be resized.
        T item = this.items[0];

        // Resize items to fit (items.length - 1)
        T[] temp = this.items;
        this.items = (T[]) new Object[this.items.length - 1];
        System.arraycopy(temp, 1, this.items, 0, temp.length - 1);

        this.size--;
        this.rear--;

        return item;
    }

    /**
     * Returns the number of items in the queue.
     *
     * @return The size of the queue.
     */
    public synchronized int getSize() {
        return this.size;
    }

    /**
     * Returns the item at the front of the queue, without removing it from the queue.
     * @return The item at the front of the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    public synchronized T peek() {
        // Check if the queue is empty
        if (this.size < 1) {
            // There is nothing to peek
            throw new NoSuchElementException("Cannot peek on an empty queue.");
        }

        return this.items[0];
    }

}
