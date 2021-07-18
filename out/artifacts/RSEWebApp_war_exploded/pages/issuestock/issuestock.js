function resetForm() {
    $("#symbol").val('');
    $("#comapny-name").val('');
    $("#amount").val('');
    $("#company-worth").val('Buying');
}

$(function() { // onload...do
    //add a function to the submit event
    $("#issue-stock-form").submit(function() {
        $.ajax({
            type: "POST",
            dataType: "json",
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function(errorObject) {
                var errorJSON = errorObject.responseJSON;

                alert("Error status " + errorJSON.status + ": " + errorJSON.message);
            },
            success: function(successMessage) {
                console.log(successMessage)
                alert(successMessage.message);
                resetForm();
            }
        });
        return false;
    });
});