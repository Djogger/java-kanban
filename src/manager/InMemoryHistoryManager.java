package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node<Task>> tasksIdAndNodes = new HashMap<>();
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
            if (history.head != null) {
                history.head.prev = null;
            }
            if (nodeToRemove.next != null) {
                nodeToRemove.next.prev = nodeToRemove.prev;
            } else {
                history.tail = nodeToRemove.prev;
            }
            if (history.tail != null) {
                history.tail.next = null;
            }

            tasksIdAndNodes.remove(id);
            history.removeNode();
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getTasks();
    }
}