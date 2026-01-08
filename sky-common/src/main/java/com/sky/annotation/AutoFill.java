package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  //当前注解放在方法前面
@Retention(RetentionPolicy.RUNTIME)  //保证该注解运行时也有效果
public @interface AutoFill {
    OperationType value();
}
