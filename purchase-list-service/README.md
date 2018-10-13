Kitchen Device Service
======================

The kitchen device service allows REST CRUD operations on kitchen device
objects, stored in the database. This is a very simple demo application.
The application is not hardened for production use.

It has three data channels:
* HATEOAS paging and sorting REST API under `api/devices`
* Controller that generates server pages under `controller/devices`
* When using Edge Site Includes under `edge/devices`

Also, this application starts several debugging tools
* H2 Database console to see database rows under `/h2`
* HATEOAS Rest Data browser, to see REST data under `/browser`
* The controller application is a fully fledged web application under `controller/site`
