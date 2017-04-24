
var searchExample = document.getElementById('example');
var searchInput = document.getElementById("query");

searchExample.addEventListener("click", setSearch, false);

function setSearch(e) {
    searchInput.value = e.target.innerHTML;
}