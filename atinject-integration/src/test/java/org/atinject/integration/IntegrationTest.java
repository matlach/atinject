package org.atinject.integration;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public abstract class IntegrationTest
{
    Logger logger = LoggerFactory.getLogger(IntegrationTest.class);
    
    @Rule public TestRule watchman = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            logger.info("starting {}", description.getMethodName());
        }

        @Override
        protected void finished(Description description) {
            logger.info("finished {}", description.getMethodName());
        }
    };
    
    public static JavaArchive createArchive(Class<? extends IntegrationTest> testClass) {
        return ShrinkWrap.create(JavaArchive.class, testClass.getSimpleName() + ".jar") ;
    }
    
    public static void addAtinjectCore(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.core");
    }
    
    public static void addAtinjectApi(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api");
    }
    
    public static void addAuthentication(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.authentication");
    }
    
    public static void addAuthorization(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.authorization");
    }
    
    public static void addFacebook(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.facebook");
    }
    
    public static void addPermission(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.permission");
    }
    
    public static void addRegistration(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.registration");
    }
    
    public static void addRole(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.role");
    }
    
    public static void addRolePermission(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.rolepermission");
    }
    
    public static void addUser(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.user");
    }
    
    public static void addUserAffinity(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.useraffinity");
    }
    
    public static void addUserCredential(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.usercredential");
    }
    
    public static void addUserLockout(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.userlockout");
    }
    
    public static void addUserPermission(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.userpermission");
    }
    
    public static void addUserPreference(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.userpreference");
    }
    
    public static void addUserRole(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.userrole");
    }
    
    public static void addUserSession(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.usersession");
    }
    
    public static void addUserTopology(JavaArchive archive) {
        archive.addPackages(true, "org.atinject.api.usertopology");
    }
    
}
