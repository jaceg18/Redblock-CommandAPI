package com.ftjace.commandapi.Annotations;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface ScheduledForRemoval {String inVersion() default "";}
