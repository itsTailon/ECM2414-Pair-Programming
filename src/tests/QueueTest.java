package tests;

import cardgame.Queue;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class QueueTest {

    @Test
    public void testEnqueueAndDequeue() {
        Queue<Integer> q = new Queue<Integer>();

        q.enqueue(1);
        q.enqueue(2);

        assertEquals(1, q.dequeue());
        assertEquals(2, q.dequeue());

        assertThrows(NoSuchElementException.class, () -> q.dequeue());
    }

    @Test
    public void testGetSize() {
        Queue<Integer> q = new Queue<Integer>();

        assertEquals(0, q.getSize());

        q.enqueue(1);
        assertEquals(1, q.getSize());

        q.enqueue(2);
        assertEquals(2, q.getSize());
    }

    @Test
    public void testPeek() {
        Queue<Integer> q = new Queue<Integer>();

        // Test on an empty queue
        assertThrows(NoSuchElementException.class, () -> q.peek());

        // Test on a queue with one item
        q.enqueue(1);
        assertEquals(1, q.peek());

        // Test on a queue with two items
        q.enqueue(2);
        assertEquals(1, q.peek());
    }
}
