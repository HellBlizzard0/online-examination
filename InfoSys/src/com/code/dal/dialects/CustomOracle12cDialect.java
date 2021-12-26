package com.code.dal.dialects;

import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class CustomOracle12cDialect extends Oracle12cDialect {
	public CustomOracle12cDialect() {
		super();

		registerFunction("PKG_NVL", new StandardSQLFunction("PKG_NVL"));
		registerFunction("PKG_LENGTH", new StandardSQLFunction("PKG_LENGTH"));
		registerFunction("PKG_CONCAT", new StandardSQLFunction("PKG_CONCAT"));
		registerFunction("PKG_SUBSTRING", new StandardSQLFunction("PKG_SUBSTRING"));
		registerFunction("PKG_REPLACE", new StandardSQLFunction("PKG_REPLACE"));
		registerFunction("PKG_RTRIM", new StandardSQLFunction("PKG_RTRIM"));

		registerFunction("PKG_DATE_TO_CHAR", new StandardSQLFunction("PKG_DATE_TO_CHAR"));
		registerFunction("PKG_CHAR_TO_DATE", new StandardSQLFunction("PKG_CHAR_TO_DATE"));
		registerFunction("PKG_NUMBER_TO_CHAR", new StandardSQLFunction("PKG_NUMBER_TO_CHAR"));
		registerFunction("PKG_CHAR_TO_NUMBER", new StandardSQLFunction("PKG_CHAR_TO_NUMBER"));

		registerFunction("PKG_RPAD", new StandardSQLFunction("PKG_RPAD"));
		registerFunction("PKG_LPAD", new StandardSQLFunction("PKG_LPAD"));
		registerFunction("PKG_MOD", new StandardSQLFunction("PKG_MOD"));

		registerFunction("PKG_ADD_MONTHS", new StandardSQLFunction("PKG_ADD_MONTHS"));
		registerFunction("PKG_MONTHS_BETWEEN", new StandardSQLFunction("PKG_MONTHS_BETWEEN"));
		
		registerFunction("PKG_DATEPART", new StandardSQLFunction("PKG_DATEPART"));
	}
}
