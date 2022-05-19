package dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemDTO implements Serializable {
    private String itemCode;
    private String itemDescription;
    private int itemQtyOnHand;
    private BigDecimal itemUnitPrice;

    public ItemDTO() {
    }

    public ItemDTO(String itemCode, String itemDescription, int itemQtyOnHand, BigDecimal itemUnitPrice) {
        this.itemCode = itemCode;
        this.itemDescription = itemDescription;
        this.itemQtyOnHand = itemQtyOnHand;
        this.itemUnitPrice = itemUnitPrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getItemQtyOnHand() {
        return itemQtyOnHand;
    }

    public void setItemQtyOnHand(int itemQtyOnHand) {
        this.itemQtyOnHand = itemQtyOnHand;
    }

    public BigDecimal getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(BigDecimal itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "itemCode='" + itemCode + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemQtyOnHand=" + itemQtyOnHand +
                ", itemUnitPrice=" + itemUnitPrice +
                '}';
    }
}
