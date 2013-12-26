#!/bin/sh
time curl -H "content-type: text/xml" -d @soap.xml "http://localhost:8080/smev/mvvact"
