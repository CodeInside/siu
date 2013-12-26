package liquibase.servicelocator;

import liquibase.exception.ServiceNotFoundException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.osgi.framework.Bundle;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ServiceLocator {

    private static ServiceLocator instance;

    public static void initialize(Bundle bundle) {
        instance = new ServiceLocator(bundle);
    }

    public static void clean() {
        instance = null;
    }


    private ResourceAccessor resourceAccessor;

    private Map<Class, List<Class>> classesBySuperclass;
    private List<String> packagesToScan;
    private Logger logger = Logger.getLogger(getClass().getName());
    private PackageScanClassResolver classResolver;

    private ServiceLocator(final Bundle bundle) {
        classResolver = defaultClassLoader(bundle);
        setResourceAccessor(new ClassLoaderResourceAccessor(getClass().getClassLoader()));
    }

    public static ServiceLocator getInstance() {
        return instance;
    }

    private PackageScanClassResolver defaultClassLoader(final Bundle bundle) {
        return bundle == null ? new DefaultPackageScanClassResolver() : new BundlePackageScanClassResolver(bundle);
    }

    public void setResourceAccessor(ResourceAccessor resourceAccessor) {
        this.resourceAccessor = resourceAccessor;
        this.classesBySuperclass = new HashMap<Class, List<Class>>();
        this.classResolver.setClassLoaders(new HashSet<ClassLoader>(Arrays.asList(new ClassLoader[]{resourceAccessor.toClassLoader()})));
        packagesToScan = new ArrayList<String>();
        addPackageToScan("liquibase.change");
        addPackageToScan("liquibase.database");
        addPackageToScan("liquibase.parser");
        addPackageToScan("liquibase.precondition");
        addPackageToScan("liquibase.serializer");
        addPackageToScan("liquibase.sqlgenerator");
        addPackageToScan("liquibase.executor");
        addPackageToScan("liquibase.snapshot");
    }

    public void addPackageToScan(String packageName) {
        packagesToScan.add(packageName);
    }

    public Class findClass(Class requiredInterface) throws ServiceNotFoundException {
        Class[] classes = findClasses(requiredInterface);
        if (PrioritizedService.class.isAssignableFrom(requiredInterface)) {
            PrioritizedService returnObject = null;
            for (Class clazz : classes) {
                PrioritizedService newInstance;
                try {
                    newInstance = (PrioritizedService) clazz.newInstance();
                } catch (Exception e) {
                    throw new UnexpectedLiquibaseException(e);
                }

                if (returnObject == null || newInstance.getPriority() > returnObject.getPriority()) {
                    returnObject = newInstance;
                }
            }

            if (returnObject == null) {
                throw new ServiceNotFoundException("Could not find implementation of " + requiredInterface.getName());
            }
            return returnObject.getClass();
        }

        if (classes.length != 1) {
            throw new ServiceNotFoundException("Could not find unique implementation of " + requiredInterface.getName() + ".  Found " + classes.length + " implementations");
        }

        return classes[0];
    }

    public Class[] findClasses(Class requiredInterface) throws ServiceNotFoundException {
        logger.fine("ServiceLocator.findClasses for " + requiredInterface.getName());
        try {
            Class.forName(requiredInterface.getName());
            if (!classesBySuperclass.containsKey(requiredInterface)) {
                classesBySuperclass.put(requiredInterface, findClassesImpl(requiredInterface));
            }
        } catch (Exception e) {
            throw new ServiceNotFoundException(e);
        }
        List<Class> classes = classesBySuperclass.get(requiredInterface);
        HashSet<Class> uniqueClasses = new HashSet<Class>(classes);
        return uniqueClasses.toArray(new Class[uniqueClasses.size()]);
    }

    public Object newInstance(Class requiredInterface) throws ServiceNotFoundException {
        try {
            return findClass(requiredInterface).newInstance();
        } catch (Exception e) {
            throw new ServiceNotFoundException(e);
        }
    }

    private List<Class> findClassesImpl(Class requiredInterface) throws Exception {
        logger.fine("ServiceLocator finding classes matching interface " + requiredInterface.getName());

        List<Class> classes = new ArrayList<Class>();

        classResolver.addClassLoader(resourceAccessor.toClassLoader());
        for (Class<?> clazz : classResolver.findImplementations(requiredInterface, packagesToScan.toArray(new String[packagesToScan.size()]))) {
            if (clazz.getAnnotation(LiquibaseService.class) != null && clazz.getAnnotation(LiquibaseService.class).skip()) {
                continue;
            }

            if (!Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers()) && Modifier.isPublic(clazz.getModifiers())) {
                try {
                    clazz.getConstructor();
                    logger.fine(clazz.getName() + " matches " + requiredInterface.getName());
                    classes.add(clazz);
                } catch (NoSuchMethodException e) {
                    logger.info("Can not use " + clazz + " as a Liquibase service because it does not have a no-argument constructor");
                }
            }
        }

        return classes;
    }

    protected Logger getLogger() {
        return logger;
    }
}