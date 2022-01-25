# anymind
After down load this project then type command 
  ./mvnw clean compile spring-boot:run
Project can be run with port http://localhost:8080


Get History of your balance API
http://localhost:8080/balances/{Start Date}/{End Date}
http://localhost:8080/balances/2022-01-22T16:48:01+07:00/2022-01-23T23:45:05+07:00

POST Deposit your BTC 
http://localhost:8080/deposit
{
    "datetime": "2022-01-22T22:45:05+07:00",
    "amount": 20
}
