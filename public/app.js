$(document).ready(function(){
  page.init();
});

var page = {
  url: "http://tiny-tiny.herokuapp.com/collections/smashbash",
  init: function(){
    page.styling();
    page.events();
  },
  styling: function(){

  },
  events: function() {
    $('.create-button').on('click', page.storingUserName);
    $('.sign-in-button').on('click', page.checkingUserName);
  },

  //CREATE USERNAME AND PASSWORD

  getUserFromDom: function() {
    var username = $('input[name="create-user-login"]').val();
    return username;
  },

  getPasswordFromDom: function () {
      var password = $('input[name="create-user-password"]').val();
      return password;
  },


  storingUserName: function() { //storing username & password in Server
    event.preventDefault();
    var newUserName = page.getUserFromDom();
    var newPassword = page.getPasswordFromDom();
    if (newUserName === "" && newPassword === ""){
      alert("Type in your username and password!");
    }
    else {
      page.hideUserPage();
      page.addNewUserPassToServer(page.getPasswordToStorage());
    }
  },

  hideUserPage: function() {
    $('.login-page').addClass('inactive');
    $('.user-page').removeClass('inactive');
  },

  getPasswordToStorage: function (){
    var username = page.getUserFromDom ();
    var password = page.getPasswordFromDom ();
    return {
      username: username,
      password: password
    };
  },

  // AJAX

  addNewUserPassToServer: function(usernameInput) {
    $.ajax({
      url: page.url,
      method: 'POST',
      data: usernameInput,
      success: function () {
        page.getPasswordToStorage();
      },
      error: function (err) {
        console.log ("error", err);
      }
    });
  },

  //LOGGING IN

  logIn: function () {
  },
  getPrevUserPassFromDom: function() {
      var username = $('input[name="user-login"]').val();
      return username;
    },

  getPrevPasswordFromDom: function () {
        var password = $('input[name="user-password"]').val();
        return password;
  },

  checkUserPass: function (){
    var username = page.getPrevUserPassFromDom ();
    var password = page.getPrevPasswordFromDom ();
    return {
      username: username,
      password: password
    };
  },

  checkingUserName: function() { //storing username & password in Server
    event.preventDefault();
    var prevUserName = page.getPrevUserPassFromDom();
    var prevPassword = page.getPrevPasswordFromDom();
    if (prevUserName === "" && prevPassword  === ""){
      alert("Type in your username and password!");
    }
    else {
      page.hideUserPage();
      page.sendUserPass(page.checkUserPass());
    }
  },


  //AJAX
  sendUserPass: function (usernameInput) {
    $.ajax({
      url: page.url,
      method: 'POST',
      data: usernameInput,
      success: function () {
        page.checkUserPass();
      },
      error: function (err) {
        console.log ("error", err);
      }
    });
  },

}; //end of page init
