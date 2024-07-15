package de.htw.SA_basketService.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity //Marks class as database Entity
@Table  //Table in Data Base (here you can name the table)
@Getter
@Setter
@NoArgsConstructor
public class Basket {
    @Id
    @NotNull(message = "username cannot be null")
    private String username;

    @Valid
    @NotNull(message = "Items cannot be null")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "username")
    private List<Item> items;

    @NotNull (message = "totalPrice cannot be null")
    @PositiveOrZero(message = "totalPrice has to be positive or zero")
    private BigDecimal totalPrice;

    public Basket(String username) {
        this.username = username;
        this.items = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }
}
