package liquibase.resource;

import liquibase.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * An implementation of liquibase.FileOpener that opens file from the class loader.
 *
 * @see ResourceAccessor
 */
public class ClassLoaderResourceAccessor implements ResourceAccessor {
    private ClassLoader classLoader;
    private final String base;

    public ClassLoaderResourceAccessor(ClassLoader classLoader) {
        this.classLoader = classLoader;
        base = null;
    }

    public ClassLoaderResourceAccessor(String base, ClassLoader classLoader) {
        this.classLoader = classLoader;
        final int endIndex = base.lastIndexOf('/');
        this.base = endIndex < 0 ? base : base.substring(0, 1 + endIndex);
    }

    public InputStream getResourceAsStream(String resource) throws IOException {
        if (base != null && resource.startsWith(base)) {
            final String file = resource.substring(base.length());
            if ("http:/www.liquibase.org/xml/ns/dbchangelog/dbchangelog-2.0.xsd".equals(file)) {
                final ClassLoader loader = getClass().getClassLoader();
                InputStream resourceAsStream = loader.getResourceAsStream("liquibase/parser/core/xml/dbchangelog-2.0.xsd");
                if (resourceAsStream == null) {
                    throw new IllegalStateException("Loader " + loader);
                }
                return resourceAsStream;
            }
        }
        return classLoader.getResourceAsStream(resource);
    }

    public Enumeration<URL> getResources(String packageName) throws IOException {
        return classLoader.getResources(packageName);
    }

    public ClassLoader toClassLoader() {
        return getClass().getClassLoader();
    }

    @Override
    public String toString() {
        String description;
        if (classLoader instanceof URLClassLoader) {
            List<String> urls = new ArrayList<String>();
            for (URL url : ((URLClassLoader) classLoader).getURLs()) {
                urls.add(url.toExternalForm());
            }
            description = StringUtils.join(urls, ",");
        } else {
            description = classLoader.getClass().getName();
        }
        return getClass().getName() + "(" + description + ")";

    }
}
