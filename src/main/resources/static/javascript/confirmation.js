
$(document).ready(function() {
    $('#deleteCaloteiroButton').click(function(event) {
        var result = confirm("Tem certeza de que deseja excluir este caloteiro?");
        if (!result) {
            event.preventDefault();
        }
    });

    $('#deleteUserButton').click(function(event) {
        var result = confirm("Tem certeza de que deseja excluir sua conta?");
        if (!result) {
            event.preventDefault();
        }
    });
});
