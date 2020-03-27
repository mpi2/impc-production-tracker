package org.gentar.audit.diff;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark fields that need to be ignored when calculating the diff of
 * 2 versions of a entity (to check what changed in an object).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IgnoreForAuditingChanges
{
}
