package com.team03.excel.annotations;


import com.team03.excel.model.ExcelCellType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCell {

	public int index() default 0;

	public String header() default "";

	public ExcelCellType type() default ExcelCellType.DEFAULT;
}
