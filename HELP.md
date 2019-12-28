### Building Spring Boot + AngularJS App
```node
npm install --save-dev rimraf
npm install --save-dev mkdirp
npm install --save-dev copyfiles
```

```bash
mvn clean && npm --prefix src/main/frontend run build && mvn package
```