package ch.hsr.apparch.purchaselist.controller;

import ch.hsr.apparch.purchaselist.exceptions.ResourceNotFoundException;
import ch.hsr.apparch.purchaselist.model.PurchaseList;
import ch.hsr.apparch.purchaselist.repository.PurchaseListItemRepository;
import ch.hsr.apparch.purchaselist.repository.PurchaseListRepository;
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

import java.time.LocalDate;
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
class PurchaseListTest {

    static final String PLURAL_MODEL_KEY = "models";
    static final String SINGULAR_MODEL_KEY = "model";
    static final String POSTURL_KEY = "posturl";
    static final String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:/controller/purchaseList/list";

    @MockBean
    PurchaseListRepository purchaseLists;

    @MockBean
    PurchaseListItemRepository items;

    @MockBean
    Model model;

    @Autowired
    PurchaseListController controller;

    @BeforeEach
    void setup() {
        Mockito.clearInvocations(purchaseLists, items, model);
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
    void testListPurchaseLists() {
        Mockito.when(purchaseLists.findAll()).thenReturn(Collections.emptyList());

        assertThat(controller.listPurchaseLists(model)).isEqualTo("list/list");

        Mockito.verify(purchaseLists, Mockito.never()).findAll(Pageable.unpaged());
        Mockito.verify(model).addAttribute(PLURAL_MODEL_KEY, purchaseLists.findAll());
    }

    @Test
    void testAddPurchaseListView() {
        assertThat(controller.editPurchaseList(Optional.empty(), model)).isEqualTo("list/edit");

        Mockito.verify(model).addAttribute(SINGULAR_MODEL_KEY, new PurchaseList());
        Mockito.verify(model).addAttribute(POSTURL_KEY, "/controller/purchaseList/add");
    }

    @Test
    void testEditUnknownPurchaseListView() {
        final long id = 100;
        Mockito.when(purchaseLists.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.editPurchaseList(Optional.of(id), model)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testEditPurchaseListView() {
        final long id = 0;
        final PurchaseList entity = new PurchaseList();
        Mockito.when(purchaseLists.findById(id)).thenReturn(Optional.of(entity));

        assertThat(controller.editPurchaseList(Optional.of(id), model)).isEqualTo("list/edit");

        Mockito.verify(purchaseLists).findById(id);
        Mockito.verify(model).addAttribute(SINGULAR_MODEL_KEY, entity);
        Mockito.verify(model).addAttribute(POSTURL_KEY, "/controller/purchaseList/" + id + "/update");
    }

    @Test
    void testAddPurchaseList() {
        final String name = "a test";
        final LocalDate date = LocalDate.of(2100, 8, 19);
        final PurchaseList entity = new PurchaseList(name, date, Collections.emptyList());

        assertThat(controller.addPurchaseList(name, date)).isEqualTo(REDIRECT_CONTROLLER_LIST_VIEW);

        Mockito.verify(purchaseLists).save(entity);
    }

    @Test
    void testUpdateUnknownPurchaseList() {
        final long id = 1000;
        final String name = "a test";
        final LocalDate date = LocalDate.of(2100, 8, 19);
        final PurchaseList entity = new PurchaseList(name, date, Collections.emptyList());
        Mockito.when(purchaseLists.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.updatePurchaseList(id, name, date)).isInstanceOf(ResourceNotFoundException.class);

        Mockito.verify(purchaseLists).findById(id);
        Mockito.verify(purchaseLists, Mockito.never()).save(entity);
    }

    @Test
    void testUpdatePurchaseList() {
        final long id = 1000;
        final String name = "a test";
        final LocalDate date = LocalDate.of(2100, 8, 19);
        final PurchaseList entity = new PurchaseList(name, date, Collections.emptyList());

        Mockito.when(purchaseLists.findById(id)).thenReturn(Optional.of(entity));
        Mockito.when(purchaseLists.save(entity)).thenReturn(entity);

        assertThat(controller.updatePurchaseList(id, name, date)).isEqualTo(REDIRECT_CONTROLLER_LIST_VIEW);

        Mockito.verify(purchaseLists).findById(id);
        Mockito.verify(purchaseLists).save(entity);
    }

    @Test
    void testDeletePurchaseList() {
        final long id = 100;

        assertThat(controller.deletePurchaseList(id)).isEqualTo(REDIRECT_CONTROLLER_LIST_VIEW);

        Mockito.verify(purchaseLists).deleteById(id);
    }
}
