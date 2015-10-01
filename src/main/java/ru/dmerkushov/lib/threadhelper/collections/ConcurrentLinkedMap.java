/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dmerkushov
 */
public class ConcurrentLinkedMap<K, V> implements Map<K, V> {

	private Map<K, V> internalMap;

	public ConcurrentLinkedMap () {
		internalMap = Collections.synchronizedMap (new java.util.LinkedHashMap<K, V> ());
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

}
