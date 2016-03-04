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
    $('.sign-in-button').on('click', page.storingUserName);
},

  getUserFromDom: function() {
    var username = $('input[name="user-login"]').val();
    return username;
  },

  getUserNameFromStorage: function() {
    return localStorage.getItem('username');
  },

  getPasswordFromDom: function () {
    var password = $('input[name="user-password"]').val();
    return password;
  },

  getPasswordFromStorage: function() {
    return localStorage.getItem('password');
  },

  storingUserName: function() {
    event.preventDefault();
    var newUserName = page.getUserFromDom();
    var newPassword = page.getPasswordFromDom();
    if (newUserName === "" && newPassword === ""){
      alert("Type in your username and password!");
    }
    else {
      localStorage.setItem('username', newUserName);
      localStorage.setItem('password', newPassword);
      page.hideUserPage();
      page.addUserPassToServer(page.getPasswordToStorage());
    }
  },

  hideUserPage: function() {
    $('.login-page').addClass('inactive');
    $('.user-page').removeClass('inactive');
  },

  getPasswordToStorage: function (){
    var username = page.getUserNameFromStorage ();
    var password = page.getPasswordFromStorage ();
    return {
      username: username,
      password: password
    }
  },

  // AJAX

  addUserPassToServer: function(usernameInput) {
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
  }
}
