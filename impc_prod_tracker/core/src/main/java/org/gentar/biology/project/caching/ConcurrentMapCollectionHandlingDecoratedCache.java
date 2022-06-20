package org.gentar.biology.project.caching;

import java.util.concurrent.ConcurrentMap;
import java.util.stream.StreamSupport;
import org.springframework.cache.Cache;


public class ConcurrentMapCollectionHandlingDecoratedCache
    extends CollectionHandlingDecoratedCache {


    protected ConcurrentMapCollectionHandlingDecoratedCache(final Cache cache) {
        super(cache);
    }

    @Override
    protected boolean areAllKeysPresentInCache(Iterable<?> keys) {

        ConcurrentMap nativeCache = (ConcurrentMap) getNativeCache();

        return StreamSupport.stream(keys.spliterator(), false).allMatch(nativeCache::containsKey);
    }
}