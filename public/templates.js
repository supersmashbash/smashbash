var templates = {};
  templates.events = [
    "<div class = 'my-event' data id = '<%= _id %>'>", //REMOVE THE UNDERSCORE ON ID WHEN USING ROUTES
    "<h6> <%= eventName %> </h6>",
    "<h6> <%= eventLocation %> </h6>",
    "<h6> <%= time %> </h6>",
    "<h6> <%= date %> </h6>",
    "<h6> <%= descrip %> </h6>",
    "<img src = '<%= img %>'>",
  ].join("");
