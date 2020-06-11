package org.jboss.weld.tests.xml.namespaces;

import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.helpers.domain.DomainClient;
import org.jboss.as.controller.client.helpers.domain.impl.DomainClientImpl;
import org.jboss.dmr.ModelNode;

import java.net.InetAddress;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

public class EnablePropertyReplacement implements ServerSetupTask {


    @Override
    public void setup(ManagementClient managementClient, String s) throws Exception {

        ModelNode stepSpecDescriptor = createNode("/subsystem=ee", WRITE_ATTRIBUTE_OPERATION);
        stepSpecDescriptor.get("spec-descriptor-property-replacement").set("true");
        InetAddress address = InetAddress.getByName("localhost");
        DomainClient client = new DomainClientImpl(address, 8080);
        client.execute(stepSpecDescriptor);

    }

    private ModelNode createNode(String address, String operation) {
        ModelNode op = new ModelNode();
        ModelNode list = op.get("address").setEmptyList();
        if (address != null) {
            String[] pathSegments = address.split("/");
            for (String segment : pathSegments) {
                String[] elements = segment.split("=");
                list.add(elements[0], elements[1]);
            }
        }
        op.get("operation").set(operation);
        return op;
    }

    @Override
    public void tearDown(ManagementClient managementClient, String s) throws Exception {

    }
}
