package com.rpgproject.domain.port;

public interface UserPresenter<T> {

	T ok();

	T error(Exception exception);


}
