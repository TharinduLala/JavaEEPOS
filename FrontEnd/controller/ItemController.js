$("#itemsLink").click(function () {
    loadAllItems()
    $('#btnUpdateItem').attr('disabled', true);
    $('#btnRemoveItem').attr('disabled', true);
});

$('#btnSaveItem').click(function () {
    saveItem();
});

$('#btnUpdateItem').click(function () {
    let updateCode = $("#txtItemCode").val();
    updateItem(updateCode);
});

$('#btnRemoveItem').click(function () {
    let removeId = $("#txtItemCode").val();
    removeItem(removeId);
});

$('#btnClearItemFields').click(function () {
    clearItemFields();
});

$('#btnSearchItem').click(function () {
    searchItem();
});

function saveItem() {
    let itemCode = $("#txtItemCode").val();
    if (isItemExist(itemCode)){
        alert("This Item code already exist..");
    }else {
        let response = confirm("Do you want to add this Item..?");
        if (response) {
            let desc = $("#txtItemDescription").val();
            let unitPrice = $("#txtUnitPrice").val();
            let qtyOnHand = $("#txtQtyOnHand").val();

            let itemDto = new ItemDto(itemCode,desc,unitPrice,qtyOnHand);

            itemDB.push(itemDto);
            clearItemFields();
        }
    }
}

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
}

function removeItem(removeCode) {
    let response = confirm("Do you want to remove this Item..?");
    if (response) {
        for (let i = 0; i < itemDB.length; i++) {
            if (itemDB[i].getItemCode() === removeCode) {
                itemDB.splice(i, 1);
            }
        }
        loadAllItems();
        clearItemFields();
    }
}

function updateItem(updateCode) {
    let res = confirm("Need to save changes..?");
    if (res) {
        let itemCode = $("#txtItemCode").val();
        let desc = $("#txtItemDescription").val();
        let unitPrice = $("#txtUnitPrice").val();
        let qtyOnHand = $("#txtQtyOnHand").val();

        let itemDto = new ItemDto(itemCode,desc,unitPrice,qtyOnHand);

        for (let i = 0; i < itemDB.length; i++) {
            if (itemDB[i].getItemCode()===updateCode) {
                itemDB[i]=itemDto;
            }
        }
        loadAllItems();
        clearItemFields();
    }
}

function isItemExist(code) {
    for (let i = 0; i < itemDB.length; i++) {
        return itemDB[i].getItemCode() === code;
    }
}

function loadAllItems() {
    $("#itemTable").empty();
    for (let i of itemDB) {
        let row = `<tr><td>${i.getItemCode()}</td><td>${i.getDescription()}</td><td>${i.getUnitPrice()}</td><td>${i.getQtyOnHand()}</td></tr>`;

    }
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
}

function clearItemFields() {
    $('#txtItemCode,#txtItemDescription,#txtUnitPrice,#txtQtyOnHand,#txtSearchItem').val("");
    $("#txtItemCode").attr('readonly', false);
    $('#btnUpdateItem').attr('disabled', true);
    $('#btnRemoveItem').attr('disabled', true);
    loadAllItems();
}
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
}