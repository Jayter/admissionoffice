function showAuthErr(arg){
    window.alert(arg);
}

function showAndRedirect(message, path) {
    window.alert(message);
    window.location.href = path;
}

function startTimer() {
    var my_timer = document.getElementById("my_timer");
    var s = my_timer.innerHTML;
    if (s == 0) {
        window.location.href = 'Controller?command=load-main';
    }
    else {
        s--;
        document.getElementById("my_timer").innerHTML = s;
        setTimeout(startTimer, 1000);
    }
}