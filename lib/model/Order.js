function OrderDto(id,date,cusId,total,discount,netTotal,orderDetails) {
    var __orderId = id;
    var __orderDate = date;
    var __customerId = cusId;
    var __total = total;
    var __discount = discount;
    var __netTotal = netTotal;
    var __orderDetails = [...orderDetails];

    this.getOrderId = function () {
        return __orderId;
    }
    this.setOrderId = function (i) {
        __orderId=i;
    }
    this.getOrderDate = function () {
        return __orderDate;
    }
    this.setOrderDate = function (d) {
        __orderDate=d;
    }
    this.getOrderCustomerId = function () {
        return __customerId;
    }
    this.setOrderCustomerId = function (c) {
        __customerId=c;
    }
    this.getNetTotal = function () {
        return __netTotal;
    }
    this.setNetTotal = function (n) {
        __netTotal=n;
    }
    this.getTotal = function () {
        return __total;
    }
    this.setTotal = function (s) {
        __total=s;
    }
    this.getDiscount = function () {
        return __discount;
    }
    this.setDiscount = function (dis) {
        __discount=dis;
    }
    this.getOrderDetails = function () {
        return __orderDetails;
    }
    this.setOrderDetails = function (details) {
        __orderDetails=[...details];
    }
}