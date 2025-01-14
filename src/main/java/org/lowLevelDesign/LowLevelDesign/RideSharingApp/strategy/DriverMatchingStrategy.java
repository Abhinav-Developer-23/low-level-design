package org.lowLevelDesign.LowLevelDesign.RideSharingApp.strategy;


import org.lowLevelDesign.LowLevelDesign.RideSharingApp.model.Driver;
import org.lowLevelDesign.LowLevelDesign.RideSharingApp.model.Rider;

import java.util.List;
import java.util.Optional;

public interface DriverMatchingStrategy {

    Optional<Driver> findDriver(Rider rider, List<Driver> nearByDrivers, int origin, int destination);
}
