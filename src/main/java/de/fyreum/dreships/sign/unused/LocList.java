package de.fyreum.dreships.sign.unused;

import de.fyreum.dreships.sign.unused.astar.WaterLocationNode;
import lombok.NonNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LocList<E extends WaterLocation> implements List<E> {

    private static final Set<WaterLocationNode> existingNodes = new HashSet<>();

    private final List<E> list;

    @SuppressWarnings("unused")
    public LocList(@NonNull List<E> list) {
        this.list = list;
    }

    public LocList() {
        this.list = new ArrayList<>();
    }

    public E get(int index) {
        return list.get(index);
    }

    @Override
    public boolean add(E e) {
        return list.add(e);
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
    }

    public E set(int index, E e) {
        return list.set(index, e);
    }

    @Override
    public E remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @NonNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object e) {
        return list.contains(e);
    }

    @NonNull
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @NonNull
    public Object[] toArray() {
        return list.toArray();
    }

    @NonNull
    public <T> T[] toArray(@NonNull T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean remove(Object e) {
        return list.remove(e);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends E> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return list.removeIf(filter);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof LocList)) {
            return false;
        }
        return list.equals(other);
    }

    public int hashCode() {
        return list.hashCode();
    }

    public Spliterator<E> spliterator() {
        return list.spliterator();
    }

    public Stream<E> stream() {
        return list.stream();
    }

    public Stream<E> parallelStream() {
        return list.parallelStream();
    }

    public Set<WaterLocationNode> toNodes() {
        // Creates a new Node for each WaterLocation if not existing.
        for (int i = 0; i < list.size(); i++) {
            WaterLocation current = list.get(i);
            if (existingNodes.stream().filter(n -> n.getLocation().equals(current)).findFirst().orElse(null) != null) {
                continue;
            }
            existingNodes.add(new WaterLocationNode("" + i, current, current.getX(), current.getZ()));
        }
        return existingNodes;
    }
}
