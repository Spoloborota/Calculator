package com.spoloborota.calculator.common;

import java.util.regex.Pattern;

public class Constants {
    public static final String DELIMITERS_STRING = "() +-*/^";
    public static final Pattern INT_OR_FLOAT = Pattern.compile("^[0-9]*[.]?[0-9]+$");
    public static final String DATE_COLUMN = "date";
    public static final String EXPRESSION_COLUMN = "expression";
    public static final String ANY_STRING = "%";
}
