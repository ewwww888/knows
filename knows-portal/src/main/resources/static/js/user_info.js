let userApp = new Vue({
    el:"#userApp",
    data:{
        user:{}  /*默认json格式*/
    },
    methods:{
        loadCurrentUser:function () {
            axios({
                url:"/v1/users/me",
                method:"get"
            }).then(function (response) {
                //axios获得的返回值绑定给vue的
                userApp.user = response.data;
            })
        }
    },
    created:function () {
        this.loadCurrentUser();
    }

})