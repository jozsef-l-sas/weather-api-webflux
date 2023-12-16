# API server
The weather API is implemented with Spring Boot and Spring Webflux.

The single GET endpoint can be accessed at:

[http://localhost:8080/api/weather?city=Cluj-Napoca,Bucuresti,Timisoara, Constanta,Baia-Mare,Arad](http://localhost:8080/api/weather?city=Cluj-Napoca,Bucuresti,Timisoara,Constanta,Baia-Mare,Arad)

The generated CSV file is generated in the root of the project and it's called 'weather_forecast.csv'.

# API Client
A Spring WebClient to call the API above, retrieve and print the results and the content of the generated CSV file.

The API server needs to be started first.
