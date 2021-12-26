package com.code.dal.dialects;

import java.util.ResourceBundle;

import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class CustomPostgreSQL95Dialect extends PostgreSQL95Dialect {

	private static ResourceBundle config = ResourceBundle.getBundle("com.code.resources.config");

	public CustomPostgreSQL95Dialect() {
		super();

		registerFunction("PKG_NVL", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_NVL"));
		registerFunction("PKG_LENGTH", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_LENGTH"));
		registerFunction("PKG_CONCAT", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_CONCAT"));
		registerFunction("PKG_SUBSTRING", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_SUBSTRING"));
		registerFunction("PKG_REPLACE", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_REPLACE"));
		registerFunction("PKG_RTRIM", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_RTRIM"));

		registerFunction("PKG_DATE_TO_CHAR", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_DATE_TO_CHAR"));
		registerFunction("PKG_CHAR_TO_DATE", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_CHAR_TO_DATE"));
		registerFunction("PKG_NUMBER_TO_CHAR", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_NUMBER_TO_CHAR"));
		registerFunction("PKG_CHAR_TO_NUMBER", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_CHAR_TO_NUMBER"));

		registerFunction("PKG_RPAD", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_RPAD"));
		registerFunction("PKG_LPAD", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_LPAD"));
		registerFunction("PKG_MOD", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_MOD"));

		registerFunction("PKG_ADD_MONTHS", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_ADD_MONTHS"));
		registerFunction("PKG_MONTHS_BETWEEN", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_MONTHS_BETWEEN"));
		
		registerFunction("PKG_DATEPART", new StandardSQLFunction(config.getString("hibernate.default_schema") + ".PKG_DATEPART"));
	}
}
