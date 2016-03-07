$(document).ready(function(){
  page.init();
});

var userName = "";
var allData = "";

var page = {
  url: {
    login: "/login",
    logout: "/logout",
    events: "/events",
    createEvent: "/createEvent",
    addEvent: "/addEventAttending",
    myEvents: "/accountEventsCreated",
    savedEvents: "/accountEventsAttending",
    delete:"/deleteEvent",
    editTheEvent: "/editEvent"
  },
  init: function(){
    page.styling();
    page.events();
  },
  styling: function(){
    // page.getAllStoredEvents();
setInterval(function(){page.getAllStoredEvents();}, 1000);
setInterval(function(){page.getMySavedEvents();}, 1000);
setInterval(function(){page.getMyStoredEvents();}, 1000);
  },
  events: function() {
    $('.create-button').on('click', page.storingUserName);
    $('.my-events-button').on('click', page.hideUserPage); //toggling
    $('.new-events-button').on('click', page.hideUserPageAgain); //toggling
    $('.create-event-button').on('click', page.createEvent); //submiting 'create events' form
    $('.create-event-button').on('click', page.hideUserPage); //submiting 'create events' form
    $('.my-events-button').on('click', page.getMyStoredEvents); //showing 'my created events
    $('.my-events-button').on('click', page.getMySavedEvents); //showing 'my saved events
    $('.event-container').on('click', '#attending-button', page.saveEvent); // saving attending events
    $('.event-container').on('click', '#delete-button', page.deleteEvent); // deleting
    $('.created-events').on('click', '#delete-button', page.deleteEvent); // deleting
    $('.created-events').on('click', '#edit-button', page.hideUserPageAgain); // edit button take you to create page
    $('.created-events').on('click', '#edit-button', page.editEvent); // edit button hopefully edit
    $('.back-button-user').on('click', page.backButtonUser);
    $('.back-button-post').on('click', page.backButtonPost);
    $('.sign-out-button').on('click', ($.post(page.url.logout)) && page.signOutButton);
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
      $("#welcome-message").html("Welcome " + page.getUserFromDom() + "!");
      $('input[name="create-user-login"]').val('');
      $('input[name="create-user-password"]').val('');
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

//BACK BUTTONS

  // to see the my events page
  hideUserPage: function() {
    $('.login-page').addClass('inactive');
    $('.user-page').addClass('inactive');
    $('.post-event-container').addClass('inactive');
    $('.user-events-container').removeClass('inactive');
  },

  // to see the create new event page
  hideUserPageAgain: function() {
    $('.login-page').addClass('inactive');
    $('.user-page').addClass('inactive');
    $('.user-events-container').addClass('inactive');
    $('.post-event-container').removeClass('inactive');
  },

  // to go back to login page
  showLoginPage: function() {
    $('.login-page').removeClass('inactive');
    $('.user-page').addClass('inactive');
    $('.user-events-container').addClass('inactive');
    $('.post-event-container').addClass('inactive');
  },

  // to go back to user page
  backButtonUser: function () {
    $('.user-page').removeClass('inactive');
    $('.user-events-container').addClass('inactive');
  },

  // to go back to post event page
  backButtonPost: function () {
    $('.user-page').removeClass('inactive');
    $('.post-event-container').addClass('inactive');
  },

  // to go back to the login page
  signOutButton: function () {
    $('.user-page').addClass('inactive');
    $('.user-events-container').addClass('inactive');
    $('.post-event-container').addClass('inactive');
    $('.login-page').removeClass('inactive');
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
        console.log ("CREATED EVENT" + eventInfo);
        $('input[name="eventName"]').val("");
        $('input[name="eventLocation"]').val("");
        $('input[name="timeOfEvent"]').val("");
        $('input[name="dateOfEvent"]').val("");
        $('input[name="descripOfEvent"]').val("");
        $('input[name="imgOfEvent"]').val("");
      },
      error: function (err) {
        console.log ("creating event not working", err);
      },
    });
  },

    getMySavedEvents: function (){
      $.ajax ({
        method: 'GET',
        url: page.url.savedEvents,
        success: function (eventInfo) {
          var eventI = JSON.parse(eventInfo);
          page.addEventsToDom(eventI, $('.saved-events'), templates.savedEvents);
        },
        error: function (err) {
          console.log("DID NOT RECEIVE EVENTS", err);
        }
      });
    },

//STORING AND DISPLAYING EVENTS


  addEventsToDom: function (eventInfo, $target, template) {
    $($target).html("");
    var tmpl = _.template(template);
    eventInfo.forEach (function (evt) {
      $($target).append(tmpl(evt));
    });
  },

// All Events
  getAllStoredEvents: function (){
    $.ajax ({
      method: 'GET',
      url: page.url.events,
      success: function (eventInfo) {
        allData = JSON.parse(eventInfo);
        page.addEventsToDom(allData, $('.event-container'),templates.events);
      },
      error: function (err) {
        console.log("DID NOT RECEIVE EVENTS", err);
      }
    });
  },

//My Events
getMyStoredEvents: function (){
  $.ajax ({
    method: 'GET',
    url: page.url.myEvents,
    success: function (eventInfo) {
      var eventI = JSON.parse(eventInfo);
      console.log (eventI);
      page.addEventsToDom(eventI, $('.created-events'), templates.events);
    },
    error: function (err) {
      console.log("DID NOT RECEIVE EVENTS", err);
    }
  });
},


//DELETING EVENTS

deleteEvent: function (event) {
  event.preventDefault();
  var eventId = ($(this).data('id'));
  console.log (eventId);
  page.deleteEventFromStorage (eventId);
},

deleteEventFromStorage: function (eventId){
  $.ajax({
    method:'POST',
    url: page.url.delete,
    data: {eventId: eventId},
    success: function () {
          console.log ('deleted event');
    }
  });
},

//SAVING EVENTS

saveEvent: function (event) {
  event.preventDefault();
  var eventId = ($(this).data('id'));
  console.log (eventId);
  page.saveEventInStorage(eventId);
},

saveEventInStorage: function (eventId) {
  $.ajax({
    method:'POST',
    url: page.url.addEvent,
    data: {eventId: eventId},
    success: function () {
          console.log ('saved event');
    }
  });
},

// EDIT EVENTS

editEvent: function(event){
  event.preventDefault();
  var eventId = ($(this).data('id'));
  var eventInfo = page.getEventInfo();
  console.log (eventId); // THIS LOGGING
  page.editEventInStorage(eventId);
  var editedEvent = page.updateEvent(editedEvent);
},

editEventInStorage: function () {
  var eventId = ($(this).data('id'));
  var eventName = $('input[name="eventName"]').val();
  $('input[name="eventName"]').val('');
  var eventLocation = $('input[name="eventLocation"]').val();
  $('input[name="eventLocation"]').val('');
  var time = $('input[name="timeOfEvent"]').val();
  $('input[name="timeOfEvent"]').val('');
  var date = $('input[name="dateOfEvent"]').val();
  $('input[name="dateOfEvent"]').val('');
  var descrip = $('input[name="descripOfEvent"]').val();
  $('input[name="descripOfEvent"]').val('');
  var img = $('input[name="imgOfEvent"]').val();
  $('input[name="imgOfEvent"]').val('');
  return {
    eventId: eventId,
    eventName: eventName,
    eventLocation: eventLocation,
    time: time,
    date: date,
    image: img,
    description: descrip,
  };
},

updateEvent: function (editedEvent){
  $.ajax({
    method: 'POST',
    url: page.url.editTheEvent,
    data: editedEvent,
    success: function(result){
      console.log ('edit event');
    },
    error: function (err){
      console.log ('it didnt edit event');
    },
  });
},

}; //end of page init
