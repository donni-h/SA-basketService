package de.htw.SA_basketService.core.domain.service.interfaces;

import de.htw.SA_basketService.core.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
}
