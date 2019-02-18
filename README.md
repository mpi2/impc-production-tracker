# impc-production-tracker
This repository will host the code for the new and improved IMPC effort production tracker.  This is a successor to iMits, which started life as the ESCell production tracking database for the IMPC project.


To build the project with docker-compose:

cd impc-production-tracker

docker-compose up -d --build


This will start the server on localhost on port 8901

http://127.0.0.1:8091



To follow the activity on the server:

docker-compose logs -f prod_tracker



To check all the local services running:

docker-compose ps



To stop the server

docker-compose down