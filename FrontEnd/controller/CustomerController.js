$("#customersLink").click(function () {
    loadAllCustomers();
    $('#btnUpdateCustomer').attr('disabled', true);
    $('#btnRemoveCustomer').attr('disabled', true);
});

$('#btnSaveCustomer').click(function () {
    saveCustomer("customerForm1");
});

$("#btnUpdateCustomer").click(function () {
    updateCustomer();
});

$("#btnRemoveCustomer").click(function () {
    removeCustomer();
});

$('#btnClearCustomerFields').click(function () {
    clearAll();
});

$('#btnSearchCustomer').click(function () {
    searchCustomer();
});

function saveCustomer(formId) {
    let form="#"+formId;
    let formData = $(form).serialize();
    let decision = confirm("Do you want to add this Customer..?");
    if (decision){
        $.ajax({
            url:"http://localhost:8081/backEnd/customer",
            method:"POST",
            data:formData,
            success:function (response) {
                if (response.status==200){
                    alert(response.message);
                    clearAll();
                }else {
                    alert(response.message);
                }
            }
        });
    }

}//done

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

function removeCustomer() {
    let removeId = $("#txtCustomerId").val();
    let decision = confirm("Do you want to remove this Customer..?");
    if (decision) {
        $.ajax({
            url: "http://localhost:8081/backEnd/customer?customerId="+removeId,
            method: "DELETE",
            success: function (res) {
                if (res.status == 200) {
                    alert(res.message);
                    clearAll()
                } else {
                    alert(res.message);
                }
            },
        });
    }
}//done

function updateCustomer() {
    let condition = confirm("Do you want to save changes..?");
    if (condition) {
        let customer={
            id:$("#txtCustomerId").val(),
            name:$("#txtCustomerName").val(),
            address:$("#txtCustomerAddress").val(),
            contactNo:$("#txtCustomerContact").val()
        }
        $.ajax({
            url:"http://localhost:8081/backEnd/customer",
            method:"PUT",
            data: JSON.stringify(customer),
            contentType:"application/json",
            success:function (response) {
                if (response.status==200){
                    alert(response.message);
                    clearAll();
                }else {
                    alert(response.message);
                }
            }
        });
    }
}//done


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

}//done

function clearAll() {
    $('#txtCustomerId,#txtCustomerName,#txtCustomerAddress,#txtCustomerContact,#txtSearchCustomer').val("");
    $("#txtCustomerId").attr('readonly', false);
    $('#btnUpdateCustomer').attr('disabled', true);
    $('#btnRemoveCustomer').attr('disabled', true);
    loadAllCustomers();

}//done

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
}//done


function isCustomerExist(id) {
    for (let i = 0; i < customerDB.length; i++) {
        return customerDB[i].getCustomerId() === id;
    }
}