FROM clojure:boot-alpine
WORKDIR /usr/src/app
COPY . /usr/src/app
CMD ["boot", "run"]
