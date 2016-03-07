var templates = {};
  templates.events = [
    "<div class = 'my-event' data-id = '<%= userName%>'>", //REMOVE THE UNDERSCORE ON ID WHEN USING ROUTES
    "<img src = '<%= image %>'>",
    "<h6 id = 'event-name'> <%= name %> </h6>",
    "<h6 id = 'event-location'> <%= location %> </h6>",
    "<h6 id = 'event-time'> <%= time %>  |  </h6>",
    "<h6 id = 'event-date'> <%= date %> </h6>",
    "<h6 id = 'event-descrip'> <%= description %> </h6>",
    "<button data-id = '<%= id %>'id='attending-button'> ATTEND </button>",
    "<button data-id = '<%= id %>' id='delete-button'> DELETE </button>",
    "<button data-id = '<%= id %>' id='edit-button'> EDIT </button>",
  ].join("");

  templates.savedEvents = [
    "<div class = 'my-event' id = '<%= userName %>'>",
    "<img src = '<%= event.image %>'>",
    "<h6 id = 'event-name'> <%= event.name %> </h6>",
    "<h6 id = 'event-location'> <%= event.location %> </h6>",
    "<h6 id = 'event-time'> <%= event.time %> </h6>",
    "<h6 id = 'event-date'> <%= event.date %> </h6>",
    "<h6 id = 'event-descrip'> <%= event.description %> </h6>",
  ].join("");
