const page_elements = document.getElementsByClassName("page-link");
Array.from(page_elements).forEach(function(element){
    element.addEventListener('click',function(){
        document.getElementById('page').value = this.dataset.page;
        document.getElementById('searchForm').submit();
    });
});

const search_btn = document.getElementById("search_btn");
search_btn.addEventListener('click',function(){
    document.getElementById('keyword').value = document.getElementById('search_kw').value;
    document.getElementById('page').value = 0;
    document.getElementById('searchForm').submit();
});