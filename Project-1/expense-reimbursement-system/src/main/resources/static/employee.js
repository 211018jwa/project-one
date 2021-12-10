// Initial page load
window.addEventListener('load', async () => {

    let res = await fetch('http://localhost:8080/checkloginstatus', {
        credentials: 'include',
        method: 'GET'
    });

    if (res.status === 200) {
        let userObj = await res.json();

        if (userObj.userRole === 'manager') {
            window.location.href = 'manager-homepage.html';
        }
        let fnLn = document.querySelector('#fnLn');
      fnLn.innerHTML = `Welcome ${userObj.firstName} ${userObj.lastName}!!!!`; 
    } else if (res.status === 401) {
        window.location.href = 'index.html';
    }

    populateTableWithReimbursement();
});


// Logout btn
let logoutBtn = document.querySelector('#logout');

logoutBtn.addEventListener('click', async () => {

    let res = await fetch('http://localhost:8080/logout', {
        'method': 'POST',
        'credentials': 'include'
    });

    if (res.status === 200) {
        window.location.href = 'index.html';
    }

})


async function populateTableWithReimbursement() {
    let res = await fetch('http://localhost:8080/reimbursement', {
        credentials: 'include',
        method: 'GET'
    });

    let tbodyElement = document.querySelector("#reimb_table tbody");
  tbodyElement.innerHTML = "";

  let reimbArray = await res.json();
  console.log("reimbArray>>",reimbArray)

  for (let i = 0; i < reimbArray.length; i++) {
    let reimbursement = reimbArray[i];

    let tr = document.createElement("tr");

    let td1 = document.createElement("td");
    td1.innerHTML = reimbursement.id;

    let td2 = document.createElement("td");
    td2.innerHTML = reimbursement.amount;

    let td3 = document.createElement("td");
    td3.innerHTML =new Date(reimbursement.submitted).toLocaleDateString('en-US');

    let td4 = document.createElement("td");
    td4.innerHTML = reimbursement.resolved;

    let td5 = document.createElement("td");
    td5.innerHTML = reimbursement.description;

    let td6 = document.createElement("td");
    td6.innerHTML = reimbursement.authorId;

    let td7 = document.createElement("td"); //resolverId

    let td8 = document.createElement("td"); // status

    let td9 = document.createElement("td");
    td9.innerHTML = reimbursement.reimType;

    let td10 = document.createElement("td"); 
    //let td11 = document.createElement("td");
    //let td12 = document.createElement("td");

    if(reimbursement.resolverId !=null && reimbursement.resolverId!=0){
        td8.innerHTML = reimbursement.status;
        td7.innerHTML = reimbursement.resolverId;
       } else {
       td8.innerHTML = ' ';
       }

    let viewImageButton = document.createElement('button');
    viewImageButton.innerHTML = 'View Receipt';

    td10.appendChild(viewImageButton);

    viewImageButton.addEventListener("click", () => {
      let reimbImageModal = document.querySelector('#reimb-image-modal');

      let modalCloseElement = reimbImageModal.querySelector('button');
      modalCloseElement.addEventListener('click', () =>{
          reimbImageModal.classList.remove('is-active');
      });

      let modalContentElement = reimbImageModal.querySelector('.modal-content');
      modalContentElement.innerHTML = '';

      let imageElement = document.createElement("img");
      imageElement.setAttribute(
        "src",
        `http://localhost:8080/reimbursement/${reimbursement.id}/image`
      );
      modalContentElement.appendChild(imageElement);

      reimbImageModal.classList.add("is-active");
    })
  
            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            tr.appendChild(td5);
            tr.appendChild(td6);
            tr.appendChild(td7);
            tr.appendChild(td8);
            tr.appendChild(td9);
            tr.appendChild(td10);

        tbodyElement.appendChild(tr);
    }
}

// Submitting assignment
let reimbursementSubmitButton = document.querySelector('#submit-reimbursement-btn');

reimbursementSubmitButton.addEventListener('click', submitReimbursement);

async function submitReimbursement() {

    let reimbursementAmountInput = document.querySelector('#amount');
    let reimbursementImageInput = document.querySelector('#reimbursement-file');
    let reimbursementDescInput = document.querySelector('#description');
    let reimbursementTypeInput = document.querySelector('#reimbType')

    const file = reimbursementImageInput.files[0];
    //console.log("image>>>", file);
   

    let formData = new FormData();
    formData.append('amount', reimbursementAmountInput.value);
    formData.append('reimbType', reimbursementTypeInput.value);
    formData.append('reimb_receipt', file);
    formData.append('description', reimbursementDescInput.value);


    console.log("image>>>", file);
    console.log("amount>>",reimbursementAmountInput.value);
    console.log("type>>",reimbursementTypeInput.value);
    console.log("desc>>",reimbursementDescInput.value);



    
    


    let res = await fetch('http://localhost:8080/reimbursement', {
        method: 'POST',
        credentials: 'include',
        body: formData
    });

    if (res.status === 201) { // If we successfully added an assignment
        populateTableWithReimbursement(); // Refresh the table of assignments
    } else if (res.status === 400) {
        let reimbursementForm = document.querySelector('#reimbursement-submit-form');

        let data = await res.json();

        let pTag = document.createElement('p');
        pTag.innerHTML = data.message;
        pTag.style.color = 'red';

        reimbursementForm.appendChild(pTag);
    }
}