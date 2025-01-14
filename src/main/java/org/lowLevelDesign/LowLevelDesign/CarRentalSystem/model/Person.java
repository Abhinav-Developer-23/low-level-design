package org.lowLevelDesign.LowLevelDesign.CarRentalSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
    private String email;
    private String phone;
    private Address address;
}
