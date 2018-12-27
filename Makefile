REPL_PORT ?= "40001"
REPL_HOST ?= "0.0.0.0"
HTTP_PORT ?= "3500"

.PHONY: test

run:
	docker-compose build web && docker-compose up web

repl:
	docker-compose run --service-ports repl

lint:
	docker-compose run test boot check-sources

test:
	docker-compose run test

dev:
	docker-compose run --service-ports repl boot dev
