package de.htw.SA_basketService.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity //Marks class as database Entity
@Table  //Table in Data Base (here you can name the table)
@Getter
@Setter
@NoArgsConstructor
public class Basket {
    @Id
    @NotNull(message = "userId cannot be null")
    private UUID userId;

    @NotNull(message = "Items cannot be null")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userId")
    private List<Item> items;

    @NotNull (message = "totalPrice cannot be null")
    @PositiveOrZero(message = "totalPrice has to be positive or zero")
    private BigDecimal totalPrice;
}
