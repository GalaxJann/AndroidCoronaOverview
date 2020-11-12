# Corona Overview Germany - Android
With this app you can view the current cases of the Coronavirus in Germany based on the dataset of the RKI (Robert Koch Institut).<br>
This app is literally faster than the [experience](https://experience.arcgis.com/experience/478220a4c454480e823b17327b2bf1d4/page/page_1/) website or any other of these ArcGIS websites, because it's a native Android application which is limited to a minimum of important data.

## Features
View the current data of following places:
- RLP (Rheinland-Pfalz; Maybe there will come a selection of all federal states)
    - Total + New total cases
    - Active + New active cases
    - Deaths + New deaths
    - Recovered + New recovered
- MYK (Mayen-Koblenz; Maybe there will come a selection of all counties)
    - Total cases
    - Cases per 100k + Cases per 7-day 100k
    - Death cases
- Germany
    - Total + New total cases
    - Active + New active cases
    - Deaths + New deaths
    - Recovered + New recovered
<a/>
Info: The "New"-Values are the new cases from past day to today.

## How does it work
The App gets the data from the dataset of the [experience](https://experience.arcgis.com/experience/478220a4c454480e823b17327b2bf1d4/page/page_1/) website with simple REST Requests, which sends JSON-Responses which I can parse very easily.

## Screenshots
[go here](Screenshots.md)

## Planned Feautures/Improvements
\+ Scrolling for pages<br>
\+ Selection for all federal states<br>
\+ Selection for all counties<br>
\# Fix some crashes

## Credits
### Used Libraries
- Material Library (Google) --> Used for material design
- OkHttp3 (SquareUp) --> Used for REST Requests

### Other
- RKI Experience Website --> Publishing Corona dataset
