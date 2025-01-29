package com.team03.excel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheet {

	public int index() default 0;
	public String name() default "";

	public String heading() default "";
}
