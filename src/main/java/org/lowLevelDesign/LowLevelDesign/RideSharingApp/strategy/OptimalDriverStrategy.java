package org.lowLevelDesign.LowLevelDesign.RideSharingApp.strategy;

import lombok.NonNull;
import org.lowLevelDesign.LowLevelDesign.RideSharingApp.model.Driver;
import org.lowLevelDesign.LowLevelDesign.RideSharingApp.model.Rider;

import java.util.List;
import java.util.Optional;

public class OptimalDriverStrategy implements DriverMatchingStrategy {

    @Override
    public Optional<Driver> findDriver(
            @NonNull final Rider rider,
            @NonNull final List<Driver> nearByDrivers,
            final int origin,
            final int destination) {
        return nearByDrivers.stream().findAny();
    }
}
