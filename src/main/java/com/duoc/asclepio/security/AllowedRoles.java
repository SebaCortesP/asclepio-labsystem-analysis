package com.duoc.asclepio.security;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AllowedRoles {
    String[] value() default {}; 
}