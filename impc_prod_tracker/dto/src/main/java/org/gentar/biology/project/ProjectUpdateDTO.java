package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

/**
 * Class to keep the information the user can send in order to update a project.
 * As in this case the only editable information is the ProjectCommonDataDTO object, for now this
 * class does not add more information.
 */
@Data
public class ProjectUpdateDTO
{
    // Public identifier of the project. No editable.
    private String tpn;
    private String completionNote;
    private String completionComment;

    @JsonUnwrapped
    private ProjectCommonDataDTO projectCommonDataDTO;
}
