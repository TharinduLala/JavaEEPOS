$("#newOrderLink").click(function () {
    loadItemCodes();setDate();setNewOrderId();
});

$("#btnCustomerSave").click(function () {
    customerSave();
});

$('#btnSearchCus').click(function () {
    let customer = searchCustomer($("#txtNewOrdercusId").val());
    if (customer) {
        $("#txtNewOrdercusId").val(customer.getCustomerId());
        $("#txtNewOrdercusName").val(customer.getCustomerName());
        $("#txtNewOrdercusAddress").val(customer.getCustomerAddress());
        $("#txtNewOrdercusContact").val(customer.getCustomerContact());
    } else {
        alert("No Such a Customer");
    }
});

$('#txtNewOrderItemCode').change(function () {
    let itemCode = $(this).val();
    for (let i = 0; i < itemDB.length; i++) {
        if (itemDB[i].getItemCode() === itemCode) {
            $('#txtNewOrderItemDescription').val(itemDB[i].getDescription());
            $('#txtNewOrderItemUnitPrice').val(itemDB[i].getUnitPrice());
            $('#txtNewOrderItemQtyOnH').val(itemDB[i].getQtyOnHand());
        }
    }
});

$('#txtNewOrderItemCode').click(function () {
    if(itemDB.length===0){
        alert("No items. Please enter items");
    }
});

$('#btnAddToCart').click(function () {
    let item=$('#txtNewOrderItemCode').val();
    let unitPrice= $('#txtNewOrderItemUnitPrice').val();
    let oderQty = $('#txtNewOrderOrderedQty').val();
    let total=unitPrice*oderQty;
    let button=`<button  type="button" class="btn btn-danger btn-sm"  onclick="deleteRow(this);">Remove</button>`;
    addCart(item,unitPrice,oderQty,total,button);
    calculateTotal();
    $('#btnProceedOrder').attr('disabled', true);
});

$('#btnCalculate').click(function () {
    let total =parseInt($('#txtNewOrderTotal').val());
    //let discount =parseInt($('#txtNewOrderDiscount').val());
    let temp =$('#txtNewOrderDiscount').val();
    let discount;
    if(temp===""){
        discount=0;
    }else {
        discount =parseInt(temp);
    }

    let netTotal =total-((total*discount)/100);
    $('#txtNewOrderNetTotal').val(netTotal);
    $('#btnProceedOrder').attr('disabled', false);
});

$('#btnProceedOrder').click(function () {
    let oId=$("#txtNewOrderId").val();
    let oDate=$('#txtNewOrderDate').val();
    let customer=$("#txtNewOrdercusId").val();
    let total = $('#txtNewOrderTotal').val();
    let discount=$('#txtNewOrderDiscount').val();
    let netTotal=$('#txtNewOrderNetTotal').val();
    let orderDetail = [];

    $("#tblNewOrderCart tr").each(function() {
        orderDetail.push(new OrderDetailsDto(
            this.cells[0].innerHTML,
            this.cells[1].innerHTML,
            this.cells[2].innerHTML,
            this.cells[3].innerHTML
        ));
        updateQty(this.cells[0].innerHTML,this.cells[2].innerHTML);
    });
    orderDB.push(new OrderDto(oId,oDate,customer,total,discount,netTotal,orderDetail));
    alert("Success");
    cancelOrder();

});
$('#btnClearSelection').click(function () {
   clearSelection();
});
$('#btnCancelOrder').click(function () {
    cancelOrder();
});
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
}

function customerSave() {
    let customerID = $("#txtCusid").val();
    if (isCustomerExist(customerID)) {
        alert("This customer id already exist..");
    } else {
        let response = confirm("Do you want to add this Customer..?");
        if (response) {
            let customerName = $("#txtCusname").val();
            let customerAddress = $("#txtCusaddress").val();
            let customerContact = $("#txtCusContact").val();

            let customerDto = new CustomerDto(customerID, customerName, customerAddress, customerContact);

            customerDB.push(customerDto);
        }
    }
}

function setNewOrderId() {
    if (orderDB.length === 0) {
        $("#txtNewOrderId").val("#Od-01");
    } else {
        let last = (orderDB[orderDB.length-1].getOrderId().slice(5));
        let newValue = "#Od-0" + (parseInt(last) + 1);
        $("#txtNewOrderId").val(newValue);
    }
}

function setDate() {
    let date = new Date().toLocaleDateString("fr-FR");
    $('#txtNewOrderDate').val(date);
}

function loadItemCodes() {
    $('#txtNewOrderItemCode').empty();
    $("#txtNewOrderItemCode").append(`<option disabled selected hidden>select item code</option>`);
    for (let i of itemDB) {
        let data = `<option >${i.getItemCode()}</option>`;
        $("#txtNewOrderItemCode").append(data);
    }
}

function calculateTotal() {
    let total=0;
    $("#tblNewOrderCart tr").each(function() {
            let rowTotal = parseInt(this.cells[3].innerHTML);
            total+=rowTotal;

    });
    $('#txtNewOrderTotal').val(total);
}

function addCart(item,unitPrice,qty,total,btn) {
    let not=true;
    let currentQty;
    let removeRow;
    $("#tblNewOrderCart tr").each(function() {
        if(this.cells[0].innerHTML===item) {
            not=false;
            currentQty=parseInt(this.cells[2].innerHTML);
            removeRow=$(this);
        }
    });
    let newQty=currentQty+parseInt(qty);
    let newTot=newQty*unitPrice;
    let qtyOnHand=parseInt($('#txtNewOrderItemQtyOnH').val());
    if (qty>qtyOnHand||newQty>qtyOnHand){
        alert("not enough qty");
    }else {
        if (not){
            let row = `<tr><td>${item}</td><td>${unitPrice}</td><td>${qty}</td><td>${total}</td><td>${btn}</td></tr>`;
            $("#tblNewOrderCart").append(row);
        }else {
            removeRow.remove();
            let row = `<tr><td>${item}</td><td>${unitPrice}</td><td>${newQty}</td><td>${newTot}</td><td>${btn}</td></tr>`;
            $("#tblNewOrderCart").append(row);
        }
    }
}

function clearSelection() {
    $('#txtNewOrderItemCode').val("");
    $('#txtNewOrderItemUnitPrice').val("");
    $('#txtNewOrderItemDescription').val("");
    $('#txtNewOrderItemQtyOnH').val("");
    $('#txtNewOrderOrderedQty').val("");
    loadItemCodes();
}
function cancelOrder() {
    clearSelection();
    setDate();setNewOrderId();
    $('#btnProceedOrder').attr('disabled', true);
    $('#txtNewOrderTotal').val("");
    $('#txtNewOrderDiscount').val("");
    $('#txtNewOrderNetTotal').val("");
    $("#txtNewOrdercusId").val("");
    $("#txtNewOrdercusName").val("");
    $("#txtNewOrdercusAddress").val("");
    $("#txtNewOrdercusContact").val("");
    $("#tblNewOrderCart").empty();
}

function updateQty(itemCode,qty) {
    let buyQty=parseInt(qty);
    for (let i = 0; i < itemDB.length; i++) {
        if (itemDB[i].getItemCode()===itemCode) {
            let current =parseInt(itemDB[i].getQtyOnHand());
            let newQty=current-buyQty;
            itemDB[i].setQtyOnHand(newQty);
        }
    }
}