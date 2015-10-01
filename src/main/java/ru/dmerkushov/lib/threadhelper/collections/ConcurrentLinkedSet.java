/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author dmerkushov
 */
public class ConcurrentLinkedSet<E> implements java.util.Set<E> {

	private Set<E> internalSet;

	public ConcurrentLinkedSet () {
		internalSet = Collections.synchronizedSet (new LinkedHashSet<E> ());
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

}
