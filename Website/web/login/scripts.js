function login() {
    let token = '2241626F6C66617A6C416C697A616465683132333422';
    let username = document.getElementById('username').value;
    let password = document.getElementById('password').value;

    $.post('../../api/users/accounts.php?token=' + token + "&request=login",
        { 'username': username, 'password': password }).done(function (data) {
            let info = JSON.parse(data);
            if (info['data']['status'] === false)
                alert(info['data']['msg']);
        });
}