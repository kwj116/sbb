const appear_btns = document.getElementsByClassName("appear_btn");
const comment_btn = document.getElementsByClassName("comment_btn");
Array.from(appear_btns).forEach(function(element){
    element.addEventListener('click',function(){
        Array.from(comment_btn).forEach(function(){
            console.log(this.id);
        })
    })
})

