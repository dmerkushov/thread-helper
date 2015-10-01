/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 *
 * @author dmerkushov
 */
public class ConcurrentSortedMap<K, V> implements SortedMap<K, V> {

	private SortedMap<K, V> internalMap;

	public ConcurrentSortedMap () {
		internalMap = Collections.synchronizedSortedMap (new java.util.TreeMap<K, V> ());
	}

	@Override
	public int size () {
		return internalMap.size ();
	}

	@Override
	public boolean isEmpty () {
		return internalMap.isEmpty ();
	}

	@Override
	public boolean containsKey (Object key) {
		return internalMap.containsKey (key);
	}

	@Override
	public boolean containsValue (Object value) {
		return internalMap.containsValue (value);
	}

	@Override
	public V get (Object key) {
		return internalMap.get (key);
	}

	@Override
	public V put (K key, V value) {
		return internalMap.put (key, value);
	}

	@Override
	public V remove (Object key) {
		return internalMap.remove (key);
	}

	@Override
	public void putAll (Map<? extends K, ? extends V> m) {
		internalMap.putAll (m);
	}

	@Override
	public void clear () {
		internalMap.clear ();
	}

	@Override
	public Set<K> keySet () {
		return internalMap.keySet ();
	}

	@Override
	public Collection<V> values () {
		return internalMap.values ();
	}

	@Override
	public Set<Entry<K, V>> entrySet () {
		return internalMap.entrySet ();
	}

	@Override
	public Comparator<? super K> comparator () {
		return internalMap.comparator ();
	}

	@Override
	public SortedMap<K, V> subMap (K fromKey, K toKey) {
		return internalMap.subMap (fromKey, toKey);
	}

	@Override
	public SortedMap<K, V> headMap (K toKey) {
		return internalMap.headMap (toKey);
	}

	@Override
	public SortedMap<K, V> tailMap (K fromKey) {
		return internalMap.tailMap (fromKey);
	}

	@Override
	public K firstKey () {
		return internalMap.firstKey ();
	}

	@Override
	public K lastKey () {
		return internalMap.lastKey ();
	}

}
