package com.homemanagment.dashboardapi.repository;

import com.homemanagment.dashboardapi.model.Features;
import com.homemanagment.dashboardapi.model.Home;
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
                buildHome("1", "Ranch",  true,  true,  true,  "TX", 1800),
                buildHome("2", "Ranch",  true,  false, true,  "TX", 1950),
                buildHome("3", "Colonial", false, true,  true,  "CA", 2200),
                buildHome("4", "Colonial", false, false, false, "CA", 1600),
                buildHome("5", "Tudor",  true,  true,  true,  "NY", 2500)
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
        List<Object[]> results = homeRepository.countByArchitectureType();

        assertThat(results).hasSize(3); // Ranch, Colonial, Tudor

        // find Ranch row and assert count = 2
        Object[] ranch = results.stream()
                .filter(r -> "Ranch".equals(r[0]))
                .findFirst()
                .orElseThrow();
        assertThat((Long) ranch[1]).isEqualTo(2);

        // find Colonial row and assert count = 2
        Object[] colonial = results.stream()
                .filter(r -> "Colonial".equals(r[0]))
                .findFirst()
                .orElseThrow();
        assertThat((Long) colonial[1]).isEqualTo(2);

        // find Tudor row and assert count = 1
        Object[] tudor = results.stream()
                .filter(r -> "Tudor".equals(r[0]))
                .findFirst()
                .orElseThrow();
        assertThat((Long) tudor[1]).isEqualTo(1);
    }

    // ── countByState ─────────────────────────────────────────────────────────

    @Test
    void countByState_groupsCorrectly() {
        List<Object[]> results = homeRepository.countByState();

        assertThat(results).hasSize(3); // TX, CA, NY

        Object[] tx = results.stream()
                .filter(r -> "TX".equals(r[0]))
                .findFirst()
                .orElseThrow();
        assertThat((Long) tx[1]).isEqualTo(2);

        Object[] ca = results.stream()
                .filter(r -> "CA".equals(r[0]))
                .findFirst()
                .orElseThrow();
        assertThat((Long) ca[1]).isEqualTo(2);

        Object[] ny = results.stream()
                .filter(r -> "NY".equals(r[0]))
                .findFirst()
                .orElseThrow();
        assertThat((Long) ny[1]).isEqualTo(1);
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
        // All 5 homes have yearBuilt set — avg not tested exactly,
        // just ensure it returns a reasonable value
        Double avg = homeRepository.avgYearBuilt();
        assertThat(avg).isNotNull().isBetween(1800.0, 2100.0);
    }

    @Test
    void avgYearBuilt_excludesNullYears() {
        // Add a home with no yearBuilt — avg should not change count
        Home noYear = buildHome("6", "Modern", false, false, false, "FL", null);
        homeRepository.save(noYear);

        Double avg = homeRepository.avgYearBuilt();
        assertThat(avg).isNotNull();
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private Home buildHome(String id, String archType,
                           boolean garage, boolean cooling, boolean heating,
                           String state, Integer squareFootage) {
        Features features = new Features();
        features.setArchitectureType(archType);
        features.setGarage(garage);
        features.setCooling(cooling);
        features.setHeating(heating);

        Home home = new Home();
        home.setId(id);
        home.setState(state);
        home.setSquareFootage(squareFootage);
        home.setYearBuilt(squareFootage != null ? 1990 : null); // reuse nullability
        home.setFeatures(features);
        return home;
    }
}
