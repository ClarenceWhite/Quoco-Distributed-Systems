# Quoco-REST
## Architecture of my project
![architecture.png](architecture.png)
## How to run?
I built every component into docker except core and client, so we can open the terminal, cd to the root folder, and run:
```
docker compose up --build
```
then we would see spring boot apps start running:
![img.png](img.png)
open another terminal, type:
```
mvn exec:java -pl client
```
all information would be printed out in the console:
![img_1.png](img_1.png)