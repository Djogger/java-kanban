package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node<Task>> tasksIdAndNodes= new HashMap<>();
    private DoublyLinkedList<Task> history = new DoublyLinkedList<>();


    @Override
    public void add(Task task) {
        int taskId = task.getIdentificationNumber();

        if (tasksIdAndNodes.containsKey(taskId)) {
            remove(taskId);
        }

        Node<Task> newNode = new Node<>(task);

        history.linkLast(newNode);
        tasksIdAndNodes.put(taskId, newNode);
    }

    @Override
    public void remove(int id) {
        Node<Task> nodeToRemove = tasksIdAndNodes.get(id);
        if (nodeToRemove != null) {
            if (nodeToRemove.prev != null) {
                nodeToRemove.prev.next = nodeToRemove.next;
            } else {
                history.head = nodeToRemove.next;
            }
            if (nodeToRemove.next != null) {
                nodeToRemove.next.prev = nodeToRemove.prev;
            } else {
                history.tail = nodeToRemove.prev;
            }

            tasksIdAndNodes.remove(id);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getTasks();
    }
}


class DoublyLinkedList<Task> {
    public Node<Task> head;
    public Node<Task> tail;
    private int size = 0;


    public void linkLast(Node<Task> newNode) {
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        size++;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> current = head;

        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }

        return tasks;
    }
}
