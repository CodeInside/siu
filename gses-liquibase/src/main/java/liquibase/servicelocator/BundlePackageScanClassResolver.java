package liquibase.servicelocator;

import org.osgi.framework.Bundle;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

final public class BundlePackageScanClassResolver implements PackageScanClassResolver {

    final private Set<ClassLoader> classLoaders = new LinkedHashSet<ClassLoader>();
    final private Bundle bundle;

    public BundlePackageScanClassResolver(final Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void setClassLoaders(Set<ClassLoader> classLoaders) {
        this.classLoaders.clear();
        classLoaders.addAll(classLoaders);
    }

    @Override
    public Set<ClassLoader> getClassLoaders() {
        return classLoaders;
    }

    @Override
    public void addClassLoader(ClassLoader classLoader) {
        classLoaders.add(classLoader);
    }

    @Override
    public Set<Class<?>> findImplementations(final Class<?> parent, final String... packageNames) {
        final Set<Class<?>> classSet = new LinkedHashSet<Class<?>>();
        for (final String packageName : packageNames) {
            final Enumeration entries = bundle.findEntries("/" + packageName.replace('.', '/'), "*.class", true);
            while (entries.hasMoreElements()) {
                final URL url = (URL) entries.nextElement();
                final String path = url.getPath();
                final int index = path.indexOf("/");
                final int endIndex = path.length() - 6; //Strip ".class" substring
                final String className = path.substring(index + 1, endIndex).replace('/', '.');
                try {
                    final Class clazz = bundle.loadClass(className);
                    int modifiers = clazz.getModifiers();
                    if (!Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                        if (parent.isAssignableFrom(clazz)) {
                            classSet.add(clazz);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    //
                }
            }
        }
        return classSet;
    }
}
