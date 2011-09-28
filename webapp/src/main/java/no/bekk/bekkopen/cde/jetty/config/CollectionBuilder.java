package no.bekk.bekkopen.cde.jetty.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionBuilder {
	private CollectionBuilder() {
	}

	public static <T> Set<T> newSet(final T... elements) {
		return new HashSet<T>(Arrays.asList(elements));
	}

	public static <T> List<T> newList(final T... elements) {
		return new ArrayList<T>(Arrays.asList(elements));
	}
}
