package org.example.strategies.selection;

import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Inventory;
import org.example.model.Product;

import java.util.Optional;

/**
 * Basic Product Selection Strategy: Selects products by slot ID.
 * Direct slot-based selection (e.g., "A1", "B2").
 */
public class BasicProductSelectionStrategy implements ProductSelectionStrategy {

    @Override
    public String selectProduct(Object inventory, String criteria) {
        if (!(inventory instanceof Inventory)) {
            return null;
        }

        Inventory inv = (Inventory) inventory;

        // Criteria is the slot ID
        String slotId = criteria.toUpperCase().trim();

        // Validate slot ID format (should be letter + number)
        if (!isValidSlotId(slotId)) {
            System.out.println("Invalid slot ID format: " + slotId);
            return null;
        }

        // Check if product exists and is available
        Optional<Product> product = inv.getProduct(slotId);
        if (product.isPresent()) {
            if (inv.isProductAvailable(slotId)) {
                return slotId;
            } else {
                System.out.println("Product " + slotId + " is out of stock");
                return null;
            }
        } else {
            System.out.println("Product " + slotId + " not found");
            return null;
        }
    }

    @Override
    public String getStrategyName() {
        return "Slot ID Selection";
    }

    /**
     * Validates slot ID format.
     * Expected format: Letter(s) followed by number(s), e.g., "A1", "B12", "AA5"
     * @param slotId The slot ID to validate
     * @return true if format is valid
     */
    private boolean isValidSlotId(String slotId) {
        if (slotId == null || slotId.length() < 2 || slotId.length() > 4) {
            return false;
        }

        // Check pattern: starts with letter(s), ends with number(s)
        return slotId.matches("^[A-Za-z]+\\d+$");
    }
}
