$("#itemsLink").click(function () {
    loadAllItems()
    $('#btnUpdateItem').attr('disabled', true);
    $('#btnRemoveItem').attr('disabled', true);
});

$('#btnSaveItem').click(function () {
    saveItem();
});

$("#btnUpdateItem").click(function () {
    updateItem();
});

$("#btnRemoveItem").click(function () {
    removeItem();
});

$('#btnClearItemFields').click(function () {
    clearItemFields();
});

$('#btnSearchItem').click(function () {
    searchItem();
});

function saveItem() {
    let formData = $("#itemForm").serialize();
    let decision = confirm("Do you want to add this Item..?");
    if (decision){
        $.ajax({
            url:"http://localhost:8081/backEnd/item",
            method:"POST",
            data:formData,
            success:function (response) {
                if (response.status==200){
                    alert(response.message);
                    clearItemFields();
                }else {
                    alert(response.message);
                }
            }
        });
    }
}//done

function searchItem() {
    let code = $("#txtSearchItem").val();
    $.ajax({
        url: "http://localhost:8081/backEnd/item?option=SEARCH&itemCode=" + code,
        method: "GET",
        success: function (response) {
            if (response.status == 400) {
                alert(response.message);
            } else {
                const item = response.data;
                let code = item.code;
                let description = item.description;
                let unitPrice = item.unitPrice;
                let qtyOnHand = item.qtyOnHand;
                $("#txtItemCode").val(code);
                $("#txtItemDescription").val(description);
                $("#txtUnitPrice").val(unitPrice);
                $("#txtQtyOnHand").val(qtyOnHand);
                $("#txtItemCode").attr('readonly', true);
                $('#btnUpdateItem').attr('disabled', false);
                $('#btnRemoveItem').attr('disabled', false);
            }
        }

    });
}//done

function removeItem() {
    let removeCode = $("#txtItemCode").val();
    let decision = confirm("Do you want to remove this Item..?");
    if (decision) {
        $.ajax({
            url: "http://localhost:8081/backEnd/item?code="+removeCode,
            method: "DELETE",
            success: function (res) {
                if (res.status == 200) {
                    alert(res.message);
                    clearItemFields()
                } else {
                    alert(res.message);
                }
            },
        });
    }
}//done

function updateItem() {
    let condition = confirm("Do you want to save changes..?");
    if (condition) {
        let item={
            code:$("#txtItemCode").val(),
            description:$("#txtItemDescription").val(),
            unitPrice:$("#txtUnitPrice").val(),
            qtyOnHand:$("#txtQtyOnHand").val()
        }
        $.ajax({
            url:"http://localhost:8081/backEnd/item",
            method:"PUT",
            data: JSON.stringify(item),
            contentType:"application/json",
            success:function (response) {
                if (response.status==200){
                    alert(response.message);
                    clearItemFields();
                }else {
                    alert(response.message);
                }
            }
        });
    }
}//done

// function isItemExist(code) {
//     for (let i = 0; i < itemDB.length; i++) {
//         return itemDB[i].getItemCode() === code;
//     }
// }

function loadAllItems() {
    $("#itemTable").empty();
    $.ajax({
        url: "http://localhost:8081/backEnd/item?option=GET_ALL",
        method: "GET",
        success: function (response) {
            if (response.status == 201) {
                for (const item of response.data) {
                    let row = `<tr><td>${item.code}</td><td>${item.description}</td><td>${item.unitPrice}</td><td>${item.qtyOnHand}</td></tr>`;
                    $("#itemTable").append(row);
                }
                bindTableClickEvent();
            } else {
                alert("Problem while loading items")
            }
        }
    });
}//done

function clearItemFields() {
    $('#txtItemCode,#txtItemDescription,#txtUnitPrice,#txtQtyOnHand,#txtSearchItem').val("");
    $("#txtItemCode").attr('readonly', false);
    $('#btnUpdateItem').attr('disabled', true);
    $('#btnRemoveItem').attr('disabled', true);
    loadAllItems();
}//done

function bindTableClickEvent() {
    $("#itemTable>tr").click(function () {
        let itemCode = $(this).children().eq(0).text();
        let desc = $(this).children().eq(1).text();
        let unitPrice = $(this).children().eq(2).text();
        let qtyOnHand = $(this).children().eq(3).text();

        $("#txtItemCode").val(itemCode);
        $("#txtItemDescription").val(desc);
        $("#txtUnitPrice").val(unitPrice);
        $("#txtQtyOnHand").val(qtyOnHand);
        $("#txtItemCode").attr('readonly', true);
        $('#btnUpdateItem').attr('disabled', false);
        $('#btnRemoveItem').attr('disabled', false);
    });
}//done