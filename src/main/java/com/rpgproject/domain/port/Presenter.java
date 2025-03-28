package com.rpgproject.domain.port;

public interface Presenter<INPUT, OUTPUT> {

	OUTPUT ok();

	OUTPUT ok(INPUT input);

	OUTPUT error(Exception exception);

}
