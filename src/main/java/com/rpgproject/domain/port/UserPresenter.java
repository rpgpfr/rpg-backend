package com.rpgproject.domain.port;

import com.rpgproject.domain.bean.User;

public interface UserPresenter<T> {

	T ok();

	T ok(User user);

	T error(Exception exception);


}
