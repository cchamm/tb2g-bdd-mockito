package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture())).willAnswer(
                invocation -> {
                    List<Owner> owners = new ArrayList<>();
                    String name = invocation.getArgument(0);

                    System.out.println("name: " + name + " equals: " + name.equals("%DontFindMe%"));
                    if (name.equals("%Buck%")) {
                        owners.add(new Owner(1L, "Joe", "Buck"));
                        return owners;
                    } else if (name.equals("%DontFindMe%")) {
                        return owners;
                    } else if (name.equals("%FindMe%")) {
                        owners.add(new Owner(1L, "Tom", "FindMe"));
                        owners.add(new Owner(2L, "Sam", "FindMe2"));
                        return owners;
                    }
                    throw new RuntimeException("Invalid argument");
                }
        );
    }


    @Test
    void processFindFormWildcardStringAnnotation() {
        // Given
        Owner owner = new Owner(1L, "Joe", "Buck");
        // when
        String viewName = controller.processFindForm(owner, mock(BindingResult.class), null);

        // then
        assertThat("%Buck%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat(viewName).isEqualToIgnoringCase("redirect:/owners/1");
    }

    @Test
    void processFindFormWildcardNotFound() {
        // Given
        Owner owner = new Owner(1L, "Joe", "DontFindMe");
        // when
        String viewName = controller.processFindForm(owner, mock(BindingResult.class), null);

        // then
        then(ownerService).shouldHaveNoMoreInteractions();
        assertThat((String) stringArgumentCaptor.getValue()).isEqualToIgnoringCase("%DontFindMe%");
        assertThat(viewName).isEqualToIgnoringCase("owners/findOwners");
    }

    @Test
    void processFindFormWildcardFoundMultiple() {
        // Given
        var model = mock(Model.class);
        Owner owner = new Owner(1L, "Joe", "FindMe");
        InOrder inOrder = inOrder(ownerService, model );
        // when
        String viewName = controller.processFindForm(owner, mock(BindingResult.class), model);

        // then
        assertThat((String) stringArgumentCaptor.getValue()).isEqualToIgnoringCase("%FindMe%");
        assertThat(viewName).isEqualToIgnoringCase("owners/ownersList");
        // inorder asserts
        inOrder.verify(ownerService).findAllByLastNameLike(anyString());
        inOrder.verify(model).addAttribute(anyString(), anyList());
        then(model).shouldHaveNoMoreInteractions();
    }


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