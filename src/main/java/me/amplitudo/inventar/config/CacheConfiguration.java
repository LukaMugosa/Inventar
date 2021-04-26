package me.amplitudo.inventar.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, me.amplitudo.inventar.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, me.amplitudo.inventar.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, me.amplitudo.inventar.domain.User.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Authority.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.User.class.getName() + ".authorities");
            createCache(cm, me.amplitudo.inventar.domain.Tenant.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Tenant.class.getName() + ".employees");
            createCache(cm, me.amplitudo.inventar.domain.Supplier.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Supplier.class.getName() + ".equipments");
            createCache(cm, me.amplitudo.inventar.domain.Repairer.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Repairer.class.getName() + ".equipmentServices");
            createCache(cm, me.amplitudo.inventar.domain.Employee.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Employee.class.getName() + ".equipmentRequests");
            createCache(cm, me.amplitudo.inventar.domain.Employee.class.getName() + ".problemReports");
            createCache(cm, me.amplitudo.inventar.domain.Employee.class.getName() + ".equipmentServices");
            createCache(cm, me.amplitudo.inventar.domain.Employee.class.getName() + ".equipmentEmployees");
            createCache(cm, me.amplitudo.inventar.domain.Employee.class.getName() + ".createdNotifications");
            createCache(cm, me.amplitudo.inventar.domain.Employee.class.getName() + ".receivedNotifications");
            createCache(cm, me.amplitudo.inventar.domain.Sector.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Sector.class.getName() + ".positions");
            createCache(cm, me.amplitudo.inventar.domain.Position.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Position.class.getName() + ".employees");
            createCache(cm, me.amplitudo.inventar.domain.Manufacturer.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Manufacturer.class.getName() + ".equipments");
            createCache(cm, me.amplitudo.inventar.domain.EquipmentCategory.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.EquipmentCategory.class.getName() + ".equipments");
            createCache(cm, me.amplitudo.inventar.domain.Equipment.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Equipment.class.getName() + ".equipmentRequests");
            createCache(cm, me.amplitudo.inventar.domain.Equipment.class.getName() + ".equipmentServices");
            createCache(cm, me.amplitudo.inventar.domain.Equipment.class.getName() + ".equipmentEmployees");
            createCache(cm, me.amplitudo.inventar.domain.Equipment.class.getName() + ".images");
            createCache(cm, me.amplitudo.inventar.domain.EquipmentEmployee.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.ProblemReportCategory.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.ProblemReportCategory.class.getName() + ".problems");
            createCache(cm, me.amplitudo.inventar.domain.ProblemReport.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.EquipmentRequest.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.EquipmentService.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Notification.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.EquipmentImage.class.getName());
            createCache(cm, me.amplitudo.inventar.domain.Repairer.class.getName() + ".equipmentServicings");
            createCache(cm, me.amplitudo.inventar.domain.Employee.class.getName() + ".equipmentServicings");
            createCache(cm, me.amplitudo.inventar.domain.Equipment.class.getName() + ".equipmentServicings");
            createCache(cm, me.amplitudo.inventar.domain.EquipmentServicing.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
