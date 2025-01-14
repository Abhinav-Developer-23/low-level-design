package org.lowLevelDesign.LowLevelDesign.CarRentalSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// Insurance type enum
enum InsuranceType {
    BASIC,             // Basic liability coverage
    COMPREHENSIVE,     // Full coverage including collision
    THIRD_PARTY,      // Third-party liability only
    PREMIUM,          // Premium coverage with additional benefits
    NONE              // No insurance coverage
}

// Insurance status enum
enum InsuranceStatus {
    ACTIVE,
    EXPIRED,
    CANCELLED,
    PENDING,
    CLAIMED
}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalInsurance {
    private String insuranceId;
    private String policyNumber;
    private InsuranceType type;
    private double coverageAmount;
    private double deductible;
    private Date startDate;
    private Date endDate;
    private double dailyRate;

    // Coverage details
    private boolean personalInjuryCoverage;
    private boolean propertyDamageCoverage;
    private boolean theftCoverage;
    private boolean collisionDamageCoverage;
    private boolean naturalDisasterCoverage;

    // Insurance provider details
    private String providerName;
    private String providerContact;
    private String policyDocumentUrl;

    // Status of the insurance
    private InsuranceStatus status;
} 