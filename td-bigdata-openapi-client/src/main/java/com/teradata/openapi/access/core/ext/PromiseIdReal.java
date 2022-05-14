package com.teradata.openapi.access.core.ext;

import java.util.HashMap;
import java.util.Map;

import scala.concurrent.Promise;

public class PromiseIdReal {

	@SuppressWarnings("rawtypes")
	private static Map<String, Promise> proMap = null;

	public PromiseIdReal() {

	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Promise> getInstance() {
		if (proMap == null) {
			proMap = new HashMap<String, Promise>();
		}
		return proMap;
	}

}
