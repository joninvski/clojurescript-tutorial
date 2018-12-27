# iShorty

The shorter url shortner

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Have a machine with docker, docker-compose and make.

### Development instruction

Should expose the port being used in file `.nrepl-port`

```
make repl
```

You can also lint the code via:

```
make lint
```

To serve the clojure script frontend and automatically reload:

```
make dev
```

### Running instruction

```
make run
```

You can change the `HTTP_PORT` in the `.env` file to suit your needs.

After the `make run` command finished, point your browser to `http://localhost:${HTTP_PORT}/`.
