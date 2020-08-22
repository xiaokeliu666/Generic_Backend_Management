# Generic_Backend_Management
## Introduction
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This is a generic backend management. Its main features include: authority management, menu management, user management, real-time system monitoring, web service encryption and so on.
## Tech Stack
Frontend: Layui

Backend: SpringBoot + Thymeleaf + Spring Security + Spring Data JPA + MySQL
## Project Display
### User Management
![](https://github.com/xiaokeliu666/Generic_Backend_Management/blob/latest_branch/src/main/resources/static/image/User.jpg?raw=true)
&nbsp;&nbsp;&nbsp;&nbsp;In this section you can do some action on users, for example: soft delete, ban IP, multiple online and so on. If you are an admin, you can findout how many users are online currently and even force them to offline(this feature doesn't show in the image due to the screenshot, please find by running this project)
### Authority Management
![](https://github.com/xiaokeliu666/Generic_Backend_Management/blob/latest_branch/src/main/resources/static/image/Authority.jpg?raw=true)
&nbsp;&nbsp;&nbsp;&nbsp;In this section, we can edit different roles and corresponding authorities by specifing the available path, or we can also add new roles if needed. By default, I set three roles: user, admin and super admin.
### Menu Management
![](https://github.com/xiaokeliu666/Generic_Backend_Management/blob/latest_branch/src/main/resources/static/image/Menu.jpg?raw=true)
&nbsp;&nbsp;&nbsp;&nbsp;In this section, all the menus of this system is shown in a tree fashion and we can also add or delete menus by specifying its path and parent node.
### Real-time Monitor
![](https://github.com/xiaokeliu666/Generic_Backend_Management/blob/latest_branch/src/main/resources/static/image/Real-time%20Monitor.jpg?raw=true)
&nbsp;&nbsp;&nbsp;&nbsp;In this section, we can monitor the real-time status of device including CPU, memory, disk and so on. The page will automatically refresh per second 
### Real-time Logging
![](https://github.com/xiaokeliu666/Generic_Backend_Management/blob/latest_branch/src/main/resources/static/image/Real-time%20logging.jpg?raw=true)
&nbsp;&nbsp;&nbsp;&nbsp;In this section, the backend loggings are printed on the page and I did some polish work to highlight some keyword so the readability is improved.
## How to run this project
Above are just part of the features, please find more fun by run this project on your device :)
1. Find the sql file in `Generic_Backend_Management/src/main/resources/static/sql/base_admin.sql` and run the script on your device.
2. Login 

&nbsp;&nbsp;&nbsp;&nbsp;There are three users, you can login with the account: sa, admin, scott with the same password 123456 to find out the different authorities among supaer admin, admin and user

## Relative Notes
1. [Define secured URLs dynamically](https://xiaokeliu666.github.io/2020/08/17/Spring-Security-Define-secured-URLS-dynamically/)
2. [Hybrid encryption](https://xiaokeliu666.github.io/2020/08/03/A-secure-scheme-for-data-exchange-AES-RSA/)

(Still updating...)
