$("#ordersLink").click(function () {
    loadAllOrders();
    $("#tblOrderDetails").empty();
});


function loadAllOrders() {
    $("#ordersTable").empty();
    for (let i of orderDB) {
        let button=`<button  type="button" class="btn btn-danger btn-sm"  onclick="viewDetails(this);">View Details</button>`;
        let row = `<tr><td>${i.getOrderId()}</td><td>${i.getOrderDate()}</td><td>${i.getOrderCustomerId()}</td><td>${i.getTotal()}</td><td>${i.getDiscount()}</td><td>${i.getNetTotal()}</td><td>${button}</td></tr>`;
        $("#ordersTable").append(row);
    }
}

function viewDetails(btn) {
    let val =$(btn).closest("tr").find("td:eq(0)").text();
    let orderDetailsArray;
    for (let i = 0; i < orderDB.length; i++) {
        if (orderDB[i].getOrderId() === val) {
            orderDetailsArray=orderDB[i].getOrderDetails();
        }
    }
    $("#tblOrderDetails").empty();
    for (let i of orderDetailsArray) {
        let row = `<tr><td>${i.getItem()}</td><td>${i.getUniPrice()}</td><td>${i.getPurchaseQty()}</td><td>${i.getItemTotal()}</td></tr>`;
        $("#tblOrderDetails").append(row);
    }
}