<a id="readme-top"></a>
<br />
<div align="center">
<h1>dslab.playandgo.test</h1>
<h2>Track Verification Test</h2>
<i>with Here API</i>

</div>
<hr>

<!-- TABLE OF CONTENTS -->
<details>
  <summary><font size="4"> Table of Contents</font> </summary>
  <ol>
    <li>
      <a href="#about-the-project">About the Project</a>
    </li>
    <li>
      <a href="#environment-variables">Environment Variables</a>
    </li>
    <li>
      <a href="#here-api">Here API</a>
    </li>
  </ol>
</details>
<hr>

<div id="about-the-project">
    <h2>About The Project</h2>
    <p>This project integrates with the HERE API to retrieve a specific route information based on the origin, destination and transport mode chosen by the user.
    It processes this data into a predefined template with Thymeleaf and enables verification of the route within the PlayAndGo platform.</p>
    <p align="right">(<a href="#readme-top">back to top</a>)</p>
</div>

<hr>

<div id="environment-variables">
    <h2>Environment Variables</h2>
    <b>Warning: ALL Environment Variables are mandatory.</b>
    <ul>
      <li>
          <h4>API_ENDPOINT</h4>
          <ul>
            The apiEndpoint variable represents the base URL for the API of the PlayAndGoEngine.
            <br><br>
            <b>Example:</b> https://localhost:{port}/playandgo/api
          </ul>
      </li>
      <li>
          <h4>CAMPAIGN_ID:</h4>
          <ul>
            Variable used by the PlayAndGoEngine to assign a survey and/or to send a challenge invitation, 
            of a specific campaign, to a player.
            <br><br>
            <b>Temporary Value:</b> TAA.city
          </ul>
      </li>
      <li>
          <h4>DEPARTURE_TIME:</h4>
          <ul>
            Departure Date and Time assigned to the Route created by the Here API. <br>
            <b>Attention!</b> Be sure to use the right format: "yyyy-MM-dd'T'HH:mm:ss"
            <br><br>
            <b>Example:</b> 2024-11-11T00:00:00
          </ul>
      </li>
      <li>
          <h4>END_WEEK:</h4>
          <ul>
            End Date and Time used by the TemplateManager to set the ending date of the survey in the template.<br>
            <b>Attention!</b> Be sure to use the right format: "yyyy-MM-dd'T'HH:mm:ss"
            <br><br>
            <b>Example:</b> 2024-11-15T00:00:00
          </ul>
      </li>
      <li>
          <h4>HERE_API_KEY:</h4>
          <ul>
            This is the Specific Key to access the HERE API.<br>
            To create it you need to have an account on HERE Api and generate it.<br>
            In the <a href="#here-api">HERE Api Section</a> you will have access to all the documentation to set and start using Here API.
          </ul>
      </li>
      <li>
          <h4>HERE_ROUTE_API_URL:</h4>
          <ul>
            This is the base URL for the <a href="#routing-api">HERE Routing API</a>.<br>
            It is used to create a route between two points only if the <b>transport mode is walk or bike.</b> 
            <br><br>
            <b>Value:</b> https://router.hereapi.com/v8/routes
          </ul>
      </li>
      <li>
          <h4>HERE_TRANSIT_API_URL:</h4>
          <ul>
            This is the base URL for the creation of the API Request of <a href="#public-transit-api">HERE Public Transit API</a>.<br>
            It is used to create a route between two points only if the <b>transport mode is bus, train or any other public transportation.</b>
            <br><br>
            <b>Value:</b> https://transit.router.hereapi.com/v8/routes
          </ul>
      </li>
      <li>
          <h4>OUTPUT_DIR:</h4>
          <ul>
            Personal output directory to where the final templates will be saved before sending them to the Backend of PlayAndGo for the verification of the track.
            <br><br>
            <b>Example for Linux:</b> /home/username/Documents/testResults
          </ul>
      </li>
      <li>
          <h4>PLAYER_ID:</h4>
          <ul>
            Variable used by the PlayAndGoEngine to assign a survey to a specific player.
            <br><br>
            <b>Temporary Value:</b> u_fe939cab-1638-45b3-a604-80a3fb018e54
          </ul>
      </li>
      <li>
          <h4>PLAYER_TO_INVITE:</h4>
          <ul>
            Variable used by the TemplateManager to set the playerId inside the template for the challenge invite.
            <br><br>
            <b>Temporary Value:</b> u_11111
          </ul>
      </li>
      <li>
          <h4>START_WEEK:</h4>
          <ul>
            Start Date and Time used by the TemplateManager to set the starting date of the survey in the template.<br>
            <b>Attention!</b> Be sure to use the right format: "yyyy-MM-dd'T'HH:mm:ss"
            <br><br>
            <b>Example:</b> 2024-11-11T00:00:00
          </ul>
      </li>
      <li>
          <h4>TOKEN:</h4>
          <ul>
            Authentication Token of AAC to access the Backend of PlayAndGo.
          </ul>
      </li>
    </ul>
    <hr>
    <i>Since the test still not implements survey and challenge invite, the following variables can be set to empty, but MUST be declared:</i>
    <ol>
        <li>CAMPAIGN_ID</li>
        <li>PLAYER_ID</li>
        <li>PLAYER_TO_INVITE</li>
    </ol>
    <p align="right">(<a href="#readme-top">back to top</a>)</p>
</div>

<hr>

<div id="here-api">
    <h2>Here API</h2>
<p>HERE API is a suite of location-based services provided by HERE Technologies, a leading company in mapping and geospatial data. It offers features such as mapping, routing, traffic, and transit information, enabling applications to deliver real-time navigation, route optimization, and location intelligence.</p>
    <h3>Get Started:</h3>
    <ol>
        <li>
            <a href="https://www.here.com/docs/bundle/identity-and-access-management-developer-guide/page/topics/plat-using-apikeys.html">Get an API Key</a>
        </li>
        <li>
            <a href="https://www.here.com/docs/bundle/identity-and-access-management-developer-guide/page/topics/manage-projects.html">Manage a Project</a>
        </li>
        <li>
            <a href="https://www.here.com/docs/bundle/identity-and-access-management-developer-guide/page/topics/manage-projects.html#services">Add a Service to a Project</a>
        </li>
    </ol>
    <font size="3"><b>You MUST add the following <u>Services</u> to your Project:</b> </font>
    <ol>
        <li>HERE Routing v8</li>
        <li>HERE Routing - Transit v8</li>
    </ol>
    <h4>Other Guides:</h4>
    <ul>
        <li>
            <a href="https://www.here.com/docs/bundle/public-transit-api-developer-guide/page/routing/route-example.html">Example: Calculate a Transit Route</a>
        </li>
        <li>
            <a href="https://www.here.com/docs/category/tutorials">HERE Official Tutorials</a>
        </li>
    </ul>
    <div align="center"><hr width="70%"></div>
    <h3>Here API Reference:</h3>
    The API references below are provided just to have a better understanding of the HERE API.<br>
    You will not need to know it perfectly to use this application since the test will automatically change the APIs parameters based on the initial request.
    <ul>
        <li>
            <div id="routing-api">
                <a href="https://www.here.com/docs/bundle/routing-api-v8-api-reference/page/index.html">Routing API Documentation</a>
            </div>
        </li>
        <li>
            <div id="public-transit-api">
                <a href="https://www.here.com/docs/bundle/public-transit-api-developer-guide/page/routing/README.html">Public Transit API Documentation</a>
            </div>
        </li>
        </ul>
    <p align="right">(<a href="#readme-top">back to top</a>)</p>
</div>



