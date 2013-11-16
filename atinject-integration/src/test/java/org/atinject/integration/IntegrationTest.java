package org.atinject.integration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public abstract class IntegrationTest {
    
    private Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    private static Map<String, Integer> inSequenceCounter = new HashMap<>();
    
    @Rule public TestRule watchman = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            logger.info("starting {}", description.getMethodName());
            
            InSequence inSequence = description.getAnnotation(InSequence.class);
            if (inSequence != null) {
                int expectedSequenceValue = inSequence.value();
                if (! inSequenceCounter.containsKey(description.getClassName())) {
                    inSequenceCounter.put(description.getClassName(), Integer.valueOf(1));
                }
                int actualSequenceValue = inSequenceCounter.get(description.getClassName()).intValue();
                Assume.assumeTrue(actualSequenceValue == expectedSequenceValue);
            }
        }

        @Override
        protected void finished(Description description) {
            InSequence inSequence = description.getAnnotation(InSequence.class);
            if (inSequence != null) {
                int actualSequenceValue = inSequenceCounter.get(description.getClassName()).intValue();
                int nextSequenceValue = actualSequenceValue + 1;
                inSequenceCounter.put(description.getClassName(), Integer.valueOf(nextSequenceValue));
            }
            
            logger.info("finished {}", description.getMethodName());
        }
    };
    
    public static final String framework = "org.atinject";
    public static final String core = "org.atinject.core";
    public static final String api = "org.atinject.api";
    public static final String api_analytic = "org.atinject.api.analytic";
    public static final String api_authentication = "org.atinject.api.authentication";
    public static final String api_authorization = "org.atinject.api.authorization";
    public static final String api_facebook = "org.atinject.api.facebook";
    public static final String api_permission = "org.atinject.api.permission";
    public static final String api_registration = "org.atinject.api.registration";
    public static final String api_role = "org.atinject.api.role";
    public static final String api_rolepermission = "org.atinject.api.rolepermission";
    public static final String api_systemproperty = "org.atinject.api.systemproperty";
    public static final String api_user = "org.atinject.api.user";
    public static final String api_user_affinity = "org.atinject.api.useraffinity";
    public static final String api_user_credential = "org.atinject.api.usercredential";
    public static final String api_user_lockout = "org.atinject.api.userlockout";
    public static final String api_user_permission = "org.atinject.api.userpermission";
    public static final String api_user_preference = "org.atinject.api.userpreference";
    public static final String api_user_role = "org.atinject.api.userrole";
    public static final String api_user_session = "org.atinject.api.usersession";
    public static final String api_user_topology = "org.atinject.api.usertopology";
    public static final String api_websocket = "org.atinject.api.websocket";
    
    static Map<String, List<String>> dependencies;
    static {
        dependencies = new HashMap<>();
        dependencies.put(core, Collections.<String>emptyList());
        dependencies.put(api, Arrays.asList(core));
        dependencies.put(api_analytic, Arrays.asList(core));
        dependencies.put(api_authentication, Arrays.asList(core));
        dependencies.put(api_authorization, Arrays.asList(core));
        dependencies.put(api_facebook, Arrays.asList(core));
        dependencies.put(api_permission, Arrays.asList(core));
        dependencies.put(api_registration, Arrays.asList(core));
        dependencies.put(api_role, Arrays.asList(core));
        dependencies.put(api_rolepermission, Arrays.asList(api_role, api_permission));
        dependencies.put(api_systemproperty, Arrays.asList(core));
        dependencies.put(api_user, Arrays.asList(core));
        dependencies.put(api_user_affinity, Arrays.asList(api_user));
        dependencies.put(api_user_credential, Arrays.asList(api_user));
        dependencies.put(api_user_lockout, Arrays.asList(api_user));
        dependencies.put(api_user_permission, Arrays.asList(api_user, api_permission));
        dependencies.put(api_user_preference, Arrays.asList(api_user));
        dependencies.put(api_user_role, Arrays.asList(api_user, api_role));
        dependencies.put(api_user_session, Arrays.asList(api_user));
        dependencies.put(api_user_topology, Arrays.asList(api_user));
        dependencies.put(api_websocket, Arrays.asList(api_user));
    }

    public static JavaArchive createArchive(Class<? extends IntegrationTest> integrationTestClass) {
        return ShrinkWrap.create(JavaArchive.class, integrationTestClass.getSimpleName() + ".jar") ;
    }
    
    public static void addPackageAndItIsDependencies(JavaArchive archive, String pack) {
        addPackage(archive, pack);
        for (String dependency : dependencies.get(pack)) {
            addPackageAndItIsDependencies(archive, dependency);
        } 
    }
    
    public static void addPackage(JavaArchive archive, String pack) {
        archive.addPackages(true, pack);
    }
    
}
