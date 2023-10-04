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

import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class containing useful methods for serializing and deserializing JSON collections.
 */
public abstract class JSONUtils {
	static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);
	private static boolean showErrors = false;

	private static final Moshi moshi = new Moshi.Builder()
			.add(OffsetDateTime.class, new Rfc3339DateJsonAdapter())
			.build();

	/**
	 * Serializes the supplied object to JSON.
	 *
	 * @param o The object to be serialized.
	 * @return The stringified JSON representation.
	 */
	public static String toJSON(Object o) {
		if (o instanceof String) {
			return (String) o;
		}

		return moshi.adapter(Object.class).toJson(o);
	}

	/**
	 * Deserializes the supplied stringified JSON into an instance of the supplied {@link Class}.
	 *
	 * @param <T>   Generic type.
	 * @param json  The stringified JSON.
	 * @param klass The target {@link Class} to deserialize the JSON to.
	 * @return A deserialized {@link T} instance.
	 */
	public static <T> T fromJSON(String json, Class<T> klass) {
		try {
			return moshi.adapter(klass).nullSafe().fromJson(json);
		} catch (IOException | JsonDataException e) {
			LOGGER.debug(e.toString(), e);
			if (showErrors) {
				LOGGER.info(json);
			} else {
				LOGGER.debug(json);
			}
			return null;
		}
	}

	/**
	 * Deserializes the supplied stringified JSON into a JSON object.
	 *
	 * @param json The stringified JSON.
	 * @return A deserialized JSON object.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(@Language("JSON5") String json) {
		try {
			return (Map<String, Object>) moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class)).fromJson(json);
		} catch (IOException | JsonDataException e) {
			LOGGER.debug(e.toString(), e);
			if (showErrors) {
				LOGGER.info(json);
			} else {
				LOGGER.debug(json);
			}
			return new HashMap<>();
		}
	}

	/**
	 * Deserializes the supplied stringified JSON into a JSON array.
	 *
	 * @param json The stringified JSON.
	 * @return A deserialized JSON array.
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> toList(@Language("JSON5") String json) {
		try {
			return (List<Object>) moshi.adapter(Types.newParameterizedType(List.class, Object.class)).fromJson(json);
		} catch (IOException | JsonDataException e) {
			LOGGER.debug(e.toString(), e);
			if (showErrors) {
				LOGGER.info(json);
			} else {
				LOGGER.debug(json);
			}
			return new ArrayList<>();
		}
	}

	/**
	 * Converts the supplied {@link Object} into a JSON object.
	 *
	 * @param o The object to be converted.
	 * @return A generic JSON object representing the original object.
	 */
	public static Map<String, Object> toMap(Object o) {
		return toMap(toJSON(o));
	}

	/**
	 * Converts the supplied {@link Object} into a JSON array.
	 *
	 * @param o The object to be converted.
	 * @return A generic JSON array representing the original object.
	 */
	public static List<Object> toList(Object o) {
		return toList(toJSON(o));
	}

	/**
	 * Change parsing errors to INFO level.
	 *
	 * @param show Make parsing errors visible at INFO level.
	 */
	public static void showErrors(boolean show) {
		showErrors = show;
	}

	static <T> T getOr(T get, T or) {
		if (get instanceof String && ((String) get).isEmpty()) return or;
		else return get == null ? or : get;
	}
}
