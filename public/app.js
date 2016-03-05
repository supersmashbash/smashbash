$(document).ready(function(){
  page.init();
});

var userName = "";

var page = {
  // url: "http://tiny-tiny.herokuapp.com/collections/hbd",
  url: {
    login: "/login",
    logout: "/logout",
    events: "/events",
    createEvent: "/createEvent"
    //need to add 'create events' route
  },
  init: function(){
    page.styling();
    page.events();
  },
  styling: function(){

  },
  events: function() {
    $('.create-button').on('click', page.storingUserName);
    $('.my-events-button').on('click', page.hideUserPage); //toggling
    $('.new-events-button').on('click', page.hideUserPageAgain); //toggling
    $('.create-event-button').on('click', page.createEvent); //submiting 'create events' form
    $('.my-events-button').on('click', page.getStoredEvents); //showing 'my events
    $('.back-button-user').on('click', page.backButtonUser);
    $('.back-button-post').on('click', page.backButtonPost);
    $('.sign-out-button').on('click', ($.post(page.url.logout)));
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
      $("#welcome-message").append(page.getUserFromDom());
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

  backButtonUser: function () {
    $('.user-page').removeClass('inactive');
    $('.user-events-container').addClass('inactive');
  },

  backButtonPost: function () {
    $('.user-page').removeClass('inactive');
    $('.post-event-container').addClass('inactive');
  },

  // AJAX

  addNewUserPassToServer: function(usernameInput) {
    $.ajax({
      url: page.url.login,
      method: 'POST',
      data: usernameInput,
      success: function () {
        userName = page.getUserFromDom();
        page.getPasswordToStorage();
      },
      error: function (err) {
        console.log ("error", err);
      }
    });
  },

//CREATE EVENTS

  createEvent: function () {
    event.preventDefault();
    var eventInfo = page.getEventInfo();
    console.log (eventInfo);
    page.storeEvent(eventInfo);
  },

  getEventInfo: function () {
    var eventName = $('input[name="eventName"]').val();
    var eventLocation = $('input[name="eventLocation"]').val();
    var time = $('input[name="timeOfEvent"]').val();
    var date = $('input[name="dateOfEvent"]').val();
    var descrip = $('input[name="descripOfEvent"]').val();
    var img = $('input[name="imgOfEvent"]').val();
    return {
      eventName: eventName,
      eventLocation: eventLocation,
      time: time,
      date: date,
      image: img,
      description: descrip,
    };
  },
  storeEvent: function (eventInfo) {
    $.ajax ({
      method: 'POST',
      url: page.url.createEvent,
      data: eventInfo,
      success: function (eventInfo) {
        console.log ("CREATED EVENT", eventInfo);
      },
      error: function (err) {
        console.log ("creating event not working", err);
      },
    });
  },

//MY EVENTS


  addMyEventsToDom: function (eventInfo) {
    $('.created-events').html("");
    var tmpl = _.template(templates.events);
    eventInfo.forEach (function (evt) {
      $('.created-events').append(tmpl(evt));
    });
    page.hideUserPage ();
  },
  getStoredEvents: function (){
    $.ajax ({
      method: 'GET',
      url: page.url.createEvent,
      success: function (eventInfo) {
        console.log ("RECEIVED EVENTS", eventInfo);
        page.addMyEventsToDom(eventInfo);
      },
      error: function (err) {
        console.log("DID NOT RECEIVE EVENTS", err);
      }
    });
  },


}; //end of page init
