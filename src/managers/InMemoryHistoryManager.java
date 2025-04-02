package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private HashMap<Integer, Node> historyMap = new HashMap<>();

    private static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            int id = task.getId();
            if (historyMap.containsKey(id)) {
                removeNode(historyMap.get(id));
            }
            linkLast(task);
        }
    }

    @Override
    public void removeHistory(int id) {
        removeNode(historyMap.get(id));

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node newNode = new Node(tail, task, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        historyMap.put(task.getId(), newNode);
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }
        historyMap.remove(node.task.getId());
    }

    private List<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        Node buf = head;
        while (buf != null) {
            history.add(buf.task);
            buf = buf.next;
        }
        return history;
    }
}
