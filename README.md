<a id="readme-top"></a>
<div align="center">
<h1>dslab.playandgo.test</h1>
<h2>Track Verification Test</h2>
<i>with Here API</i>
</div>
<br>
<hr>

<!-- TABLE OF CONTENTS -->
## Table of contents
1. [About the Project](#about-the-project)
2. [Environment Variables](#environment-variables)
3. [Here API](#here-api)

<hr>

<a id="about-the-project"></a>
## About The Project
This project integrates with the HERE API to retrieve a specific route information based on the origin, destination and transport mode chosen by the user.
It processes this data into a predefined template with Thymeleaf and enables verification of the route within the PlayAndGo platform.
<p align="right">(<a href="#readme-top">back to top</a>)</p>

<hr>

<a id="environment-variables"></a>
## Environment Variables

> [!WARNING]
> **Environment Variables are mandatory.**
    
- **API_ENDPOINT**
  - The apiEndpoint variable represents the base URL for the API of the PlayAndGoEngine.
  - **Example:** https://.../playandgo/api (localhost or direct playandgo api endpoint)


<!-- - **CAMPAIGN_ID:**
  - Variable used by the PlayAndGoEngine to assign a survey and/or to send a challenge invitation, of a specific campaign, to a player.
  - **Temporary Value:** TAA.city -->


- **DEPARTURE_TIME:**
  - Departure Date and Time assigned to the Route created by the Here API.</br>
  **Attention!** Be sure to use the right format: "yyyy-MM-dd'T'HH:mm:ss"
  - **Example:** 2024-11-11T00:00:00


<!-- - **END_WEEK:**
  - End Date and Time used by the TemplateManager to set the ending date of the survey in the template.</br>
  **Attention!** Be sure to use the right format: "yyyy-MM-dd'T'HH:mm:ss"
  - **Example:** 2024-11-15T00:00:00 -->

    
- **HERE_API_KEY:**
  - This is the Specific Key to access the HERE API.</br>
    To create it you need to have an account on HERE Api and generate it.</br>
  - In the [HERE Api Section](#here-api) you will have access to all the documentation to set and start using Here API.


- **HERE_ROUTE_API_URL:**
  - This is the base URL for the [HERE Routing API](#here-api-reference).</br>
  It is used to create a route between two points only if the transport mode is walk, bike or car.
  - **Value:** https://router.hereapi.com/v8/routes


- **HERE_TRANSIT_API_URL:**
    - This is the base URL for the creation of the API Request of [HERE Public Transit API](#here-api-reference).</br>
    It is used to create a route between two points only if the transport mode is bus, train or any other public transportation.
    - **Value:** https://transit.router.hereapi.com/v8/routes


<!-- - **OUTPUT_DIR:**
    - Personal output directory to where the final templates will be saved before sending them to the Backend of PlayAndGo for the verification of the track.
    - **Example for Linux:** /home/username/Documents/testResults


 - **PLAYER_ID:**
    - Variable used by the PlayAndGoEngine to assign a survey to a specific player.
    - **Temporary Value:** u_fe939cab-1638-45b3-a604-80a3fb018e54
  

- **PLAYER_TO_INVITE:**
    - Variable used by the TemplateManager to set the playerId inside the template for the challenge invite.
    - **Temporary Value:** u_11111


- **START_WEEK:**
  - Start Date and Time used by the TemplateManager to set the starting date of the survey in the template.</br>
  Attention! Be sure to use the right format: "yyyy-MM-dd'T'HH:mm:ss"
  - **Example:** 2024-11-11T00:00:00 -->

> [!WARNING]
> This variable is mandatory **ONLY if you wanna send the track directly to PlayAndGoEngine.** (playAndGoEngine.sendTrack(track); in the code) </br>
> <ins>If you wanna just create a json file, this token IS NOT USED.</ins>

- **TOKEN:**
    - Authentication Token of AAC to access the Backend of PlayAndGo.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<hr>

<a id="here-api"></a>
## Here API
HERE API is a suite of location-based services provided by HERE Technologies, a leading company in mapping and geospatial data. It offers features such as mapping, routing, traffic, and transit information, enabling applications to deliver real-time navigation, route optimization, and location intelligence.
### Get Started:
1. [Get an API Key](https://www.here.com/docs/bundle/identity-and-access-management-developer-guide/page/topics/plat-using-apikeys.html)
2. [Manage a Project](https://www.here.com/docs/bundle/identity-and-access-management-developer-guide/page/topics/manage-projects.html)
3. [Add a Service to a Project](https://www.here.com/docs/bundle/identity-and-access-management-developer-guide/page/topics/manage-projects.html#services)

> [!NOTE]    
> If you want to use <u>your own</u> HERE Api for this App, you <b>MUST</b> add the following <u>Services</u> to your Project:
1. HERE Routing v8
2. HERE Routing - Transit v8

#### Other Guides:
- [Example: Calculate a Transit Route](https://www.here.com/docs/bundle/public-transit-api-developer-guide/page/routing/route-example.html)
- [Here Official Tutorials](https://www.here.com/docs/category/tutorials)

<a id="here-api-reference"></a>
### Here API Reference:
> [!NOTE]
>The API references below are provided just to have a better understanding of how HERE API works.<br>
>You will not need to know it perfectly to use this application since the test will automatically change the APIs parameters based on the initial request.

- [Routing API Documentation](https://www.here.com/docs/bundle/routing-api-v8-api-reference/page/index.html)
- [Public Transit API Documentation](https://www.here.com/docs/bundle/public-transit-api-developer-guide/page/routing/README.html)

<p align="right">(<a href="#readme-top">back to top</a>)</p>
