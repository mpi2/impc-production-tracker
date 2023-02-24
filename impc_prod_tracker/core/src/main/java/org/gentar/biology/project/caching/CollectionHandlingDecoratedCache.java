package org.gentar.biology.project.caching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadOrthologDto;
import org.springframework.cache.Cache;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;


public abstract class CollectionHandlingDecoratedCache implements Cache {

    private final Cache cache;

    protected CollectionHandlingDecoratedCache(Cache cache) {

        Assert.notNull(cache, "Cache must not be null");

        this.cache = cache;
    }

    protected Cache getCache() {
        return this.cache;
    }

    @Override
    public String getName() {
        return getCache().getName();
    }

    @Override
    public Object getNativeCache() {
        return getCache().getNativeCache();
    }

    protected abstract boolean areAllKeysPresentInCache(Iterable<?> keys);

    @SuppressWarnings("unused")
    protected int sizeOf(Iterable<?> iterable) {
        return Long.valueOf(StreamSupport.stream(iterable.spliterator(), false).count()).intValue();
    }

    protected <T> List<T> toList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("all")
    public ValueWrapper get(Object key) {
        if (key instanceof Iterable) {

            Iterable<?> keys = (Iterable<?>) key;

            if (!areAllKeysPresentInCache(keys)) {
                return null;
            }

            Collection<Object> values = new ArrayList<>();

            for (Object singleKey : keys) {
                values.add(getCache().get(singleKey).get());
            }

            return () -> values;
        }

        return getCache().get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {

        if (key instanceof Iterable) {

            Assert.isAssignable(Iterable.class, type,
                String.format(
                    "Expected return type [%1$s] must be Iterable when querying multiple keys [%2$s]",
                    type.getName(), key));

            return (T) Optional.ofNullable(get(key)).map(Cache.ValueWrapper::get).orElse(null);
        }

        return getCache().get(key, type);
    }

    @Override
    @SuppressWarnings("all")
    public <T> T get(Object key, Callable<T> valueLoader) {
        return (T) get(key, Object.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void put(@NonNull Object key, Object value) {

        if (key instanceof Iterable) {

            if (value instanceof Iterable && ((ArrayList) value).size() != 0) {
                pairsFromKeysAndValues(toList((Iterable<?>) key), toList((Iterable<?>) value))
                    .forEach(pair -> getCache().put(pair.getFirst(), pair.getSecond()));
            }
        } else {
            getCache().put(key, value);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {

        if (key instanceof Iterable) {

            Assert.isInstanceOf(Iterable.class, value,
                String.format(
                    "Value [%1$s] must be an instance of Iterable when caching multiple keys [%2$s]",
                    ObjectUtils.nullSafeClassName(value), key));

            return () -> pairsFromKeysAndValues(toList((Iterable<?>) key),
                toList((Iterable<?>) value)).stream()
                .map(pair -> getCache().putIfAbsent(pair.getFirst(), pair.getSecond()))
                .collect(Collectors.toList());
        }

        return getCache().putIfAbsent(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void evict(Object key) {

        if (key instanceof Iterable) {
            StreamSupport.stream(((Iterable) key).spliterator(), false).forEach(getCache()::evict);
        } else {
            getCache().evict(key);
        }
    }

    @Override
    public void clear() {
        getCache().clear();
    }

    private <K, V> List<Pair<K, V>> pairsFromKeysAndValues(List<K> keys, List<V> values) {


        if (values.size() > 0 && values.get(0) instanceof ProjectSearchDownloadOrthologDto) {
            List<ProjectSearchDownloadOrthologDto> orthologs = new ArrayList<>();
            for (V value : values) {
                orthologs.add((ProjectSearchDownloadOrthologDto) value);
            }

            List<String> mgis = new ArrayList<>();
            for (K key : keys) {
                mgis.add((String) key);
            }

            List<String> dublicateMgis = findDuplicates(mgis);
            keys.removeAll(dublicateMgis);

            List<String> missingMgis = missingMgis(mgis, orthologs);
            keys.removeAll(missingMgis);




            values.addAll((Collection<? extends V>) fillEmptyOrthologs(missingMgis));
            keys.addAll((Collection<? extends K>) missingMgis);
            keys.addAll((Collection<? extends K>) dublicateMgis);

        }

        int keysSize = keys.size();
        Assert.isTrue(keysSize == values.size(),
            String.format("The number of values [%1$d] must match the number of keys [%2$d]",
                values.size(), keysSize));

        return IntStream.range(0, keysSize)
            .mapToObj(index -> Pair.of(keys.get(index), values.get(index)))
            .collect(Collectors.toList());

    }

    private List<String> missingMgis(List<String> mgiIds,
                                     List<ProjectSearchDownloadOrthologDto> projectSearchDownloadOrthologDtos) {
        List<String> missingMgis = new ArrayList<>();
        mgiIds.forEach(mgiId -> {
            if (!isMissingOrtholog(projectSearchDownloadOrthologDtos, mgiId)) {
                missingMgis.add(mgiId);
            }
        });
        return missingMgis;
    }

    private boolean isMissingOrtholog(
        List<ProjectSearchDownloadOrthologDto> projectSearchDownloadOrthologDto, String mgiId) {

        return projectSearchDownloadOrthologDto.stream()
            .anyMatch(orthologDto -> mgiId.equals(orthologDto.getMgiGeneAccId()));

    }


    private List<ProjectSearchDownloadOrthologDto> fillEmptyOrthologs(List<String> missingMgiId) {
        List<ProjectSearchDownloadOrthologDto> ortholog = new ArrayList<>();
        missingMgiId.forEach(m -> {
            ProjectSearchDownloadOrthologDto orthologDto = new ProjectSearchDownloadOrthologDto();
            orthologDto.setHumanGeneSymbol("");
            orthologDto.setMgiGeneAccId(m);
            ortholog.add(orthologDto);
        });
        return ortholog;
    }


    public static List<String> findDuplicates(List<String> mgis) {
        List<String> duplicates = new ArrayList<>();
        Set<String> seenStrings = new HashSet<>();

        for (String mgi : mgis) {
            if (!seenStrings.add(mgi)) {
                duplicates.add(mgi);
            }
        }

        return duplicates;
    }

}