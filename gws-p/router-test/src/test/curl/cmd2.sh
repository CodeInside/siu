#!/bin/sh
time curl -H "content-type: text/xml" -d @request.xml "http://localhost:8080/smev/mvvact"
