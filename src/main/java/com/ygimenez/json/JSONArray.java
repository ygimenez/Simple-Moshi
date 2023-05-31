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
 * Collection representing an array. This is an extension of {@link ArrayList}, and as such all of its methods are
 * supported.
 */
public class JSONArray extends ArrayList<Object> implements Cloneable {

	/**
	 * Creates a new empty JSON array.
	 */
	public JSONArray() {
	}

	/**
	 * Creates a new JSON array based on the supplied {@link Collection}.
	 *
	 * @param collection The entries to be included in this JSON array.
	 */
	public JSONArray(Collection<?> collection) {
		addAll(collection);
	}

	/**
	 * Parse the supplied stringified JSON into a usable list.
	 *
	 * @param json The stringified JSON to be deserialized.
	 */
	public JSONArray(@Language("JSON5") String json) {
		this(JSONUtils.toList(json));
	}

	/**
	 * Transforms the supplied object into a JSON array.
	 *
	 * @param obj The object to be converted to JSON array.
	 */
	public JSONArray(Object obj) {
		this(JSONUtils.toJSON(obj));
	}

	/**
	 * Creates a new JSON array from the supplied objects.
	 *
	 * @param objs The entries to be included in this JSON array.
	 * @return A {@link JSONArray} instance pre-populated with the supplied entries.
	 */
	public static JSONArray of(Object... objs) {
		return new JSONArray(Arrays.asList(objs));
	}

	/**
	 * Retrieves the value at the specified index.
	 *
	 * @param index The index to be retrieved.
	 * @return The value if the index was found, null otherwise.
	 */
	public Object get(int index) {
		return super.get(index);
	}

	/**
	 * Retrieves the value at the specified index with a fallback option if the index doesn't exist.
	 *
	 * @param index The index to be retrieved.
	 * @param or    The value to be returned if the index is not found.
	 * @return The value if the index was found, the value of "or" otherwise.
	 */
	public Object get(int index, Object or) {
		return JSONUtils.getOr(get(index), or);
	}

	/**
	 * Retrieves the value at the specified index and attempts to cast to the specified class.
	 *
	 * @param <T>   Generic type.
	 * @param klass The {@link Class} to attempt to cast the value to.
	 * @param index The index to be retrieved.
	 * @return The value if the index was found, null otherwise.
	 */
	public <T> T get(Class<T> klass, int index) {
		return get(klass, index, null);
	}

	/**
	 * Retrieves the value at the specified index and attempts to cast to the specified class with a fallback option if
	 * the index doesn't exist.
	 *
	 * @param <T>   Generic type.
	 * @param klass The {@link Class} to attempt to cast the value to.
	 * @param index The index to be retrieved.
	 * @param or    The value to be returned if the index is not found.
	 * @return The value if the index was found, the value of "or" otherwise.
	 */
	public <T> T get(Class<T> klass, int index, T or) {
		try {
			return klass.cast(get(index));
		} catch (ClassCastException e) {
			return or;
		}
	}

	/**
	 * Retrieves the value at the specified index as an {@link Enum}.
	 *
	 * @param <E>   Generic enum type.
	 * @param klass The {@link Enum} type of the value.
	 * @param index The index to be retrieved.
	 * @return The value if the index was found and is a valid enum, null otherwise.
	 */
	public <E extends Enum<E>> E getEnum(Class<E> klass, int index) {
		return getEnum(klass, index, null);
	}

	/**
	 * Retrieves the value at the specified index as an {@link Enum} with a fallback option if the index doesn't exist or
	 * isn't a member of the specified enum.
	 *
	 * @param <E>   Generic enum type.
	 * @param klass The {@link Enum} type of the value.
	 * @param index The index to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the index was found and is a valid enum, the value of "or" otherwise.
	 */
	public <E extends Enum<E>> E getEnum(Class<E> klass, int index, E or) {
		if (!klass.isEnum()) return null;
		return Arrays.stream(klass.getEnumConstants())
				.filter(e -> e.name().equalsIgnoreCase(getString(index)))
				.findFirst()
				.orElse(or);
	}

	/**
	 * Retrieves the value at the specified index as a boolean.
	 *
	 * @param index The index to be retrieved.
	 * @return The value if the index was found and is a boolean, false otherwise.
	 */
	public boolean getBoolean(int index) {
		return getBoolean(index, false);
	}

	/**
	 * Retrieves the value at the specified index as a boolean with a fallback option if the index doesn't exist or
	 * is not of the desired type.
	 *
	 * @param index The index to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the key was found and is a boolean, the value of "or" otherwise.
	 */
	public boolean getBoolean(int index, boolean or) {
		try {
			return (boolean) get(index, or);
		} catch (ClassCastException | IndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * Retrieves the value at the specified index as a double.
	 *
	 * @param index The index to be retrieved.
	 * @return The value if the index was found and is a double, 0 otherwise.
	 */
	public double getDouble(int index) {
		return getDouble(index, 0);
	}

	/**
	 * Retrieves the value at the specified index as a double with a fallback option if the index doesn't exist or
	 * is not of the desired type.
	 *
	 * @param index The index to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the key was found and is a double, the value of "or" otherwise.
	 */
	public double getDouble(int index, double or) {
		try {
			return ((Number) get(index, or)).doubleValue();
		} catch (ClassCastException e) {
			try {
				return Double.parseDouble((String) get(index, String.valueOf(or)));
			} catch (NumberFormatException ex) {
				return or;
			}
		} catch (IndexOutOfBoundsException e) {
			return or;
		}
	}

	/**
	 * Retrieves the value at the specified index as a float.
	 *
	 * @param index The index to be retrieved.
	 * @return The value if the index was found and is a float, 0 otherwise.
	 */
	public float getFloat(int index) {
		return getFloat(index, 0);
	}

	/**
	 * Retrieves the value at the specified index as a float with a fallback option if the index doesn't exist or
	 * is not of the desired type.
	 *
	 * @param index The index to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the key was found and is a float, the value of "or" otherwise.
	 */
	public float getFloat(int index, float or) {
		try {
			return ((Number) get(index, or)).floatValue();
		} catch (ClassCastException e) {
			try {
				return Float.parseFloat((String) get(index, String.valueOf(or)));
			} catch (NumberFormatException ex) {
				return or;
			}
		} catch (IndexOutOfBoundsException e) {
			return or;
		}
	}

	/**
	 * Retrieves the value at the specified index as an int.
	 *
	 * @param index The index to be retrieved.
	 * @return The value if the index was found and is an int, 0 otherwise.
	 */
	public int getInt(int index) {
		return getInt(index, 0);
	}

	/**
	 * Retrieves the value at the specified index as a int with a fallback option if the index doesn't exist or
	 * is not of the desired type.
	 *
	 * @param index The index to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the key was found and is a int, the value of "or" otherwise.
	 */
	public int getInt(int index, int or) {
		try {
			return ((Number) get(index, or)).intValue();
		} catch (ClassCastException e) {
			try {
				return Integer.parseInt((String) get(index, String.valueOf(or)));
			} catch (NumberFormatException ex) {
				return or;
			}
		} catch (IndexOutOfBoundsException e) {
			return or;
		}
	}

	/**
	 * Retrieves the value at the specified index as a long.
	 *
	 * @param index The index to be retrieved.
	 * @return The value if the index was found and is a long, 0 otherwise.
	 */
	public long getLong(int index) {
		return getLong(index, 0);
	}

	/**
	 * Retrieves the value at the specified index as a long with a fallback option if the index doesn't exist or
	 * is not of the desired type.
	 *
	 * @param index The index to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the key was found and is a long, the value of "or" otherwise.
	 */
	public long getLong(int index, long or) {
		try {
			return ((Number) get(index, or)).longValue();
		} catch (ClassCastException e) {
			try {
				return Long.parseLong((String) get(index, String.valueOf(or)));
			} catch (NumberFormatException ex) {
				return or;
			}
		} catch (IndexOutOfBoundsException e) {
			return or;
		}
	}

	/**
	 * Retrieves the value at the specified index as a String.
	 *
	 * @param index The index to be retrieved.
	 * @return The value if the index was found and is a String, "" otherwise.
	 */
	public String getString(int index) {
		return getString(index, "");
	}

	/**
	 * Retrieves the value at the specified index as a String with a fallback option if the index doesn't exist or
	 * is not of the desired type.
	 *
	 * @param index The index to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the key was found and is a String, the value of "or" otherwise.
	 */
	public String getString(int index, String or) {
		return String.valueOf(get(index, or));
	}

	/**
	 * Retrieves the value at the specified index as a {@link JSONArray}.
	 *
	 * @param index The index to be retrieved.
	 * @return The value if the index was found and is a {@link JSONArray}, or a {@link List}, an empty JSON array
	 * otherwise.
	 */
	public JSONArray getJSONArray(int index) {
		return getJSONArray(index, new JSONArray());
	}

	/**
	 * Retrieves the value at the specified index as a {@link JSONArray} with a fallback option if the index doesn't exist or
	 * is not of the desired type.
	 *
	 * @param index The index to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the index was found and is a {@link JSONArray} or a {@link List}, the value of "or"
	 * otherwise.
	 */
	public JSONArray getJSONArray(int index, JSONArray or) {
		try {
			return (JSONArray) get(index, or);
		} catch (ClassCastException e) {
			try {
				return new JSONArray((List<?>) get(index, or));
			} catch (ClassCastException ex) {
				return or;
			}
		} catch (IndexOutOfBoundsException e) {
			return or;
		}
	}

	/**
	 * Retrieves the value at the specified index as a {@link JSONObject}.
	 *
	 * @param index The index to be retrieved.
	 * @return The value if the index was found and is a {@link JSONObject} or an {@link Object}, an empty JSON object
	 * otherwise.
	 */
	public JSONObject getJSONObject(int index) {
		return getJSONObject(index, new JSONObject());
	}

	/**
	 * Retrieves the value at the specified index as a {@link JSONObject} with a fallback option if the index doesn't exist or
	 * is not of the desired type.
	 *
	 * @param index The index to be retrieved.
	 * @param or    The fallback value.
	 * @return The value if the index was found and is a {@link JSONObject} or a {@link Map}, the value of "or"
	 * otherwise.
	 */
	public JSONObject getJSONObject(int index, JSONObject or) {
		try {
			return (JSONObject) get(index, or);
		} catch (ClassCastException e) {
			Object obj = get(index);
			return obj == null ? or : new JSONObject(obj);
		} catch (IndexOutOfBoundsException e) {
			return or;
		}
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
	 * @param index The index to be incremented.
	 */
	public void increment(int index) {
		Object obj = get(index);
		if (obj == null) return;

		if (obj instanceof Number) {
			Number n = (Number) obj;
			if (n.longValue() == n.doubleValue()) {
				set(index, n.longValue() + 1);
			} else {
				set(index, n.doubleValue() + 1);
			}
		}
	}

	/**
	 * Concatenates the {@link String} representation of each entry using the supplied
	 * separator into a single {@link String}.
	 *
	 * @param separator The sequence to be placed between each element.
	 * @return A concatenated {@link String} representation of the elements.
	 */
	public String join(String separator) {
		List<String> out = new ArrayList<>();
		for (Object elem : this) {
			out.add(String.valueOf(elem));
		}

		return String.join(separator, out);
	}

	/**
	 * Convenience method for usage in Groovy. This is the opposite operation of leftShift (&lt;&lt;).
	 * <br>
	 * <pre>
	 * {@code
	 * 		arr << "hello"
	 * 		arr << "world"
	 * 		println arr // ["hello", "world"]
	 * 		arr >> 0
	 * 		println arr // ["world"]
	 * }
	 * </pre>
	 *
	 * @param index The index to be removed.
	 * @return The previously assigned value, or null if it didn't exist.
	 */
	public Object rightShift(int index) {
		if (index < 0) return rightShift(size() - (index % size()));
		else if (index >= size()) return rightShift(index % size());

		return remove(index);
	}

	/**
	 * Convenience method for usage in Groovy. This is the opposite operation of leftShift (&lt;&lt;).
	 * <br>
	 * <pre>
	 * {@code
	 * 		arr << "example"
	 * 		println arr // ["example"]
	 * 		arr >> "example"
	 * 		println arr // []
	 * }
	 * </pre>
	 *
	 * @param obj The value to be removed.
	 * @return Whether the value was found and removed or not.
	 */
	public boolean rightShift(Object obj) {
		return remove(obj);
	}

	/**
	 * Converts this JSON array into a JSON object using a 1:1 association with the supplied list of keys.
	 *
	 * @param keys The list of keys to be associated with the values.
	 * @return A JSON object with the values mapped to the keys.
	 */
	public JSONObject toJSONObject(JSONArray keys) {
		JSONObject out = new JSONObject();
		for (int i = 0; i < keys.size(); i++) {
			out.put(keys.getString(i), get(i));
		}

		return out;
	}

	/**
	 * Serializes this list to a stringified JSON.
	 *
	 * @return A {@link String} representation of this JSON object.
	 */
	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}

	/**
	 * Creates a shallow copy of this JSON array. The containing references will be the same across both copies.
	 *
	 * @return A copy of this list.
	 */
	@Override
	public JSONArray clone() {
		return new JSONArray(this);
	}

	/**
	 * Creates a deep copy of this JSON array. This will serialize and deserialize the elements, thus creating an
	 * independent copy of this list.
	 *
	 * @return A copy of this list.
	 */
	public JSONArray deepClone() {
		return new JSONArray(this.toString());
	}
}
