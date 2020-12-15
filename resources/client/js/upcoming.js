function pageLoad(){
    function gamesList() {
        //debugger;
        console.log("Invoked gamesList()");		//console.log your BFF for debugging client side
        const url = "/games/list/";	// API method on webserver will be in Users class with method list
        fetch(url, {
            method: "GET",
        }).then(response => {
            return response.json();                 //return response to JSON
        }).then(response => {
            if (response.hasOwnProperty("Error")) { //checks if response from server has a key "Error"
                alert(JSON.stringify(response));    // if it does, convert JSON object to string and alert
            } else {
                formatGamesList(response);          //this function will create an HTML table of the data
            }
        });
    }
}


function gamesList() {
    //debugger;
    console.log("Invoked gamesList()");		//console.log your BFF for debugging client side
    const url = "/games/list/";	// API method on webserver will be in Users class with method list
    fetch(url, {
        method: "GET",
    }).then(response => {
        return response.json();                 //return response to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) { //checks if response from server has a key "Error"
            alert(JSON.stringify(response));    // if it does, convert JSON object to string and alert
        } else {
            formatGamesList(response);          //this function will create an HTML table of the data
        }
    });
}
//fetch() GET
function formatGamesList(myJSONArray) {
    let dataHTML = "";
    for (let item of myJSONArray) {
        dataHTML += "<tr><td>" + item.GameDescription + "</td><td>" + item.GameDate + "</td></tr>";

        document.getElementById("GamesTable").innerHTML = dataHTML;
    }
}