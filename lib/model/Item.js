function ItemDto(itemCode,description,unitPrice,qtyOnHand) {
    var __itemCode=itemCode;
    var __description=description;
    var __unitPrice=unitPrice;
    var __qtyOnHand=qtyOnHand;

    this.getItemCode = function () {
        return __itemCode;
    }
    this.setItemCode = function (c) {
        __itemCode=c;
    }
    this.getDescription = function () {
        return __description;
    }
    this.setDescription = function (d) {
        __description=d;
    }
    this.getUnitPrice = function () {
        return __unitPrice;
    }
    this.setUnitPrice = function (u) {
        __unitPrice=u;
    }
    this.getQtyOnHand = function () {
        return __qtyOnHand;
    }
    this.setQtyOnHand = function (q) {
        __qtyOnHand=q;
    }
}