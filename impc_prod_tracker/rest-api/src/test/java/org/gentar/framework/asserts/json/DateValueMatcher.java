package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.ValueMatcher;

import java.time.LocalDateTime;

public class DateValueMatcher<T> implements ValueMatcher<T>
{
    @Override
    public boolean equal(Object o, Object t1)
    {
        return obtainedIsADate(o);
    }

    private boolean obtainedIsADate(Object obtained)
    {
        LocalDateTime date = LocalDateTime.parse(obtained.toString());
        return true;
    }
}
