package de.htw.SA_basketService.unitTests;

import de.htw.SA_basketService.core.domain.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    private Item itemUnderTest;

    @BeforeEach
    void setUp() {
        itemUnderTest = new Item(UUID.fromString("3486336f-ee61-4a61-ac87-d33dd2e139da"),
                UUID.fromString("89d23342-a708-45d8-b473-211e1cad4d6e"), "name", new BigDecimal("0.00"), "imageLink");
    }

    @Test
    void testToString() {
        assertThat(itemUnderTest.toString()).isEqualTo("Item{itemId=3486336f-ee61-4a61-ac87-d33dd2e139da, " +
                "name='name', itemPrice=0.00, imageLink='imageLink'}");
    }

    @Test
    void testEqualsTrue() {
        // Setup
        Item referenceItem = new Item(UUID.fromString("3486336f-ee61-4a61-ac87-d33dd2e139da"),
                UUID.fromString("89d23342-a708-45d8-b473-211e1cad4d6e"), "name", new BigDecimal("0.00"),
                "imageLink");
        // Run the test
        final boolean result = itemUnderTest.equals(referenceItem);

        // Verify the results
        assertThat(result).isTrue();
    }

    @Test
    void testEqualsFalse() {
        // Setup
        Item referenceItem = new Item(UUID.fromString("3482336f-ee61-4a61-ac87-d33dd2e139da"),
                UUID.fromString("89d23342-a708-45d8-b473-211e1cad4d6e"), "name", new BigDecimal("10.00"),
                "imageLink");
        // Run the test
        final boolean result = itemUnderTest.equals(referenceItem);

        // Verify the results
        assertThat(result).isFalse();
    }

    @Test
    void testItemIdGetterAndSetter() {
        final UUID itemId = UUID.fromString("3486336f-ee61-4a61-ac87-d33dd2e139da");
        itemUnderTest.setItemId(itemId);
        assertThat(itemUnderTest.getItemId()).isEqualTo(itemId);
    }

    @Test
    void testPlantIdGetterAndSetter() {
        final UUID plantId = UUID.fromString("89d23342-a708-45d8-b473-211e1cad4d6e");
        itemUnderTest.setPlantId(plantId);
        assertThat(itemUnderTest.getPlantId()).isEqualTo(plantId);
    }

    @Test
    void testNameGetterAndSetter() {
        final String name = "name";
        itemUnderTest.setName(name);
        assertThat(itemUnderTest.getName()).isEqualTo(name);
    }

    @Test
    void testItemPriceGetterAndSetter() {
        final BigDecimal itemPrice = new BigDecimal("0.00");
        itemUnderTest.setItemPrice(itemPrice);
        assertThat(itemUnderTest.getItemPrice()).isEqualTo(itemPrice);
    }

    @Test
    void testImageLinkGetterAndSetter() {
        final String imageLink = "imageLink";
        itemUnderTest.setImageLink(imageLink);
        assertThat(itemUnderTest.getImageLink()).isEqualTo(imageLink);
    }
}
