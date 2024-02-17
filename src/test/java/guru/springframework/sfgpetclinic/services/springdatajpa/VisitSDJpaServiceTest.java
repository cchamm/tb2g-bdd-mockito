package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService service;

    @DisplayName("Test Find All")
    @Test
    void findAll() {

        // Given
        Visit visit = new Visit();
        Set<Visit> visits = new HashSet<>();
        visits.add(visit);
        given(visitRepository.findAll()).willReturn(visits);

        //when
        Set<Visit> foundVisits = service.findAll();

        // then
        then(visitRepository).should().findAll();
        assertThat(foundVisits).hasSize(1);

    }

    @Test
    void findById() {
        // Given
        Visit visit = new Visit();
        given(visitRepository.findById(anyLong())).willReturn(Optional.of(visit));

        // When
        Visit foundVisit = service.findById(1L);

        // Then
        then(visitRepository).should().findById(anyLong());
        assertThat(foundVisit).isNotNull();
    }

    @Test
    void save() {
        // Given
        Visit visit = new Visit();
        given(visitRepository.save(any(Visit.class))).willReturn(visit);

        // When
        Visit savedVisit = service.save(new Visit());

        // Then
        verify(visitRepository).save(any(Visit.class));
        assertThat(savedVisit).isNotNull();
    }

    @Test
    void delete() {
        // Given
        Visit visit = new Visit();

        // When
        service.delete(visit);

        // Then
        then(visitRepository).should().delete(any(Visit.class));

    }

    @Test
    void deleteById() {

        // Given - none

        // When
        service.deleteById(1L);

        // Then
        then(visitRepository).should().deleteById(anyLong());
    }
}