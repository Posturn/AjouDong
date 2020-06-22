addClubAccountElement = document.getElementById("add-club-account")
deleteClubAccountElement = document.getElementById("delete-club-account")
addClubElement = document.getElementById("add-club")
deleteClubElement = document.getElementById("delete-club")
ClubAccountListElement = document.getElementById("club-account-list")
ClubListElement = document.getElementById("club-list")
InvisibleDiv()
addClubAccountElement.style.display="block"

function ShowAddClubAccount(){
    InvisibleDiv()
    addClubAccountElement.style.display="block"
}

function ShowDeleteClubAccount(){
    InvisibleDiv()
    deleteClubAccountElement.style.display="block"
}

function ShowAddClub(){
    InvisibleDiv()
    addClubElement.style.display="block"
}

function ShowDeleteClub(){
    InvisibleDiv()
    deleteClubElement.style.display="block"
}

function ShowClubAccountList(){
    InvisibleDiv()
    ClubAccountListElement.style.display="block"
}

function ShowClubList(){
    InvisibleDiv()
    ClubListElement.style.display="block"
}

function InvisibleDiv(){
    addClubAccountElement.style.display="none"
    deleteClubAccountElement.style.display="none"
    addClubElement.style.display="none"
    deleteClubElement.style.display="none"
    ClubAccountListElement.style.display="none"
    ClubListElement.style.display="none"
}