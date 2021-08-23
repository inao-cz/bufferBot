package me.inao.discordbot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ParameterType {
    me.inao.discordbot.enums.ParameterType type() default me.inao.discordbot.enums.ParameterType.PROVIDE;
}
