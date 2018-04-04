#!/bin/bash
if [ $# != 1 ]
  then
    echo "GroceryScraper takes 1 argument"
    echo "Supply the URL to be queried"
  exit 1
fi

./gradlew run -q -Purl="['$1']"
