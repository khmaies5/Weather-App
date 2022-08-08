# PSA Weather - Android App


PSA Weather is a minimalist weather app.  

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:wolox/<reponame>.git
```
## Configuration
### API key:
Edit data build.gradle to add your API key:
```gradle
        buildConfigField "String", "weather_api_key", '"...."'

```

## Features

The android app lets you:
- save multiple a cities .
- Delete a city.
- use it offline.
- Needs no special permissions on Android 6.0+.

## Screenshots

[<img src="/screenshots/Screenshot1.png" align="left"
width="200"
    hspace="10" vspace="10">](/screenshots/Screenshot1.png)
[<img src="/screenshots/Screenshot2.png" align="center"
width="200"
    hspace="10" vspace="10">](/screenshots/Screenshot2.png)
    [<img src="/screenshots/Screenshot3.png" align="center"
width="200"
    hspace="10" vspace="10">](/screenshots/Screenshot3.png)

## Permissions

PSA Weather requires the following permissions:
- Full Network Access.
- View Network Connections.


## License

This application is released under GNU GPLv3 (see [LICENSE](LICENSE)).
Some of the used libraries are released under different licenses.
