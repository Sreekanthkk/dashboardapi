package com.homemanagment.dashboardapi.repository;

import com.homemanagment.dashboardapi.model.ArchitectureTypeCount;
import com.homemanagment.dashboardapi.model.Features;
import com.homemanagment.dashboardapi.model.Home;
import com.homemanagment.dashboardapi.model.Owner;
import com.homemanagment.dashboardapi.model.StateCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class HomeRepositoryTest {

    @Autowired
    private HomeRepository homeRepository;

    @BeforeEach
    void setUp() {
        homeRepository.deleteAll();

        homeRepository.saveAll(List.of(
                buildHome("1", "Ranch",   true,  true,  true,  "TX", 1800, "John Smith"),
                buildHome("2", "Ranch",   true,  false, true,  "TX", 1950, "Jane Doe"),
                buildHome("3", "Colonial",false, true,  true,  "CA", 2200, "Bob Johnson"),
                buildHome("4", "Colonial",false, false, false, "CA", 1600, "Alice Brown"),
                buildHome("5", "Tudor",   true,  true,  true,  "NY", 2500, "Charlie Wilson")
        ));
    }

    // ── count ────────────────────────────────────────────────────────────────

    @Test
    void count_returnsAllHomes() {
        assertThat(homeRepository.count()).isEqualTo(5);
    }

    // ── countByArchitectureType ───────────────────────────────────────────────

    @Test
    void countByArchitectureType_groupsCorrectly() {
        List<ArchitectureTypeCount> results = homeRepository.countByArchitectureType();

        assertThat(results).hasSize(3); // Ranch, Colonial, Tudor

        ArchitectureTypeCount ranch = results.stream()
                .filter(r -> "Ranch".equals(r.architectureType()))
                .findFirst()
                .orElseThrow();
        assertThat(ranch.count()).isEqualTo(2);

        ArchitectureTypeCount colonial = results.stream()
                .filter(r -> "Colonial".equals(r.architectureType()))
                .findFirst()
                .orElseThrow();
        assertThat(colonial.count()).isEqualTo(2);

        ArchitectureTypeCount tudor = results.stream()
                .filter(r -> "Tudor".equals(r.architectureType()))
                .findFirst()
                .orElseThrow();
        assertThat(tudor.count()).isEqualTo(1);
    }

    // ── countByState ─────────────────────────────────────────────────────────

    @Test
    void countByState_groupsCorrectly() {
        List<StateCount> results = homeRepository.countByState();

        assertThat(results).hasSize(3); // TX, CA, NY

        StateCount tx = results.stream()
                .filter(r -> "TX".equals(r.state()))
                .findFirst()
                .orElseThrow();
        assertThat(tx.count()).isEqualTo(2);

        StateCount ca = results.stream()
                .filter(r -> "CA".equals(r.state()))
                .findFirst()
                .orElseThrow();
        assertThat(ca.count()).isEqualTo(2);

        StateCount ny = results.stream()
                .filter(r -> "NY".equals(r.state()))
                .findFirst()
                .orElseThrow();
        assertThat(ny.count()).isEqualTo(1);
    }

    // ── countWithGarage ───────────────────────────────────────────────────────

    @Test
    void countWithGarage_countsOnlyHomesWithGarage() {
        // 3 homes have garage = true (ids 1, 2, 5)
        assertThat(homeRepository.countWithGarage()).isEqualTo(3);
    }

    // ── countWithCooling ──────────────────────────────────────────────────────

    @Test
    void countWithCooling_countsOnlyHomesWithCooling() {
        // 3 homes have cooling = true (ids 1, 3, 5)
        assertThat(homeRepository.countWithCooling()).isEqualTo(3);
    }

    // ── countWithHeating ──────────────────────────────────────────────────────

    @Test
    void countWithHeating_countsOnlyHomesWithHeating() {
        // 4 homes have heating = true (ids 1, 2, 3, 5)
        assertThat(homeRepository.countWithHeating()).isEqualTo(4);
    }

    // ── avgSquareFootage ──────────────────────────────────────────────────────

    @Test
    void avgSquareFootage_calculatesCorrectly() {
        // (1800 + 1950 + 2200 + 1600 + 2500) / 5 = 2010
        Double avg = homeRepository.avgSquareFootage();
        assertThat(avg).isEqualTo(2010.0);
    }

    // ── avgYearBuilt ──────────────────────────────────────────────────────────

    @Test
    void avgYearBuilt_calculatesCorrectly() {
        Double avg = homeRepository.avgYearBuilt();
        assertThat(avg).isNotNull().isBetween(1800.0, 2100.0);
    }

    @Test
    void avgYearBuilt_excludesNullYears() {
        Home noYear = buildHome("6", "Modern", false, false, false, "FL", null, "No Year Owner");
        homeRepository.save(noYear);

        Double avg = homeRepository.avgYearBuilt();
        assertThat(avg).isNotNull();
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private Home buildHome(String id, String archType,
                           boolean garage, boolean cooling, boolean heating,
                           String state, Integer squareFootage, String ownerName) {
        Features features = new Features();
        features.setArchitectureType(archType);
        features.setGarage(garage);
        features.setCooling(cooling);
        features.setHeating(heating);

        Owner owner = new Owner();
        owner.setOwnerName(ownerName);

        Home home = new Home();
        home.setId(id);
        home.setState(state);
        home.setSquareFootage(squareFootage);
        home.setYearBuilt(squareFootage != null ? 1990 : null);
        home.setFeatures(features);
        home.setOwner(owner);
        return home;
    }
}
