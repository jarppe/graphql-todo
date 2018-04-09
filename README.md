# GraphQL TODO

Simple TODO example using [GraphQL](http://graphql.org/) and [MongoDB](https://www.mongodb.com/). This 
application is implemented using [Clojure](https://clojure.org).

## Develop

Start MongoDB:

```bash
$ docker-compuse up -d
```

Start figwheel:

```bash
$ lein dev
```

Start server:

```bash
$ lein repl
nREPL server started on port 54171 on host 127.0.0.1 - nrepl://127.0.0.1:54171
REPL-y 0.3.7, nREPL 0.2.12
Clojure 1.9.0
Java HotSpot(TM) 64-Bit Server VM 1.8.0_151-b12
    Docs: (doc function-name-here)
          (find-doc "part-of-name-here")
  Source: (source function-name-here)
 Javadoc: (javadoc java-object-or-class-here)
    Exit: Control+D or (exit) or (quit)
 Results: Stored in vars *1, *2, *3, an exception in *e

user=> (reset)
```

## License

Copyright © 2018 Jarppe

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
