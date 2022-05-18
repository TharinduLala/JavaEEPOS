function CustomerDto(id,name,address,contact) {
    var __id=id;
    var __name=name;
    var __address=address;
    var __contact=contact;

    this.getCustomerId = function () {
        return __id;
    }
    this.setCustomerId = function (i) {
        __id=i;
    }
    this.getCustomerName = function () {
        return __name;
    }
    this.setCustomerName = function (n) {
        __name=n;
    }
    this.getCustomerAddress = function () {
        return __address;
    }
    this.setCustomerAddress = function (a) {
        __address=a;
    }
    this.getCustomerContact = function () {
        return __contact;
    }
    this.setCustomerContact = function (c) {
        __contact=c;
    }
}