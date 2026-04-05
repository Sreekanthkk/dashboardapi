package com.homemanagment.dashboardapi.repository;

import com.homemanagment.dashboardapi.model.ArchitectureTypeCount;
import com.homemanagment.dashboardapi.model.Home;
import com.homemanagment.dashboardapi.model.StateCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {

    // Total count of registered homes
    long count();

    // Count of homes grouped by architecture type
    @Query("SELECT new com.homemanagment.dashboardapi.model.ArchitectureTypeCount(h.features.architectureType, COUNT(h)) FROM Home h GROUP BY h.features.architectureType")
    List<ArchitectureTypeCount> countByArchitectureType();

    // Count of homes grouped by state
    @Query("SELECT new com.homemanagment.dashboardapi.model.StateCount(h.state, COUNT(h)) FROM Home h GROUP BY h.state")
    List<StateCount> countByState();

    // Count of homes that have a garage
    @Query("SELECT COUNT(h) FROM Home h WHERE h.features.garage = true")
    long countWithGarage();

    // Count of homes that have cooling
    @Query("SELECT COUNT(h) FROM Home h WHERE h.features.cooling = true")
    long countWithCooling();

    // Count of homes that have heating
    @Query("SELECT COUNT(h) FROM Home h WHERE h.features.heating = true")
    long countWithHeating();

    // Average square footage
    @Query("SELECT AVG(h.squareFootage) FROM Home h WHERE h.squareFootage IS NOT NULL")
    Double avgSquareFootage();

    // Average year built
    @Query("SELECT AVG(h.yearBuilt) FROM Home h WHERE h.yearBuilt IS NOT NULL AND h.yearBuilt > 1800")
    Double avgYearBuilt();
}
