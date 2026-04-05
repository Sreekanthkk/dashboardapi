package com.homemanagment.dashboardapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Owner {

    @Column(name = "owner_name")
    private String ownerName;
}
