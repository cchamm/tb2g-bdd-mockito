package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.VisitService;
import guru.springframework.sfgpetclinic.services.map.PetMapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class VisitControllerTestl {

    @Mock
    VisitService visitService;


    // Wrapper around the real implementation
    @Spy
    PetMapService petMapService;

    @InjectMocks
    VisitController visitController;


    @Test
    void loadPetWithVisit() {

        // given
        Map<String, Object> model = new HashMap<>();
        Pet pet = new Pet(12L);
        Pet pet3 = new Pet(3L);

        petMapService.save(pet);
        petMapService.save(pet3);

        given(petMapService.findById(anyLong())).willCallRealMethod();

        // when
        Visit visit = visitController.loadPetWithVisit(12L, model);

        // then
        assertThat(visit).isNotNull();

        assertThat(visit.getPet()).isNotNull();
        assertThat(visit.getPet().getId()).isNotNull();

        then(petMapService).should(times(1)).findById(anyLong());

    }

    @Test
    void loadPetWithVisitWithStubbing() {

        // given
        Map<String, Object> model = new HashMap<>();
        Pet pet = new Pet(12L);
        Pet pet3 = new Pet(3L);

        petMapService.save(pet);
        petMapService.save(pet3);

        given(petMapService.findById(anyLong())).willReturn(pet);

        // when
        Visit visit = visitController.loadPetWithVisit(12L, model);

        // then
        assertThat(visit).isNotNull();

        assertThat(visit.getPet()).isNotNull();
        assertThat(visit.getPet().getId()).isNotNull();

        assertThat(visit.getPet().getId()).isEqualTo(12L);
        then(petMapService).should(times(1)).findById(anyLong());

    }
}