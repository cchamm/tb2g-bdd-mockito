package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {


    public static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController controller;

    @Test
    void processCreationFormBindingResultWithErrors() {
        // log

        // given
        BindingResult bindingResult = mock(BindingResult.class);
        given(bindingResult.hasErrors()).willReturn(true);
        Owner owner = new Owner(1L, "Sam", "Lui");
        // when
        var result = controller.processCreationForm(owner, bindingResult);

        // then
        then(bindingResult).should(atLeastOnce()).hasErrors();
        assertThat(result).isEqualTo(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
    }

    @Test
    void processCreationFormBindingResultNoError() {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        given(bindingResult.hasErrors()).willReturn(false);
        Owner owner = new Owner(null,  "Sam", "Lui");
        Owner savedOwner = new Owner(1L, "Sam", "Lui");

        given(ownerService.save(argThat(argument -> argument.getFirstName().equals("Sam")))).willReturn(savedOwner);

        // when
        var result = controller.processCreationForm(owner, bindingResult);

        // then
        then(ownerService).should(atLeastOnce()).save(any(Owner.class));
        then(bindingResult).should(atLeastOnce()).hasErrors();
        assertThat(result).isEqualTo("redirect:/owners/"+savedOwner.getId());
    }
}