# Markazpillar (Backend)
Staging Website: http://staging.markazpillar.afriza.co <br>
API endpoint: http://api.staging.markazpillar.afriza.co <br>
Postman: https://documenter.getpostman.com/view/12004801/UUxxgU6Y/ <br>

## What is Markazpillar?
Markaz Pillar is a website for the tahfiz community where people can learn, volunteer, and donate. The Markazpillar project uses [Spring Boot](https://spring.io/projects/spring-boot) for its backend side.

## Developers
- [Achmad Afriza Wibawa - 1906315821](https://gitlab.cs.ui.ac.id/achmad.afriza)
- [Hanifah Annisa Putri - 1906315935](https://gitlab.cs.ui.ac.id/hanifahaputri)
- [Abimanyu Yuda Dewa - 1906426714](https://gitlab.cs.ui.ac.id/AbimanyuYudaDewa)
- [Wishnu Hazmi Lazuardi - 1906316793](https://gitlab.cs.ui.ac.id/wishnuhl)
- [Al Taaj Kautsar Supangkat - 1906426746](https://gitlab.cs.ui.ac.id/al.taaj)
- [Kevin Muhammad Afiza - 1906316603](https://gitlab.cs.ui.ac.id/KevinAfiza)

For building and running the application you need:
- JDK 

1. Run the application on https://localhost:8080/
``` 
$ ./gradlew bootRun
```

2. Run unit testing with [JUnit](https://junit.org/junit5/)
```
$ ./gradlew test
```

## Pages
### Admin
Needs `ROLE_SUPERUSER` or `ROLE_ADMIN`
#### Markaz
- Create, Delete Markaz (/admin/markaz)
- Edit Markaz (/admin/markaz/edit)
#### Santri
- Create, Delete Santri (/admin/santri)
- Edit Santri (/admin/santri/edit)
#### Donation
- Read Donation Detail (/admin/donation)
- Fetch and POST Markaz Donation (/admin/donation/markaz)
- Edit Markaz Donation (/admin/donation/markaz/edit)
- Fetch and POST Santri Donation (/admin/donation/santri)
- Edit Santri Donation (/admin/donation/santri/edit)
#### Transaction
- Fetch User Transactions by Donation (/admin/transaction)
- Edit Transaction Status (/admin/transaction/status)

### User
- Login (/authenticate)
- Refresh (/authenticate/refresh)
- Validate (/authenticate/validate)
- Register (/register)
- Get and Edit Profile (/user)
#### Markaz
- Get Markaz detail (/markaz)
- Fetch list of Markaz (/markaz/search)
#### Santri
- Get Santri detail (/santri)
- Fetch list of Santri (/santri/search)
#### Transaction
- Fetch list of User Transactions (/transaction)
- Create Transaction for User (/transaction)