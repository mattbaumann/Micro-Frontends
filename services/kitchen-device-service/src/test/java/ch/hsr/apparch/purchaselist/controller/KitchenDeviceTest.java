package ch.hsr.apparch.purchaselist.controller;


import ch.hsr.apparch.kitchenDevices.controller.KitchenDeviceController;
import ch.hsr.apparch.kitchenDevices.exceptions.ResourceNotFoundException;
import ch.hsr.apparch.kitchenDevices.model.KitchenDevice;
import ch.hsr.apparch.kitchenDevices.repository.KitchenDeviceRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag("Controller")
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class KitchenDeviceTest {

    static final String PLURAL_MODEL_KEY = "models";
    static final String SINGULAR_MODEL_KEY = "model";
    static final String POSTURL_KEY = "posturl";
    static final String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:/controller/purchaseList/list";
    
    @MockBean
    KitchenDeviceRepository kitchenDevices;
    
    @MockBean
    Model model;
    
    @Autowired
    KitchenDeviceController controller;

    @BeforeEach
    void setup() {
        Mockito.clearInvocations(kitchenDevices, model);
    }

    @Test
    void testDependencyInjection() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testHomeRoute() {
        assertThat(controller.home()).isEqualTo("home");
    }

    @Test
    void testListKitchenDevices() {
        Mockito.when(kitchenDevices.findAll()).thenReturn(Collections.emptyList());

        assertThat(controller.listKitchenDevices(model)).isEqualTo("list/list");

        Mockito.verify(kitchenDevices, Mockito.never()).findAll(Pageable.unpaged());
        Mockito.verify(model).addAttribute(PLURAL_MODEL_KEY, kitchenDevices.findAll());
    }

    @Test
    void testAddKitchenDeviceView() {
        assertThat(controller.editKitchenDevice(Optional.empty(), model)).isEqualTo("list/edit");

        Mockito.verify(model).addAttribute(SINGULAR_MODEL_KEY, new KitchenDevice());
        Mockito.verify(model).addAttribute(POSTURL_KEY, "/controller/purchaseList/add");
    }

    @Test
    void testEditUnknownKitchenDeviceView() {
        final long id = 100;
        Mockito.when(kitchenDevices.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.editKitchenDevice(Optional.of(id), model)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testEditKitchenDeviceView() {
        final long id = 0;
        final KitchenDevice entity = new KitchenDevice();
        Mockito.when(kitchenDevices.findById(id)).thenReturn(Optional.of(entity));

        assertThat(controller.editKitchenDevice(Optional.of(id), model)).isEqualTo("list/edit");

        Mockito.verify(kitchenDevices).findById(id);
        Mockito.verify(model).addAttribute(SINGULAR_MODEL_KEY, entity);
        Mockito.verify(model).addAttribute(POSTURL_KEY, "/controller/purchaseList/" + id + "/update");
    }

    @Test
    void testAddKitchenDevice() {
        final String name = "a test";
        final String function = "Test func";
        final boolean available = false;
        final KitchenDevice entity = new KitchenDevice(name, function, available);

        assertThat(controller.addKitchenDevice(name, function, available)).isEqualTo(REDIRECT_CONTROLLER_LIST_VIEW);

        Mockito.verify(kitchenDevices).save(entity);
    }

    @Test
    void testUpdateUnknownKitchenDevice() {
        final long id = 1000;
        final String name = "a test";
        final String function = "Test func";
        final boolean available = false;
        final KitchenDevice entity = new KitchenDevice(name, function, available);
        Mockito.when(kitchenDevices.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.updateKitchenDevice(id, name, function, available))
                .isInstanceOf(ResourceNotFoundException.class);

        Mockito.verify(kitchenDevices).findById(id);
        Mockito.verify(kitchenDevices, Mockito.never()).save(entity);
    }

    @Test
    void testUpdateKitchenDevice() {
        final long id = 1000;
        final String name = "a test";
        final String function = "Test func";
        final boolean available = false;
        final KitchenDevice entity = new KitchenDevice(name, function, available);

        Mockito.when(kitchenDevices.findById(id)).thenReturn(Optional.of(entity));
        Mockito.when(kitchenDevices.save(entity)).thenReturn(entity);

        assertThat(controller.updateKitchenDevice(id, name, function, available))
                .isEqualTo(REDIRECT_CONTROLLER_LIST_VIEW);

        Mockito.verify(kitchenDevices).findById(id);
        Mockito.verify(kitchenDevices).save(entity);
    }

    @Test
    void testDeleteKitchenDevice() {
        final long id = 100;

        assertThat(controller.deleteKitchenDevice(id)).isEqualTo(REDIRECT_CONTROLLER_LIST_VIEW);

        Mockito.verify(kitchenDevices).deleteById(id);
    }
}
