
window.addEventListener('load', async() => {
    let res = await fetch(`http://localhost:8080/checkloginstatus`,{
        method: 'GET',    
        credentials: 'include'

    });

    if(res.status===200){
        let userObj = await res.json();

        if(userObj.role==='employee'){
            window.location.href = 'employee-homepage.html';
        }else if(userObj.role==='manager'){
            window.location.href = 'main-manager-page.html';
        }
    }
    
});

let loginButton = document.querySelector('#loginBtn');

loginButton.addEventListener('click', loginButtonClicked);

function loginButtonClicked(e){
    e.preventDefault();
    login();
}


async function login(){
    let usernameInput = document.querySelector('#uName');
    let passwordInput = document.querySelector('#password');

    console.log(passwordInput);

    try{
        let res = await fetch(`http://localhost:8080/login`,{
            method:'POST',
            credentials:'include',
            body: JSON.stringify({
                username: usernameInput.value,
                password: passwordInput.value
            })
        });
        console.log(usernameInput.value);
        console.log(passwordInput.value);

        let data = await res.json();

        console.log(data);

        if(res.status===400){
            let loginErrorMessage = document.createElement('p');
            let loginDiv = document.querySelector('#login-error');
            loginDiv.innerHTML = '';
            loginErrorMessage.innerHTML = data.message;
            loginErrorMessage.style.color = 'red';
            loginDiv.appendChild(loginErrorMessage); 
            
        }

        if(res.status===200){
            console.log(data);
            if(data.role==='employee'){
                window.location.href = 'employee-homepage.html';
            }else if(data.role==='manager'){
                window.location.href = 'main-manager-page.html';
            }
            
        }
    }catch(err){
        console.log(err);
    }
}

let createAccountButton = document.querySelector('#signupBtn');

createAccountButton.addEventListener('click', accountButtonClicked);

function accountButtonClicked(e){
    e.preventDefault();
    signup();
}

async function signup(){
    let usernameInput = document.querySelector('#userName');
    let passwordInput = document.querySelector('#pswrd');
    let firstNameInput = document.querySelector('#Firstname');
    let lastNameInput = document.querySelector('#Lastname');
    let emailInput = document.querySelector('#email');
    let roleInput = document.querySelector('#role');

    let formData = new FormData();

    formData.append('Username',usernameInput.value);
    formData.append('Password',passwordInput.value);
    formData.append('First_Name',firstNameInput.value);
    formData.append('Last_Name',lastNameInput.value);
    formData.append('Email',emailInput.value);
    formData.append('Role',roleInput.value);



    try{
        let res = await fetch(`http://localhost:8080/addUser`,{
            method:'POST',
            credentials:'include',
            body: formData
            })

        let data = await res.json();

        console.log(data);

        if(res.status===400){
            let signupErrorMessage = document.createElement('p');
            let signupDiv = document.querySelector('#login-error');
            signupErrorMessage.innerHTML = data.message;
            signupErrorMessage.style.color = 'red';
            signupDiv.appendChild(signupErrorMessage);   
        }

        if(res.status===200){
            console.log(data);
            let messageElement = document.createElement('p');
            let messageDiv = document.querySelector('#message-div');
            messageElement.innerHTML = data.message;
            messageElement.style.color = 'red';
            messageDiv.appendChild(messageElement);       
        } 
    }catch(err){
        console.log(err);
    }
}
