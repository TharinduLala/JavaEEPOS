$("#ordersLink").click(function () {
    loadAllOrders();
    $("#tblOrderDetails").empty();
});


function loadAllOrders() {
    $("#ordersTable").empty();
    $.ajax({
        url: "http://localhost:8081/backEnd/order?option=GET_ALL",
        method: "GET",
        success: function (response) {
            if (response.status == 201) {
                for (const order of response.data) {
                    let button = `<button  type="button" class="btn btn-danger btn-sm"  onclick="viewDetails(this);">View Details</button>`;
                    let row = `<tr><td>${order.orderId}</td><td>${order.orderDate}</td><td>${order.customerId}</td><td>${order.total}</td><td>${order.discount}</td><td>${order.netTotal}</td><td>${button}</td></tr>`;
                    $("#ordersTable").append(row);
                }
            } else {
                alert("Problem while loading orders")
            }
        }
    });

}

function viewDetails(btn) {
  /*  let val = $(btn).closest("tr").find("td:eq(0)").text();
    let orderDetailsArray;
    for (let i = 0; i < orderDB.length; i++) {
        if (orderDB[i].getOrderId() === val) {
            orderDetailsArray = orderDB[i].getOrderDetails();
        }
    }
    $("#tblOrderDetails").empty();
    for (let i of orderDetailsArray) {
        let row = `<tr><td>${i.getItem()}</td><td>${i.getUniPrice()}</td><td>${i.getPurchaseQty()}</td><td>${i.getItemTotal()}</td></tr>`;
        $("#tblOrderDetails").append(row);
    }*/
    alert("Clicked");
}