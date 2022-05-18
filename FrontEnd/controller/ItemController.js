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
    let item = searchItem($("#txtSearchItem").val());
    if (item) {
        $("#txtItemCode").val(item.getItemCode());
        $("#txtItemDescription").val(item.getDescription());
        $("#txtUnitPrice").val(item.getUnitPrice());
        $("#txtQtyOnHand").val(item.getQtyOnHand());
        $("#txtItemCode").attr('readonly', true);
        $('#btnUpdateItem').attr('disabled', false);
        $('#btnRemoveItem').attr('disabled', false);
    } else {
        clearItemFields();
        alert("No Such a Item");
    }
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

function searchItem(code) {
    for (let i = 0; i < itemDB.length; i++) {
        if (itemDB[i].getItemCode() === code) {
            return itemDB[i];
        }
    }
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
        $("#itemTable").append(row);
    }

    $("#itemTable>tr").click(function () {
        let itemCode = $(this).closest("tr").find("td:eq(0)").text();
        let desc = $(this).closest("tr").find("td:eq(1)").text();
        let unitPrice = $(this).closest("tr").find("td:eq(2)").text();
        let qtyOnHand = $(this).closest("tr").find("td:eq(3)").text();

        $("#txtItemCode").val(itemCode);
        $("#txtItemDescription").val(desc);
        $("#txtUnitPrice").val(unitPrice);
        $("#txtQtyOnHand").val(qtyOnHand);
        $("#txtItemCode").attr('readonly', true);
        $('#btnUpdateItem').attr('disabled', false);
        $('#btnRemoveItem').attr('disabled', false);
    });
}

function clearItemFields() {
    $('#txtItemCode,#txtItemDescription,#txtUnitPrice,#txtQtyOnHand,#txtSearchItem').val("");
    $("#txtItemCode").attr('readonly', false);
    $('#btnUpdateItem').attr('disabled', true);
    $('#btnRemoveItem').attr('disabled', true);
    loadAllItems();
}