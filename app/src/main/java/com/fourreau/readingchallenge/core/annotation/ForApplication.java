package com.fourreau.readingchallenge.core.annotation;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Pierre on 05/07/2015.
 */
@Qualifier
@Retention(RUNTIME)
public @interface ForApplication {
}