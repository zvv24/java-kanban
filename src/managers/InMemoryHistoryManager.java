package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private HashMap<Integer, Node> historyMap = new HashMap<>();

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        historyMap.put(task.getId(), newNode);
    }

    public void removeNode(Node node) {
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

    public List<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        Node i = head;
        while (i != null) {
            history.add(i.task);
            i = i.next;
        }
        return history;
    }

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            int id = task.getId();
            if (historyMap.containsKey(id)){
                removeNode(historyMap.get(id));
            }
            linkLast(task);
        }
    }

    @Override
    public void removeHistory(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
