package com.homemanagment.dashboardapi.dto;

import com.homemanagment.dashboardapi.model.ArchitectureTypeCount;
import com.homemanagment.dashboardapi.model.StateCount;

import java.util.List;

public record DashboardStatsDto(

        // Total homes registered
        long totalHomes,

        // Number of distinct architecture types
        int totalArchitectureTypes,

        // Number of distinct states
        int totalStates,

        // Homes count broken down by architecture type
        List<ArchitectureTypeCount> countByArchitectureType,

        // Homes count broken down by state
        List<StateCount> countByState,

        // Feature stats
        long homesWithGarage,
        long homesWithCooling,
        long homesWithHeating,

        // Averages
        Double avgSquareFootage,
        Integer avgYearBuilt
) {}
