async function populateTableWithReimbursement() {
    let res = await fetch("http://localhost:8080/reimbursement", {
      credentials: "include",
      method: "GET",
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
      td3.innerHTML = reimbursement.submitted;
  
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
      let td11 = document.createElement("td");
      let td12 = document.createElement("td");
  
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
  
       if(reimbursement.resolverId !=null && reimbursement.resolverId!=0){
           td8.innerHTML = reimbursement.status;
           td7.innerHTML = reimbursement.resolverId;
          } else {
          td8.innerHTML = ' ';
          let items = [
                  { id: "reApprove", name: "Approve" },
                  { id: "reDeny", name: "Deny" },
                ];
                
                items.forEach((item) => {
                  let radioSelect = document.createElement("input");
                  radioSelect.type = "radio";
                  radioSelect.id = item.id;
                  radioSelect.value = item.id;
                  radioSelect.name="reApprovalStatus"
              
                  let label = document.createElement("label");
                  label.htmlFor = item.id;
              
              
                  let description = document.createTextNode(item.name);
                  label.appendChild(description);
              
                  let newline = document.createElement("br");
              
                  let container = document.getElementById("reimb_table");
                  container.appendChild(radioSelect);
                  container.appendChild(label);
                  container.appendChild(newline);
                  td11.appendChild(radioSelect);
                  td11.appendChild(description);
                });
              
                let submitBtn = document.createElement('button');
                submitBtn.innerText = 'Submit';
                td12.appendChild(submitBtn);
                submitBtn.addEventListener('click',async ()=>{
                    if(document.getElementById( "reApprove").checked){
                      let res = await fetch(`http://localhost:8080/reimbursement/${reimbursement.id}/status`,{
                          method:'PATCH',
                          credentials:'include',
                          body:JSON.stringify({
                              status:'Approve'
                          })
                      })
              
                    }else if(document.getElementById("reDeny").checked){
                      let res = await fetch(`http://localhost:8080/reimbursement/${reimbursement.id}/status`,{
                          method:'PATCH',
                          credentials:'include',
                          body:JSON.stringify({
                              status:'Denied'
                          })
                      })
              
                    }else{
                        console.log("no option selected")
                    }
                })
  
         }
  
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
      tr.appendChild(td11);
      tr.appendChild(td12);
      
  
      tbodyElement.appendChild(tr);
    }
    
  }