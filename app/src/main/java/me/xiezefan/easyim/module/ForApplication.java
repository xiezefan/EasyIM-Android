package me.xiezefan.easyim.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * 表明注入对象来着Application
 * Created by XieZeFan on 2015/4/26 0026.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ForApplication {}
