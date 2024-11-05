package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

class DoublyLinkedList<T extends Task> {
    public Node<T> head;
    public Node<T> tail;
    private int size = 0;


    public void linkLast(Node<T> newNode) {
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }

        tail = newNode;

        size++;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<T> current = head;

        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }

        return tasks;
    }

    public void removeNode() {
        size--;
    }

}