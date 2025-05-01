package com.rpgproject.domain.port;

public interface Presenter<I, O> {

	O ok();

	O ok(I input);

	O error(RuntimeException exception);

}
