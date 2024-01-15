const recommend_elements = document.querySelectorAll(".recommend");
Array.from(recommend_elements).forEach(function(element){
    element.addEventListener('click',function(){
        if(confirm("정말로 추천하시겠습니까?")){
            location.href=this.dataset.uri;
        }
    })
})