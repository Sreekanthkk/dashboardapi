package com.homemanagment.dashboardapi.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Features {

    private String architectureType;
    private Boolean cooling;
    private String coolingType;
    private Integer floorCount;
    private String foundationType;
    private Boolean garage;
    private String garageType;
    private Boolean heating;
    private String heatingType;
}
