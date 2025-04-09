package com.apoollo.auto.connect.model;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author liuyulong
 * @since 2025-04-09
 */
public interface ThrowingConsumer<T> extends Consumer<T> {

	void acceptWithException(T t) throws Exception;

	@Override
	default void accept(T t) {
		accept(t, RuntimeException::new);

	}

	default void accept(T t, Function<Exception, RuntimeException> exceptionWrapper) {
		try {
			acceptWithException(t);
		} catch (Exception e) {
			throw exceptionWrapper.apply(e);
		}
	}

}