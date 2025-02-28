package com.rpgproject.utils;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.FieldsAnnotated;


public class BasicDatabaseExtension implements BeforeEachCallback {

	@Override
	public void beforeEach(ExtensionContext context) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:test;MODE=Oracle;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
		dataSource.setUsername("sa");
		dataSource.setPassword("");

		Class<?> testClass = context.getRequiredTestInstance().getClass();

		Reflections reflections = new Reflections(
			new ConfigurationBuilder()
				.forPackage(testClass.getName().split("\\.(?=[^.]+$)")[0])
				.setScanners(FieldsAnnotated)
		);

		Set<Field> fields = reflections.getFieldsAnnotatedWith(EzDatabase.class)
			.stream()
			.filter(field -> field.getDeclaringClass().equals(testClass))
			.collect(Collectors.toSet());

		fields.forEach(field -> {
			try {
				Field declaredField = testClass.getDeclaredField(field.getName());
				declaredField.setAccessible(true);

				declaredField.set(
					context.getRequiredTestInstance(),
					field.getType().getDeclaredConstructor(DataSource.class).newInstance(dataSource)
				);

				declaredField.setAccessible(false);
			} catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InstantiationException |
					 InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}

}
