package org.jboss.weld.tests.xml.namespaces;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.tests.xml.namespaces.excluded.FooExcluded;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@RunWith(Arquillian.class)
@ServerSetup(EnablePropertyReplacement.class)
public class BeansXmlPropertyReplacementText {

    @Inject
    private Instance<Object> instance;

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(BeansXmlTest.class.getPackage())
                .addPackage(FooExcluded.class.getPackage())
                .addAsManifestResource(
                        new StringAsset("<beans\n" +
                                "        xmlns=\"http://java.sun.com/xml/ns/javaee\" \n" +
                                "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
                                "        xmlns:weld=\"http://jboss.org/schema/weld/beans\">\n" +
                                "    <weld:scan>\n" +
                                "    <weld:exclude name=\"org.jboss.weld.tests.xml.namespaces.excluded.FooExcluded\"/>\n" +
                                "    </weld:scan>\n" +
                                "    <alternatives>\n" +
                                "        <class>\n" +
                                "            ${test:org.jboss.weld.tests.xml.namespaces.SomeAlternatives}\n"+
                                "        </class>\n" +
                                "    </alternatives>\n" +
                                "</beans>"),
                        "beans.xml");
    }

    @Test
    public void test() {
        // verify scan exclusion works
        Assert.assertFalse(instance.select(FooExcluded.class).isResolvable());
        // verify alternative is enabled
        Assert.assertTrue(instance.select(SomeAlternative.class).isResolvable());
    }
}


