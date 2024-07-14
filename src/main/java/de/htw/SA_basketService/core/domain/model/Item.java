package de.htw.SA_basketService.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity //Marks class as database Entity
@Table  //Table in Data Base (here you can name the table)
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemId;

    @NotNull(message = "name cannot be null")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotNull (message = "item Price cannot be null")
    @PositiveOrZero(message = "item Price has to be positive or zero")
    private BigDecimal itemPrice;

    @NotNull (message = "Image link cannot be null")
    private String imageLink;

    public Item(UUID itemId, String name, BigDecimal itemPrice, String imageLink) {
        this.itemId = itemId;
        this.name = name;
        this.itemPrice = itemPrice;
        this.imageLink = imageLink;
    }

    public Item(String name, BigDecimal itemPrice, String imageLink) {
        this.name = name;
        this.itemPrice = itemPrice;
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", itemPrice=" + itemPrice +
                ", imageLink='" + imageLink + '\'' +
                '}';
    }
}
