# EloLeaderboard Plugin

A simple Minecraft 1.21 Paper plugin that displays an Elo leaderboard using mini armor stands with player heads. The leaderboard updates every 10 seconds and pulls data from a MySQL database.

## Building
This project is a standard Maven structure but no build files are provided. You can create your own `pom.xml` or use your favorite build system to compile the plugin.

## Configuration
`config.yml` is generated on first run. Configure your MySQL database credentials and the leaderboard location.

```
database:
  host: localhost
  port: 3306
  name: minecraft
  user: root
  password: ''

leaderboard:
  world: world
  x: 0
  y: 100
  z: 0
  interval: 200
  size: 5
```

## Usage
Place the compiled JAR in your `plugins` folder and start the server. Mini armor stands will appear at the configured location displaying the top players by Elo.
