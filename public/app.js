$(document).ready(function(){
  page.init();
});

var page = {
  // url: "http://tiny-tiny.herokuapp.com/collections/hbd",
  url: {
    login: "/login",
    logout: "/logout"
  },
  init: function(){
    page.styling();
    page.events();
  },
  styling: function(){

  },
  events: function() {
    $('.create-button').on('click', page.storingUserName);
    $('.sign-in-button').on('click', page.checkingUserName);
    $('.my-events-button').on('click', page.hideUserPage);
    $('.new-events-button').on('click', page.hideUserPageAgain);
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
      page.hideLoginPage();
      page.addNewUserPassToServer(page.getPasswordToStorage());
    }
  },

  hideLoginPage: function() {
    $('.login-page').addClass('inactive');
    $('.user-page').removeClass('inactive');
  },

  getPasswordToStorage: function (){
    var username = page.getUserFromDom();
    var password = page.getPasswordFromDom();
    return {
      username: username,
      password: password
    };
  },

  // to see the my events page
  hideUserPage: function() {
    $('.login-page').addClass('inactive');
    $('.user-page').addClass('inactive');
    $('.user-events-container').removeClass('inactive');
  },

  // to see the create new event page
  hideUserPageAgain: function() {
    $('.login-page').addClass('inactive');
    $('.user-page').addClass('inactive');
    $('.post-event-container').removeClass('inactive');
  },

  // to go back to login page
  showLoginPage: function() {
    $('.login-page').removeClass('inactive');
    $('.user-page').addClass('inactive');
    $('.user-events-container').addClass('inactive');
    $('.post-event-container').addClass('inactive');
  },


  // AJAX

  addNewUserPassToServer: function(usernameInput) {
    $.ajax({
      url: page.url.login,
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

  logoutOfServer: function(usernameLogout) {
    $.ajax({
      url: page.url.logout,
      method: 'POST',
      data: usernameLogout,
      success: function() {
        $('.sign-out-button').on('click', page.showLoginPage);
      },
      error: function (err) {
        console.error("error", err);        
      }
    });
  }


}; //end of page init
