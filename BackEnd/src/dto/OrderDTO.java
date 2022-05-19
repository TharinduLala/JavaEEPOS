package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderDTO implements Serializable {
    private String orderId;
    private LocalDate orderDate;
    private String customerId;
    private BigDecimal total;
    private BigDecimal discount;
    private BigDecimal netTotal;
    private ArrayList<OrderDetailDTO> orderDetails;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, LocalDate orderDate, String customerId, BigDecimal total, BigDecimal discount, BigDecimal netTotal, ArrayList<OrderDetailDTO> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.total = total;
        this.discount = discount;
        this.netTotal = netTotal;
        this.orderDetails = orderDetails;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(BigDecimal netTotal) {
        this.netTotal = netTotal;
    }

    public ArrayList<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", customerId='" + customerId + '\'' +
                ", total=" + total +
                ", discount=" + discount +
                ", netTotal=" + netTotal +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
