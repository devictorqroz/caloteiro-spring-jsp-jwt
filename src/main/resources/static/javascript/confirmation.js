
$(document).ready(function() {
    $('.delete-caloteiro-button').click(function(event) {
        var result = confirm("Tem certeza de que deseja excluir este caloteiro?");
        if (!result) {
            event.preventDefault();
        }
    });

    $('.delete-user-button').click(function(event) {
        var result = confirm("Tem certeza de que deseja excluir sua conta?");
        if (!result) {
            event.preventDefault();
        }
    });
});
