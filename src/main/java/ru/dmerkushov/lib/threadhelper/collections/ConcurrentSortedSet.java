/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author dmerkushov
 */
public class ConcurrentSortedSet<E> implements java.util.SortedSet<E> {

	private SortedSet<E> internalSet;

	public ConcurrentSortedSet () {
		internalSet = Collections.synchronizedSortedSet (new TreeSet<E> ());
	}

	public ConcurrentSortedSet (Comparator<E> comparator) {
		internalSet = Collections.synchronizedSortedSet (new TreeSet<E> (comparator));
	}

	@Override
	public int size () {
		return internalSet.size ();
	}

	@Override
	public boolean isEmpty () {
		return internalSet.isEmpty ();
	}

	@Override
	public boolean contains (Object o) {
		return internalSet.contains (o);
	}

	@Override
	public Iterator<E> iterator () {
		return internalSet.iterator ();
	}

	@Override
	public Object[] toArray () {
		return internalSet.toArray ();
	}

	@Override
	public <T> T[] toArray (T[] a) {
		return internalSet.toArray (a);
	}

	@Override
	public boolean add (E e) {
		return internalSet.add (e);
	}

	@Override
	public boolean remove (Object o) {
		return internalSet.remove (o);
	}

	@Override
	public boolean containsAll (Collection<?> c) {
		return internalSet.containsAll (c);
	}

	@Override
	public boolean addAll (Collection<? extends E> c) {
		return internalSet.addAll (c);
	}

	@Override
	public boolean retainAll (Collection<?> c) {
		return internalSet.retainAll (c);
	}

	@Override
	public boolean removeAll (Collection<?> c) {
		return internalSet.removeAll (c);
	}

	@Override
	public void clear () {
		internalSet.clear ();
	}

	@Override
	public Comparator<? super E> comparator () {
		return internalSet.comparator ();
	}

	@Override
	public SortedSet<E> subSet (E fromElement, E toElement) {
		return internalSet.subSet (fromElement, toElement);
	}

	@Override
	public SortedSet<E> headSet (E toElement) {
		return internalSet.headSet (toElement);
	}

	@Override
	public SortedSet<E> tailSet (E fromElement) {
		return internalSet.tailSet (fromElement);
	}

	@Override
	public E first () {
		return internalSet.first ();
	}

	@Override
	public E last () {
		return internalSet.last ();
	}

}
