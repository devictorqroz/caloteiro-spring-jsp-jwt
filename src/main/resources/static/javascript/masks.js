$(document).ready(function() {
	
	$('#labelDebt').maskMoney();
	$('#labelDate').inputmask("99/99/9999");

    const caloteiroValidationOptions = {
        rules: {
            name: {
                required: true,
                maxlength: 50
            },
            email: {
                required: true,
                email: true
            },
            debt: {
                required: true
            },
            debtDate: {
                required: true
            }
        },
        messages: {
            name: {
                required: "O campo nome é obrigatório.",
                maxlength: "O nome não pode ter mais que 50 caracteres"
            },
            email: {
                required: "O campo email é obrigatório.",
                email: "Por favor, insira um email válido."
            },
            debt: {
                required: "O campo de dívida é obrigatório."
            },
            debtDate: {
                required: "A data da dívida é obrigatória."
            }
        }
    };

	$('#formNewCaloteiro').validate(caloteiroValidationOptions);
	$('#formUpdateCaloteiro').validate(caloteiroValidationOptions);

	$('#formRegister').validate({
        rules: {
            name: {
                required: true,
                minlength: 3,
                maxlength: 30
            },
            email: {
                required: true,
                email: true
            },
            password: {
                required: true,
                minlength: 6
            },
            confirmPassword: {
                required: true,
                minlength: 6,
                equalTo: "#labelPassword"
            }
        },
        messages: {
            name: {
                required: "O nome é obrigatório.",
                minlength: "O nome deve ter no mínimo 3 caracteres."
            },
            email: {
                required: "O email é obrigatório",
                email: "Por favor, insira um email válido."
            },
            password: {
                required: "A senha é obrigatória.",
                minlength: "A senha deve ter no mínimo 6 caracteres."
            },
            confirmPassword: {
                required: "A confirmação da senha é obrigatória.",
                minlength: "A confirmação deve ter no mínimo 6 caracteres.",
                equalTo: "As senhas não coincidem."
            }
        }
    });

	$('#formLogin').validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: {
                required: true
            }
        },
        messages: {
            email: {
                required: "O email é obrigatório.",
                email: "Por favor, insira um email válido."
            },
            password: {
                required: "A senha é obrigatória."
            }
        }
    });

    $('#deleteUserForm').validate({
        rules: {
            password: {
                required: true
            }
        },
        messages: {
            password: {
                required: "A senha é obrigatória para excluir a conta."
            }
        }
    });
});
