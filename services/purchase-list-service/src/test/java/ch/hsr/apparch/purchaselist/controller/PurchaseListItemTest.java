package ch.hsr.apparch.purchaselist.controller;

import ch.hsr.apparch.purchaselist.exceptions.ResourceNotFoundException;
import ch.hsr.apparch.purchaselist.model.PurchaseList;
import ch.hsr.apparch.purchaselist.model.PurchaseListItem;
import ch.hsr.apparch.purchaselist.repository.PurchaseListItemRepository;
import ch.hsr.apparch.purchaselist.repository.PurchaseListRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log4j2
@Tag("Controller")
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PurchaseListItemTest {

    private static final String PLURAL_MODEL_KEY = "models";
    private static final String SINGULAR_MODEL_KEY = "model";
    private static final String POSTURL_KEY = "posturl";

    @MockBean
    private PurchaseListRepository purchaseLists;

    @MockBean
    private PurchaseListItemRepository items;

    @MockBean
    private Model model;

    @Autowired
    private PurchaseListController controller;

    @BeforeEach
    void setup() {
        Mockito.clearInvocations(purchaseLists, items, model);
    }

    @Test
    void testListPurchaseListItems() {
        final long id = 100;
        final String name = "a test";
        final LocalDate date = LocalDate.of(2100, 8, 19);
        final List<PurchaseListItem> items = Collections.emptyList();
        final PurchaseList entity = Mockito.spy(new PurchaseList(name, date, items));

        Mockito.when(purchaseLists.findById(id)).thenReturn(Optional.of(entity));

        assertThat(controller.listPurchaseListItems(id, model)).isEqualTo("items/list");

        Mockito.verify(purchaseLists).findById(id);
        Mockito.verify(model).addAttribute(PLURAL_MODEL_KEY, items);
        Mockito.verify(model).addAttribute("plid", id);
        //noinspection ResultOfMethodCallIgnored
        Mockito.verify(entity).getItems();
    }

    @Test
    void testListUnknownPurchaseListItems() {
        final long id = 100;

        Mockito.when(purchaseLists.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.listPurchaseListItems(id, model))
                .isInstanceOf(ResourceNotFoundException.class);

        Mockito.verify(purchaseLists).findById(id);
        Mockito.verify(model, Mockito.never()).addAttribute(PLURAL_MODEL_KEY, Collections.emptyList());
        Mockito.verify(model, Mockito.never()).addAttribute("plid", id);
    }

    @Test
    void testAddPurchaseListItemsView() {
        final long plid = 100;

        assertThat(controller.editPurchaseListItems(plid, Optional.empty(), model)).isEqualTo("items/edit");

        Mockito.verify(model).addAttribute(POSTURL_KEY, "/controller/purchaseList/" + plid + "/item/add");
        Mockito.verify(model).addAttribute(SINGULAR_MODEL_KEY, new PurchaseListItem());
    }

    @Test
    void testEditPurchaseListItemsView() {
        final long plid = 100;
        final long itemId = 10;
        final String name = "Test";
        final LocalDate date = LocalDate.of(2100, 8, 19);
        final PurchaseList entityHolder = new PurchaseList(name, date, Collections.emptyList());
        final PurchaseListItem entity = new PurchaseListItem(name, entityHolder);
        entityHolder.setItems(Collections.singletonList(entity));

        Mockito.when(items.findById(itemId)).thenReturn(Optional.of(entity));

        assertThat(controller.editPurchaseListItems(plid, Optional.of(itemId), model))
                .isEqualTo("items/edit");

        Mockito.verify(model).addAttribute(POSTURL_KEY, "/controller/purchaseList/" + plid + "/item/" + itemId + "/update");
        Mockito.verify(model).addAttribute(SINGULAR_MODEL_KEY, entity);
    }

    @Test
    void testEditUnknownPurchaseListItemsView() {
        final long plid = 100;
        final long itemId = 10;

        Mockito.when(items.findById(itemId)).thenReturn(Optional.empty());

        assertThat(controller.editPurchaseListItems(plid, Optional.of(itemId), model))
                .isEqualTo("items/edit");

        Mockito.verify(model).addAttribute(POSTURL_KEY, "/controller/purchaseList/" + plid + "/item/add");
        Mockito.verify(model).addAttribute(SINGULAR_MODEL_KEY, new PurchaseListItem());
    }

    @Test
    void testAddPurchaseListItem() {
        final long plid = 100;
        final String name = "Test";
        final LocalDate date = LocalDate.of(2100, 8, 19);
        final PurchaseList list = new PurchaseList(name, date, Collections.emptyList());
        final PurchaseListItem entity = new PurchaseListItem(name, list);

        Mockito.when(purchaseLists.findById(plid)).thenReturn(Optional.of(list));
        Mockito.when(items.save(entity)).thenReturn(entity);

        assertThat(controller.addPurchaseListItem(plid, name)).isEqualTo("redirect:/controller/purchaseList/" + plid + "/list");

        Mockito.verify(items).save(entity);
        Mockito.verify(purchaseLists).findById(plid);
    }

    @Test
    void testEditPurchaseListItem() {
        final long plid = 100;
        final long itemId = 1;
        final String name = "Test";
        final LocalDate date = LocalDate.of(2100, 8, 19);
        final PurchaseList list = new PurchaseList(name, date, Collections.emptyList());
        final PurchaseListItem entity = new PurchaseListItem(name, list);

        Mockito.when(items.findById(itemId)).thenReturn(Optional.of(entity));
        Mockito.when(items.save(entity)).thenReturn(entity);

        assertThat(controller.updatePurchaseListItem(plid, itemId, name)).isEqualTo("redirect:/controller/purchaseList/" + plid + "/list");

        Mockito.verify(items).findById(itemId);
        Mockito.verify(items).save(entity);
    }

    @Test
    void testEditUnkownPurchaseListItem() {
        final long plid = 100;
        final long itemId = 1;
        final String name = "Test";
        final LocalDate date = LocalDate.of(2100, 8, 19);
        final PurchaseList list = new PurchaseList(name, date, Collections.emptyList());
        final PurchaseListItem entity = new PurchaseListItem(name, list);

        Mockito.when(items.findById(itemId)).thenReturn(Optional.empty());
        Mockito.when(items.save(entity)).thenReturn(entity);

        assertThatThrownBy(() -> controller.updatePurchaseListItem(plid, itemId, name)).isInstanceOf(ResourceNotFoundException.class);

        Mockito.verify(items).findById(itemId);
        Mockito.verify(items, Mockito.never()).save(entity);
    }

    @Test
    void testRemovePurchaseListItem() {
        final long plid = 100;
        final long itemId = 1;

        assertThat(controller.removePurchaseListItem(plid, itemId)).isEqualTo("redirect:/controller/purchaseList/" + plid + "/list");

        Mockito.verify(items).deleteById(itemId);
    }
}
