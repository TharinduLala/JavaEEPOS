$("#newOrderLink").click(function () {
    loadItemCodes();
    setDate();
    setNewOrderId();
    $('#btnProceedOrder').attr('disabled', true);
});

$("#btnCustomerSave").click(function () {
    saveCustomer("customerForm2");

    $("#txtCusid").val("");
    $("#txtCusname").val("");
    $("#txtCusaddress").val("");
    $("#txtCusContact").val("");
});//done

$('#btnSearchCus').click(function () {
    let cId = $('#txtNewOrdercusId').val()
    $.ajax({
        url: "http://localhost:8081/backEnd/customer?option=SEARCH&customerId=" + cId,
        method: "GET",
        success: function (response) {
            if (response.status == 400) {
                alert(response.message);
            } else {
                const customer = response.data;
                let id = customer.id;
                let name = customer.name;
                let address = customer.address;
                let contact = customer.contact;
                $("#txtNewOrdercusId").val(id);
                $("#txtNewOrdercusName").val(name);
                $("#txtNewOrdercusAddress").val(address);
                $("#txtNewOrdercusContact").val(contact);
            }
        }

    });
});//done

$("#txtNewOrderItemCode").change(function () {
    let code = $(this).val();
    $.ajax({
        url: "http://localhost:8081/backEnd/item?option=SEARCH&itemCode=" + code,
        method: "GET",
        success: function (response) {
            if (response.status == 400) {
                alert(response.message);
            } else {
                const item = response.data;
                let description = item.description;
                let unitPrice = item.unitPrice;
                let qtyOnHand = item.qtyOnHand;
                $('#txtNewOrderItemDescription').val(description);
                $('#txtNewOrderItemUnitPrice').val(unitPrice);
                $('#txtNewOrderItemQtyOnH').val(qtyOnHand);
            }
        }

    });
});//done

$('#btnAddToCart').click(function () {
    let item = $('#txtNewOrderItemCode').val();
    let unitPrice = $('#txtNewOrderItemUnitPrice').val();
    let oderQty = $('#txtNewOrderOrderedQty').val();
    let total = unitPrice * oderQty;
    let button = `<button  type="button" class="btn btn-danger btn-sm"  onclick="deleteRow(this);">Remove</button>`;
    addCart(item, unitPrice, oderQty, total, button);
    calculateTotal();
    $('#btnProceedOrder').attr('disabled', true);
});//done

$('#btnCalculate').click(function () {
    calculateNetTotal();
    $('#btnProceedOrder').attr('disabled', false);
});//done

$("#btnProceedOrder").click(function () {
    let oId = $("#txtNewOrderId").val();
    let oDate = $('#txtNewOrderDate').val();
    let customer = $("#txtNewOrdercusId").val();
    let total = $('#txtNewOrderTotal').val();
    let discount = $('#txtNewOrderDiscount').val();
    let netTotal = $('#txtNewOrderNetTotal').val();
    let orderDetail = [];

    $("#tblNewOrderCart tr").each(function () {
        orderDetail.push(new OrderDetailsDto(
            this.cells[0].innerHTML,
            this.cells[1].innerHTML,
            this.cells[2].innerHTML,
            this.cells[3].innerHTML
        ));
        updateQty(this.cells[0].innerHTML, this.cells[2].innerHTML);
    });
    orderDB.push(new OrderDto(oId, oDate, customer, total, discount, netTotal, orderDetail));
    alert("Success");
    cancelOrder();

});

$('#btnClearSelection').click(function () {
    clearItemSelection();
});//done

$('#btnCancelOrder').click(function () {
    cancelOrder();
});//done

function setNewOrderId() {
    let code = $("#txtSearchItem").val();
    $.ajax({
        url: "http://localhost:8081/backEnd/order?option=GETOID",
        method: "GET",
        success: function (response) {
            if (response.status == 400) {
                alert(response.message);
            } else {
                const newId = response.data;
                $("#txtNewOrderId").val(newId);
            }
        }
    });
}//done

function deleteRow(btn) {
    /*let b=false;
    if(!b){
        let itemCode = $(btn).closest("tr").find("td:eq(0)").text();
        b=true;
    }
    if(b){
        $(btn).closest("tr").remove();
    }*/
    $(btn).closest("tr").remove();
    calculateTotal();

}//done

function setDate() {
    let date = new Date().toLocaleDateString("fr-FR");
    $('#txtNewOrderDate').val(date);
}//done

function loadItemCodes() {
    $('#txtNewOrderItemCode').empty();
    $.ajax({
        url: "http://localhost:8081/backEnd/item?option=GET_ALL",
        method: "GET",
        success: function (response) {
            if (response.status == 201) {
                let data = `<option >${"--select item--"}</option>`;
                $("#txtNewOrderItemCode").append(data);
                for (const item of response.data) {
                    let data = `<option >${item.code}</option>`;
                    $("#txtNewOrderItemCode").append(data);
                }
            } else {
                alert("Problem while loading items")
            }
        }
    });
}//done

function calculateTotal() {
    let total = 0;
    $("#tblNewOrderCart tr").each(function () {
        let rowTotal = parseInt(this.cells[3].innerHTML);
        total += rowTotal;

    });
    $('#txtNewOrderTotal').val(total);
    calculateNetTotal();
}//done

function addCart(item, unitPrice, qty, total, btn) {
    let not = true;
    let currentQty;
    let removeRow;
    $("#tblNewOrderCart tr").each(function () {
        if (this.cells[0].innerHTML === item) {
            not = false;
            currentQty = parseInt(this.cells[2].innerHTML);
            removeRow = $(this);
        }
    });
    let newQty = currentQty + parseInt(qty);
    let newTot = newQty * unitPrice;
    let qtyOnHand = parseInt($('#txtNewOrderItemQtyOnH').val());
    if (qty > qtyOnHand || newQty > qtyOnHand) {
        alert("not enough qty");
    } else {
        if (not) {
            let row = `<tr><td>${item}</td><td>${unitPrice}</td><td>${qty}</td><td>${total}</td><td>${btn}</td></tr>`;
            $("#tblNewOrderCart").append(row);
        } else {
            removeRow.remove();
            let row = `<tr><td>${item}</td><td>${unitPrice}</td><td>${newQty}</td><td>${newTot}</td><td>${btn}</td></tr>`;
            $("#tblNewOrderCart").append(row);
        }
    }
}//done

function clearItemSelection() {
    $('#txtNewOrderItemCode').val("");
    $('#txtNewOrderItemUnitPrice').val("");
    $('#txtNewOrderItemDescription').val("");
    $('#txtNewOrderItemQtyOnH').val("");
    $('#txtNewOrderOrderedQty').val("");
    loadItemCodes();
}//done

function cancelOrder() {
    clearItemSelection();
    setDate();
    setNewOrderId();
    $('#btnProceedOrder').attr('disabled', true);
    $('#txtNewOrderTotal').val("");
    $('#txtNewOrderDiscount').val("");
    $('#txtNewOrderNetTotal').val("");
    $("#txtNewOrdercusId").val("");
    $("#txtNewOrdercusName").val("");
    $("#txtNewOrdercusAddress").val("");
    $("#txtNewOrdercusContact").val("");
    $("#tblNewOrderCart").empty();
}//done

function updateQty(itemCode, qty) {
    let buyQty = parseInt(qty);
    for (let i = 0; i < itemDB.length; i++) {
        if (itemDB[i].getItemCode() === itemCode) {
            let current = parseInt(itemDB[i].getQtyOnHand());
            let newQty = current - buyQty;
            itemDB[i].setQtyOnHand(newQty);
        }
    }
}

function calculateNetTotal() {
    let total = parseInt($('#txtNewOrderTotal').val());
    //let discount =parseInt($('#txtNewOrderDiscount').val());
    let temp = $('#txtNewOrderDiscount').val();
    let discount;
    if (temp === "") {
        discount = 0;
    } else {
        discount = parseInt(temp);
    }

    let netTotal = total - ((total * discount) / 100);
    $('#txtNewOrderNetTotal').val(netTotal);
}