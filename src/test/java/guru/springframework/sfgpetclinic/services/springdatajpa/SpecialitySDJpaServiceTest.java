package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialityRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void testDeleteByObject() {
        // Given
        Speciality speciality = new Speciality();

        // When
        service.delete(speciality);

        // then
        then(specialityRepository).should().delete(any(Speciality.class));
    }

    @Test
    void findByIdTest() {
        Speciality speciality = new Speciality();

        when(specialityRepository.findById(1L)).thenReturn(Optional.of(speciality));

        Speciality foundSpecialty = service.findById(1L);

        assertThat(foundSpecialty).isNotNull();

        verify(specialityRepository).findById(anyLong());

    }


    @Test
    void findByIdBDDTest() {
        Speciality speciality = new Speciality();

        // Given
        given(specialityRepository.findById(1L)).willReturn(Optional.of(speciality));

        // When
        Speciality foundSpecialty = service.findById(1L);


        // then
        assertThat(foundSpecialty).isNotNull();
        then(specialityRepository).should().findById(anyLong());
        then(specialityRepository).should(times(1)).findById(anyLong());
        then(specialityRepository).shouldHaveNoMoreInteractions();

    }

    @Test
    void deleteById() {
        // Give - None

        // When
        service.deleteById(1l);
        service.deleteById(1l);

        // Then
        then(specialityRepository).should(times(2)).deleteById(1l);
    }

    @Test
    void deleteByIdAtLeast() {
        // Given - None
        // When
        service.deleteById(1l);
        service.deleteById(1l);

        // Then
        then(specialityRepository).should(atLeastOnce()).deleteById(1l);
    }

    @Test
    void deleteByIdAtMost() {
        // Given - None

        // When
        service.deleteById(1l);
        service.deleteById(1l);

        // Then
        then(specialityRepository).should(atMost(5)).deleteById(1L);
    }

    @Test
    void deleteByIdNever() {
        // Given - None

        // When
        service.deleteById(1l);
        service.deleteById(1l);


        // Then
        then(specialityRepository).should(atLeastOnce()).deleteById(1l);

        then(specialityRepository).should(never()).deleteById(5L);
    }

    @Test
    void testDelete() {
        // when
        service.delete(new Speciality());

        // then
        then(specialityRepository).should().delete(any(Speciality.class));
    }

    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("boom")).when(specialityRepository).delete(any(Speciality.class));

        assertThrows(RuntimeException.class, () -> specialityRepository.delete(new Speciality()));

        verify(specialityRepository).delete(any());
    }

    @Test
    void testFindByIDDoThrows() {
        // given first style to throw
        given(specialityRepository.findById(1L)).willThrow(new RuntimeException("boom"));

        // When
        assertThrows(RuntimeException.class, () -> service.findById(1L));

        // then
        then(specialityRepository).should().findById(1L);

    }

    @Test
    void testDeleteBDDSecondStyle() {
        // BDD throw exception second style
        willThrow(new RuntimeException("boom")).given(specialityRepository).delete(any(Speciality.class));

        assertThrows(RuntimeException.class, () -> service.delete(new Speciality()));

        then(specialityRepository).should().delete(any(Speciality.class));
    }
}