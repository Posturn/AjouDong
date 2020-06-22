addAdsElement = document.getElementById("add-ads")
deleteAdsElement = document.getElementById("delete-ads")
updateAdsElement = document.getElementById("update-ads")
listAdsElement = document.getElementById("list-ads")
InvisibleDiv()
listAdsElement.style.display="block"

function ShowAddAds(){
    InvisibleDiv()
    addAdsElement.style.display="block"
}

function ShowDeleteAds(){
    InvisibleDiv()
    deleteAdsElement.style.display="block"
}

function ShowUpdateAds(){
    InvisibleDiv()
    updateAdsElement.style.display="block"
}

function ShowAdsList(){
    InvisibleDiv()
    listAdsElement.style.display="block"
}

function InvisibleDiv(){
    addAdsElement.style.display="none"
    deleteAdsElement.style.display="none"
    updateAdsElement.style.display="none"
    listAdsElement.style.display="none"
}