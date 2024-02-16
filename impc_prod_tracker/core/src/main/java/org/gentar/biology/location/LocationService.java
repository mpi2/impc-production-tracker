package org.gentar.biology.location;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class LocationService
{
    private static final String ERROR = "Location with id [%s] does not exist.";

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository)
    {
        this.locationRepository = locationRepository;
    }

    public Location getById(Long id)
    {
        return locationRepository.findFirstById(id);
    }

    public Location getByIdFailsIfNull(Long id)
    {
        Location location = getById(id);
        if (location == null)
        {
            throw new UserOperationFailedException(String.format(ERROR, id));
        }
        return location;
    }
}
