
# Offer forum app

The application is aimed at jobseekers. The application is based on the following user roles: normal user, administrator and recruiter. </br>
Frontend : https://github.com/iamruppert/offers-forum-app-front


## Authors

- [@LukaszZiolkiewicz](https://www.github.com/iamruppert)


## Used technology

Spring Boot 3, Gradle, PostgreSQL, JUnit5, TestContainers, JWT


## user functions
- account registration and login
- overview of all offers
- view information on a particular offer
- search for specific offers by name
- search for offers by name and price
- add offers to favourites, delete offers from favourites


## administrator functions
- login to your account
- overview of all offers
- view information about an offer
- search for specific offers by name
- search for offers by name and price
- creation of administrators and recruiters
- add, edit and delete any offer

## recruiter functionality
- logging into your account
- view all offers
- view information about an offer
- search for specific offers by name
- search offers by name and price
- add offers and modify, i.e. edit and delete only offers you have created



## API Reference

Application publishes Swagger API documentation. Some of the endpoints are shown below.
After running the app the Swagger documentation is under : http://localhost:8080/swagger-ui/index.html

#### adding offer to favourites

```
  POST /api/registeredUser/addToFavourite/{offerId}
```

| Parameter | Type     | 
| :-------- | :------- | 
| `offerId` | `integer` |

#### getting all offers

```
  GET /api/listAllOffers
```
#### adding offer to favourites

```
  POST /api/auth/register
```
| Parameter  | Schema                                                |
| :--------- | :-------------------------------------------------- |
| `Request body`     | `{ "name": "string", "surname": "string", "pesel": "string", "country": "string", "email": "string", "username": "string", "password": "string" }` |
