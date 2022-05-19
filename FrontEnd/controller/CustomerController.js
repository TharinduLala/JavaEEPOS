$("#customersLink").click(function () {
    loadAllCustomers();
    $('#btnUpdateCustomer').attr('disabled', true);
    $('#btnRemoveCustomer').attr('disabled', true);
});

$('#btnSaveCustomer').click(function () {
    saveCustomer();
});

$('#btnUpdateCustomer').click(function () {
    let updateId = $("#txtCustomerId").val();
    updateCustomer(updateId);
});

$('#btnRemoveCustomer').click(function () {
    let removeId = $("#txtCustomerId").val();
    removeCustomer(removeId);
});

$('#btnClearCustomerFields').click(function () {
    clearAll();
});

$('#btnSearchCustomer').click(function () {
    searchCustomer();
});

function saveCustomer() {
    let customerID = $("#txtCustomerId").val();
    if (isCustomerExist(customerID)) {
        alert("This customer id already exist..");
    } else {
        let response = confirm("Do you want to add this Customer..?");
        if (response) {
            let customerName = $("#txtCustomerName").val();
            let customerAddress = $("#txtCustomerAddress").val();
            let customerContact = $("#txtCustomerContact").val();

            let customerDto = new CustomerDto(customerID, customerName, customerAddress, customerContact);

            customerDB.push(customerDto);
            clearAll();
        }
    }
}

function searchCustomer() {
    let cId = $("#txtSearchCustomer").val();
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
                $("#txtCustomerId").val(id);
                $("#txtCustomerName").val(name);
                $("#txtCustomerAddress").val(address);
                $("#txtCustomerContact").val(contact);
                $("#txtCustomerId").attr('readonly', true);
                $('#btnUpdateCustomer').attr('disabled', false);
                $('#btnRemoveCustomer').attr('disabled', false);
            }
        }

    });
}//Done

function removeCustomer(removeId) {
    let response = confirm("Do you want to remove this Customer..?");
    if (response) {
        for (let i = 0; i < customerDB.length; i++) {
            if (customerDB[i].getCustomerId() === removeId) {
                customerDB.splice(i, 1);
            }
        }
        loadAllCustomers();
        clearAll();
    }
}

function updateCustomer(updateId) {
    let response = confirm("Do you want to save changes..?");
    if (response) {
        let customerID = $("#txtCustomerId").val();
        let customerName = $("#txtCustomerName").val();
        let customerAddress = $("#txtCustomerAddress").val();
        let customerContact = $("#txtCustomerContact").val();

        let customerDto = new CustomerDto(customerID, customerName, customerAddress, customerContact);

        for (let i = 0; i < customerDB.length; i++) {
            if (customerDB[i].getCustomerId() === updateId) {
                customerDB[i] = customerDto;
            }
        }
        loadAllCustomers();
        clearAll();
    }
}

function isCustomerExist(id) {
    for (let i = 0; i < customerDB.length; i++) {
        return customerDB[i].getCustomerId() === id;
    }
}

function loadAllCustomers() {
    $("#customerTable").empty();
    $.ajax({
        url: "http://localhost:8081/backEnd/customer?option=GET_ALL",
        method: "GET",
        success: function (response) {
            if (response.status == 201) {
                for (const customer of response.data) {
                    let row = `<tr><td>${customer.id}</td><td>${customer.name}</td><td>${customer.address}</td><td>${customer.contact}</td></tr>`;
                    $("#customerTable").append(row);
                }
                bindTableClickEvent();
            } else {
                alert("Problem while loading customers")
            }
        }
    });

}

function clearAll() {
    $('#txtCustomerId,#txtCustomerName,#txtCustomerAddress,#txtCustomerContact,#txtSearchCustomer').val("");
    $("#txtCustomerId").attr('readonly', false);
    loadAllCustomers();
    $('#btnUpdateCustomer').attr('disabled', true);
    $('#btnRemoveCustomer').attr('disabled', true);

}

function bindTableClickEvent() {
    $("#customerTable>tr").click(function () {
        let id = $(this).children().eq(0).text();
        let name = $(this).children().eq(1).text();
        let address = $(this).children().eq(2).text();
        let contact = $(this).children().eq(3).text();

        $('#txtCustomerId').val(id);
        $('#txtCustomerName').val(name);
        $('#txtCustomerAddress').val(address);
        $('#txtCustomerContact').val(contact);
        $("#txtCustomerId").attr('readonly', true);
        $('#btnUpdateCustomer').attr('disabled', false);
        $('#btnRemoveCustomer').attr('disabled', false);
    });
}