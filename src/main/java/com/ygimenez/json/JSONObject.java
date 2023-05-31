/*
 * This file is part of Shiro J Bot.
 * Copyright (C) 2019-2023  Yago Gimenez (KuuHaKu)
 *
 * Shiro J Bot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Shiro J Bot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Shiro J Bot.  If not, see <https://www.gnu.org/licenses/>
 */

package com.ygimenez.json;

import org.intellij.lang.annotations.Language;

import java.util.*;

/**
 * Collection representing an object. This is an extension of {@link HashMap}, and as such all of its methods are
 * supported.
 */
public class JSONObject extends HashMap<String, Object> implements Cloneable, Iterable<Map.Entry<String, Object>> {

	/**
	 * Creates a new empty JSON object.
	 */
	public JSONObject() {
	}

	/**
	 * Creates a new JSON object based on the supplied {@link Map}.
	 *
	 * @param map The entries to be included in this JSON array.
	 */
	public JSONObject(Map<String, Object> map) {
		putAll(map);
	}

	/**
	 * Parse the supplied stringified JSON into a usable object.
	 *
	 * @param json The stringified JSON to be deserialized.
	 */
	public JSONObject(@Language("JSON5") String json) {
		this(JSONUtils.toMap(json));
	}

	/**
	 * Transforms the supplied object into a JSON object.
	 *
	 * @param obj The object to be converted to JSON object.
	 */
	public JSONObject(Object obj) {
		this(JSONUtils.toJSON(obj));
	}

	/**
	 * Creates a new JSON object from the supplied entries.
	 *
	 * @param entries The entries to be included in this JSON object.
	 * @return A {@link JSONObject} instance pre-populated with the supplied entries.
	 */
	@SafeVarargs
	public static JSONObject of(Entry<String, Object>... entries) {
		JSONObject out = new JSONObject();
		for (Entry<String, Object> entry : entries) {
			out.put(entry.getKey(), entry.getValue());
		}

		return out;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return entrySet().iterator();
	}

	/**
	 * Retrieves the value under the supplied key.
	 *
	 * @param key The key to be retrieved.
	 * @return The value if the key was found, null otherwise.
	 */
	public Object get(String key) {
		return super.get(key);
	}

	/**
	 * Retrieves the value under the supplied key with a fallback option if the key doesn't exist.
	 *
	 * @param key The key to be retrieved.
	 * @param or  The value to be returned if the key is not found.
	 * @return The value if the key was found, the value of "or" otherwise.
	 */
	public Object get(String key, Object or) {
		return JSONUtils.getOr(get(key), or);
	}

	/**
	 * Retrieves the value under the supplied key and attempts to cast to the specified class.
	 *
	 * @param <T>   Generic type.
	 * @param klass The {@link Class} to attempt to cast the value to.
	 * @param key   The key to be retrieved.
	 * @return The value if the key was found, null otherwise.
	 */
	public <T> T get(Class<T> klass, String key) {
		return get(klass, key, null);
	}

	/**
	 * Retrieves the value under the supplied key and attempts to cast to the specified class with a fallback option if
	 * the key doesn't exist.
	 *
	 * @param <T>   Generic type.
	 * @param klass The {@link Class} to attempt to cast the value to.
	 * @param key   The key to be retrieved.
	 * @param or    The value to be returned if the key is not found.
	 * @return The value if the key was found, the value of "or" otherwise.
	 */
	public <T> T get(Class<T> klass, String key, T or) {
		try {
			return klass.cast(get(key));
		} catch (ClassCastException e) {
			return or;
		}
	}

	/**
	 * Retrieves the value under the supplied key as an {@link Enum}.
	 *
	 * @param <E>   Generic enum type.
	 * @param klass The {@link Enum} type of the value.
	 * @param key   The key to be retrieved.
	 * @return The value if the key was found and is a valid enum, null otherwise.
	 */
	public <E extends Enum<E>> E getEnum(Class<E> klass, String key) {
		return getEnum(klass, key, null);
	}

	/**
	 * Retrieves the value under the supplied key as an {@link Enum} with a fallback option if the key doesn't exist or
	 * isn't a member of the specified enum.
	 *
	 * @param <E>   Generic enum type.
	 * @param klass The {@link Enum} type of the value.
	 * @param key   The key to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the key was found and is a valid enum, the value of "or" otherwise.
	 */
	public <E extends Enum<E>> E getEnum(Class<E> klass, String key, E or) {
		if (!klass.isEnum()) return null;
		return Arrays.stream(klass.getEnumConstants())
				.filter(e -> e.name().equalsIgnoreCase(getString(key)))
				.findFirst()
				.orElse(or);
	}

	/**
	 * Retrieves the value under the supplied key as a boolean.
	 *
	 * @param key The key to be retrieved.
	 * @return The value if the key was found and is a boolean, false otherwise.
	 */
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/**
	 * Retrieves the value under the supplied key as a boolean with a fallback option if the key doesn't exist or
	 * is not of the desired type.
	 *
	 * @param key The key to be retrieved.
	 * @param or  The fallback value.
	 * @return The value if the key was found and is a boolean, the value of "or" otherwise.
	 */
	public boolean getBoolean(String key, boolean or) {
		try {
			return (boolean) get(key, or);
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * Retrieves the value under the supplied key as a double.
	 *
	 * @param key The key to be retrieved.
	 * @return The value if the key was found and is a double, 0 otherwise.
	 */
	public double getDouble(String key) {
		return getDouble(key, 0);
	}


	/**
	 * Retrieves the value under the supplied key as a double with a fallback option if the key doesn't exist or
	 * is not of the desired type.
	 *
	 * @param key The key to be retrieved.
	 * @param or  The fallback value.
	 * @return The value if the key was found and is a double, the value of "or" otherwise.
	 */
	public double getDouble(String key, double or) {
		try {
			return ((Number) get(key, or)).doubleValue();
		} catch (ClassCastException e) {
			try {
				return Double.parseDouble((String) get(key, String.valueOf(or)));
			} catch (NumberFormatException ex) {
				return or;
			}
		}
	}

	/**
	 * Retrieves the value under the supplied key as a float.
	 *
	 * @param key The key to be retrieved.
	 * @return The value if the key was found and is a float, 0 otherwise.
	 */
	public float getFloat(String key) {
		return getFloat(key, 0);
	}

	/**
	 * Retrieves the value under the supplied key as a float with a fallback option if the key doesn't exist or
	 * is not of the desired type.
	 *
	 * @param key The key to be retrieved.
	 * @param or  The fallback value.
	 * @return The value if the key was found and is a float, the value of "or" otherwise.
	 */
	public float getFloat(String key, float or) {
		try {
			return ((Number) get(key, or)).floatValue();
		} catch (ClassCastException e) {
			try {
				return Float.parseFloat((String) get(key, String.valueOf(or)));
			} catch (NumberFormatException ex) {
				return or;
			}
		}
	}

	/**
	 * Retrieves the value under the supplied key as an int.
	 *
	 * @param key The key to be retrieved.
	 * @return The value if the key was found and is an int, 0 otherwise.
	 */
	public int getInt(String key) {
		return getInt(key, 0);
	}

	/**
	 * Retrieves the value under the supplied key as a int with a fallback option if the key doesn't exist or
	 * is not of the desired type.
	 *
	 * @param key The key to be retrieved.
	 * @param or  The fallback value.
	 * @return The value if the key was found and is a int, the value of "or" otherwise.
	 */
	public int getInt(String key, int or) {
		try {
			return ((Number) get(key, or)).intValue();
		} catch (ClassCastException e) {
			try {
				return Integer.parseInt((String) get(key, String.valueOf(or)));
			} catch (NumberFormatException ex) {
				return or;
			}
		}
	}

	/**
	 * Retrieves the value under the supplied key as a long.
	 *
	 * @param key The key to be retrieved.
	 * @return The value if the key was found and is a long, 0 otherwise.
	 */
	public long getLong(String key) {
		return getLong(key, 0);
	}

	/**
	 * Retrieves the value under the supplied key as a long with a fallback option if the key doesn't exist or
	 * is not of the desired type.
	 *
	 * @param key The key to be retrieved.
	 * @param or  The fallback value.
	 * @return The value if the key was found and is a long, the value of "or" otherwise.
	 */
	public long getLong(String key, long or) {
		try {
			return ((Number) get(key, or)).longValue();
		} catch (ClassCastException e) {
			try {
				return Long.parseLong((String) get(key, String.valueOf(or)));
			} catch (NumberFormatException ex) {
				return or;
			}
		}
	}

	/**
	 * Retrieves the value under the supplied key as a {@link String}.
	 *
	 * @param key The key to be retrieved.
	 * @return The value if the key was found and is a {@link String}, an empty {@link String} otherwise.
	 */
	public String getString(String key) {
		return getString(key, "");
	}

	/**
	 * Retrieves the value under the supplied key as a String with a fallback option if the key doesn't exist or
	 * is not of the desired type.
	 *
	 * @param key The key to be retrieved.
	 * @param or  The fallback value.
	 * @return The value if the key was found and is a String, the value of "or" otherwise.
	 */
	public String getString(String key, String or) {
		return String.valueOf(get(key, or));
	}

	/**
	 * Retrieves the value under the supplied key as a {@link JSONArray}.
	 *
	 * @param key The key to be retrieved.
	 * @return The value if the key was found and is a {@link JSONArray} or a {@link List}, an empty JSON array
	 * otherwise.
	 */
	public JSONArray getJSONArray(String key) {
		return getJSONArray(key, new JSONArray());
	}

	/**
	 * Retrieves the value under the supplied key as a {@link JSONArray} with a fallback option if the key doesn't
	 * exist or is not of the desired type.
	 *
	 * @param key The key to be retrieved.
	 * @param or  The fallback value.
	 * @return The value if the key was found and is a {@link JSONArray}, the value of "or" otherwise.
	 */
	public JSONArray getJSONArray(String key, JSONArray or) {
		try {
			return (JSONArray) get(key, or);
		} catch (ClassCastException e) {
			try {
				return new JSONArray((List<?>) get(key, or));
			} catch (ClassCastException ex) {
				return or;
			}
		}
	}

	/**
	 * Retrieves the value under the supplied key as a {@link JSONObject}.
	 *
	 * @param key The key to be retrieved.
	 * @return The value if the key was found and is a {@link JSONObject} or an {@link Object}, an empty JSON object
	 * otherwise.
	 */
	public JSONObject getJSONObject(String key) {
		return getJSONObject(key, new JSONObject());
	}

	/**
	 * Retrieves the value under the supplied key as a {@link JSONObject} with a fallback option if the key doesn't
	 * exist or is not of the desired type.
	 *
	 * @param key The key to be retrieved.
	 * @param or  The fallback value.
	 * @return The value if the key was found and is a {@link JSONObject}, the value of "or" otherwise.
	 */
	public JSONObject getJSONObject(String key, JSONObject or) {
		try {
			return (JSONObject) get(key, or);
		} catch (ClassCastException e) {
			Object obj = get(key);
			return obj == null ? or : new JSONObject(obj);
		}
	}

	/**
	 * Check if a key exists.
	 *
	 * @param key The key to be checked.
	 * @return Whether the key exists or not.
	 */
	public boolean has(String key) {
		return containsKey(key);
	}

	/**
	 * Convenience method for usage in Groovy. Increments the value of the key's value by 1.
	 * <br>
	 * <pre>
	 * {@code
	 *      obj.val1 = 1
	 *      obj.val2 = 3.14
	 *      println obj // {"val1": 1, "val2": 3.14}
	 * 		obj.val1++
	 * 		obj.val2++
	 * 		println obj // {"val1": 2, "val2": 4.14}
	 * }
	 * </pre>
	 *
	 * @param key The key to be incremented.
	 */
	public void increment(String key) {
		Object obj = get(key);
		if (obj == null) return;

		if (obj instanceof Number) {
			Number n = (Number) obj;
			if (n.longValue() == n.doubleValue()) {
				put(key, n.longValue() + 1);
			} else {
				put(key, n.doubleValue() + 1);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object put(String key, Object value) {
		return super.put(key, value);
	}

	/**
	 * Associates the key with the supplied value if, and only if the key doesn't exist yet or its current value is
	 * null.
	 *
	 * @param key   The key to be associated.
	 * @param value The value to be associated.
	 * @return The previous value associated with the supplied key, null otherwise.
	 */
	public Object putOnce(String key, Object value) {
		return putIfAbsent(key, value);
	}

	/**
	 * Associates the key with the supplied value if, and only if the key doesn't exist yet or its current value is
	 * null and the supplied value is not null.
	 *
	 * @param key   The key to be associated.
	 * @param value The value to be associated.
	 * @return The previous value associated with the supplied key, null otherwise.
	 */
	public Object putOpt(String key, Object value) {
		Object curr = get(key);
		if (curr == null && value != null) {
			return put(key, value);
		}

		return curr;
	}

	/**
	 * Convenience method for usage in Groovy.
	 * <br>
	 * <pre>
	 * {@code
	 * 		obj.example = 123
	 * 		println obj // {"example": 123}
	 * 		obj >> "example"
	 * 		println obj // {}
	 * }
	 * </pre>
	 *
	 * @param obj The key to be removed.
	 * @return The previously assigned value, or null if it didn't exist.
	 */
	public Object rightShift(String obj) {
		return remove(obj);
	}

	/**
	 * Converts this JSON object into a JSON array, discarding all values in the process.
	 *
	 * @return A JSON array containing this object's keys.
	 */
	public JSONArray keys() {
		return new JSONArray(keySet());
	}

	/**
	 * Converts this JSON object into a JSON array, discarding all keys in the process.
	 *
	 * @return A JSON array containing this object's values.
	 */
	public JSONArray values() {
		return new JSONArray(super.values());
	}

	/**
	 * Serializes this object to a stringified JSON.
	 *
	 * @return A {@link String} representation of this JSON object.
	 */
	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

	/**
	 * Creates a shallow copy of this JSON object. The containing references will be the same across both copies.
	 *
	 * @return A copy of this object.
	 */
	@Override
	public JSONObject clone() {
		return new JSONObject(this);
	}

	/**
	 * Creates a deep copy of this JSON object. This will serialize and deserialize the entries, thus creating an
	 * independent copy of this object.
	 *
	 * @return A copy of this object.
	 */
	public JSONObject deepClone() {
		return new JSONObject(this.toString());
	}
}
