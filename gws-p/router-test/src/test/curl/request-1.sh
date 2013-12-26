#!/bin/sh
time curl -H "content-type: text/xml" -d @request-1.xml "http://localhost:8080/smev/mvvact"
