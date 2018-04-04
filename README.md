# grocery-scraper

to run grocery scraper on linux you will need to follow these steps -

1. you must have git and a java jdk (1.7 or later) installed
2. make sure your JAVA_HOME is set correctly
3. git clone https://github.com/andylahan/grocery-scraper
4. cd grocery-scraper
5. ./gradlew build  (this will run all the test cases and build the project)
6. ./runGroceryScraper is used to run the application, it takes the URL to be queried as the only argument
    e.g. ./runGroceryScraper.sh https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html


