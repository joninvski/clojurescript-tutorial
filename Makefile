figwheel:
	docker run --rm -p 3449:3449 -p 40001:40001 -p 7888:7888 -it -w /usr/src/app -v ${PWD}:/usr/src/app \
		-e LEIN_REPL_PORT=4001 -e LEIN_REPL_HOST=0.0.0.0 \
		clojure:lein-alpine \
		lein figwheel dev
