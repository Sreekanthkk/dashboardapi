package com.homemanagment.dashboardapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "homes")
@Data
@NoArgsConstructor
public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbId;

    private String id;
    private String formattedAddress;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String stateFips;
    private String zipCode;
    private String county;
    private String countyFips;
    private Double latitude;
    private Double longitude;
    private String propertyType;
    private Integer bedrooms;
    private Double bathrooms;
    private Integer squareFootage;
    private Integer lotSize;
    private Integer yearBuilt;
    private String assessorID;

    @Column(length = 1000)
    private String legalDescription;

    private String subdivision;
    private String lastSaleDate;
    private Boolean ownerOccupied;

    @Embedded
    private Features features;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "ownerName", column = @Column(name = "owner_name"))
    })
    private Owner owner;
}
