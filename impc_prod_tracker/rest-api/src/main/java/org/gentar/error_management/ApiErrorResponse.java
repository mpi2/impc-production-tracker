
package org.gentar.error_management;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse
{
    @JsonProperty("apierror")
    private ApiError apierror;
}
