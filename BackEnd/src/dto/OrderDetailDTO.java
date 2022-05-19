package dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDetailDTO implements Serializable {
    private String orderId;
    private String itemCode;
    private BigDecimal unitPrice;
    private int qty;
    private BigDecimal total;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(String orderId, String itemCode, BigDecimal unitPrice, int qty, BigDecimal total) {
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "orderId='" + orderId + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", total=" + total +
                '}';
    }
}
