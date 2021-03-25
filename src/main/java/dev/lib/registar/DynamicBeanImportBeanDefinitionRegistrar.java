package dev.lib.registar;

import dev.lib.annotation.DynamicBean;
import dev.lib.annotation.enable.EnableDynamicBeans;
import dev.lib.factorybean.DynamicBeanFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.*;

@Component
public class DynamicBeanImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware, ResourceLoaderAware, EnvironmentAware {

    private static final String BEAN_SUFFIX = "DynamicBean";
    private static final String TYPE = "type";
    private static final String VALUE = "value";
    private static final String HANDLER = "handler";

    private Environment environment;
    private BeanFactory beanFactory;
    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void registerBeanDefinitions(
            AnnotationMetadata metadata,
            BeanDefinitionRegistry registry
    ) {
        getBeanCandidates(metadata).stream()
                .filter(candidate -> (candidate instanceof AnnotatedBeanDefinition))
                .map(AnnotatedBeanDefinition.class::cast)
                .forEach(beanDefinition -> registerFeignClient(registry, beanDefinition));
    }

    private LinkedHashSet<BeanDefinition> getBeanCandidates(
            AnnotationMetadata metadata
    ) {
        final ClassPathScanningCandidateComponentProvider scanner = getClassPathScanningCandidateComponentProvider();
        final LinkedHashSet<BeanDefinition> candidates = new LinkedHashSet<>();
        for (String packages : getBasePackages(metadata)) candidates.addAll(scanner.findCandidateComponents(packages));
        return candidates;
    }

    private ClassPathScanningCandidateComponentProvider getClassPathScanningCandidateComponentProvider() {
        final ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.addIncludeFilter(new AnnotationTypeFilter(DynamicBean.class));
        scanner.setResourceLoader(this.resourceLoader);
        return scanner;
    }

    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition
            ) {
                return beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation();
            }
        };
    }

    private Set<String> getBasePackages(
            AnnotationMetadata metadata
    ) {
        final Set<String> packages = new HashSet<>();

        final Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableDynamicBeans.class.getCanonicalName());

        Optional.ofNullable(attributes).map(var -> var.get("basePackageClasses")).map(Class[].class::cast)
                .ifPresent(classes -> {
                    for (Class<?> clazz : classes) packages.add(ClassUtils.getPackageName(clazz));
                });

        if (packages.isEmpty()) packages.add(ClassUtils.getPackageName(metadata.getClassName()));
        return packages;
    }

    private void registerFeignClient(
            BeanDefinitionRegistry registry,
            AnnotatedBeanDefinition annotatedBeanDefinition
    ) {
        final AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
        final Map<String, Object> attributes = Objects.requireNonNull(annotationMetadata.getAnnotationAttributes(DynamicBean.class.getCanonicalName()));

        // register amqp-producer bean definition
        registerAmqpProducer(registry, annotationMetadata, attributes);
    }

    private void registerAmqpProducer(
            BeanDefinitionRegistry registry,
            AnnotationMetadata annotationMetadata,
            Map<String, Object> attributes
    ) {
        // bean-definition-builder
        final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DynamicBeanFactoryBean.class);
        beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        beanDefinitionBuilder.addPropertyValue(TYPE, annotationMetadata.getClassName());
        beanDefinitionBuilder.addPropertyValue(VALUE, attributes.get(VALUE));
        beanDefinitionBuilder.addPropertyValue(HANDLER, beanFactory.getBean((Class<?>) attributes.get(HANDLER)));

        // register bean-definition
        BeanDefinitionReaderUtils
                .registerBeanDefinition(
                        new BeanDefinitionHolder(
                                beanDefinitionBuilder.getBeanDefinition(),
                                String.format("%s%s", annotationMetadata.getClassName(), BEAN_SUFFIX)
                        ),
                        registry
                );
    }


}
