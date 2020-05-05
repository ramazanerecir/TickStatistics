# TickStatistics
Tick Statistics service in a sliding time interval

Requirements
Java 8
Spring Boot
Rabbit MQ
Lombok
JUnit

Project
It is a Restful API to monitor the incoming instrument prices. 
The main use case for that API is to provide real-time price statistics from the last 60 seconds (sliding time interval).
There will be three APIs:
• POST "/ticks" : It is called every time we receive a tick. It is also the sole input of this rest API.
Input
{
"instrument": "IBM.N",
"price": 143.82,
"timestamp": 1478192204000
}
• GET "/statistics" : It returns the statistics based on the ticks of all instruments of the last 60 seconds (sliding time interval)
Output
{
"avg": 100,
"max": 200,
"min": 50,
"maxDrawdown”: 10,
“volatility”: 7,
“quantile”: 55,
“twap”: 104,
“twap2”: 106,
"count": 10
}
• GET "/statistics/{instrument_identifier}" : It returns the statistics based on the ticks of one instrument of the last 60 seconds 
(sliding time interval).
Output is same as previous
