package org.gentar.security.abac;

import java.util.List;
import java.util.stream.Collectors;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.springframework.stereotype.Component;

@Component
public class ResourceAccessCheckerImpl<T> implements ResourceAccessChecker<T> {
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public ResourceAccessCheckerImpl(ContextAwarePolicyEnforcement policyEnforcement) {
        this.policyEnforcement = policyEnforcement;
    }

    @Override
    public Resource<T> checkAccess(Resource<T> resource, String action) {
        if (resource == null) {
            return null;
        }

        Resource<T> resourceResult = resource;

        if (policyEnforcement.isUserAnonymous()) {
            if (!resource.getResourcePrivacy().equals(ResourcePrivacy.PUBLIC)) {
                resourceResult = null;
            }
        } else {
            if (policyEnforcement.hasPermission(resource, action)) {
                resourceResult = resource;
            } else {
                resourceResult = resource.getRestrictedObject();
            }
        }

        return resourceResult;
    }

    @Override
    public List<String> getUserAccessLevel(List<String> privacyNames) {

        if (policyEnforcement.isUserAnonymous()) {
            if (privacyNames == null) {
                return List.of("public");
            } else {
                return privacyNames.stream().filter(p -> p.equals("public"))
                    .collect(
                        Collectors.toList());
            }

        } else {
            if (!policyEnforcement
                .hasPermission(getParameterRestrictedObjects(), Actions.READ_PROJECT_ACTION)) {
                if (privacyNames == null) {
                    return List.of("public","protected");
                } else {
                    return privacyNames.stream()
                        .filter(p -> p.equals("public") || p.equals("protected")).collect(
                            Collectors.toList());
                }
            }
        }

        return privacyNames;
    }


    private Project getParameterRestrictedObjects() {
        Project project = new Project();
        Privacy privacy = new Privacy();
        privacy.setName("RESTRICTED");
        project.setPrivacy(privacy);
        return project;
    }

}
