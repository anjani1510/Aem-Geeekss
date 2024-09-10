package com.aem.geeks.core.workflows;

import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.exec.WorkItem;
import org.osgi.service.component.annotations.Component;

@Component(
    service = ParticipantStepChooser.class,
    property = {
        "service.description=Simple Dynamic Participant Chooser",
        "chooser.label=Simple Dynamic Participant Chooser"
    }
)
public class Task13 implements ParticipantStepChooser {

    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession wfSession, MetaDataMap metaDataMap) {
        // Custom logic to determine the participant
        String pagePath = workItem.getWorkflowData().getPayload().toString();
        
        // Example: Assign the task to a group based on the page path
        if (pagePath.startsWith("/content/aemgeeks")) {
            return "aemgeeks";
        } else if (pagePath.startsWith("/content/project2")) {
            return "project2";
        }

        // Default participant
        return "default-user";
    }
}
