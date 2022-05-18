function OrderDetailsDto(item,uniPrice,purchaseQty,total) {
    var __item = item;
    var __uniPrice = uniPrice;
    var __purchaseQty = purchaseQty;
    var __total = total;

    this.getItem = function () {
        return __item;
    }
    this.setItem = function (i) {
        __item=i;
    }
    this.getUniPrice = function () {
        return __uniPrice;
    }
    this.setUniPrice = function (u) {
        __uniPrice=u;
    }
    this.getPurchaseQty = function () {
        return __purchaseQty;
    }
    this.setPurchaseQty = function (q) {
        __purchaseQty=q;
    }
    this.getItemTotal = function () {
        return __total;
    }
    this.setItemTotal = function (t) {
        __total=t;
    }
}