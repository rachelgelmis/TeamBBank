# CS 499 - Senior Project - Bank Management Utility
A banking application consisting of two parts: A standalone desktop application for employees and a web application for customers
## Project Title- Team B is for Bank | 
CS 499 Spring 2020 Authors: Alex Barr, Jackson King, Rachel Gelmis, Stephen Petersen
## Motivation
Purpose: CS 499- Senior Design is meant to provide students with a realistic software engineering experience. Students are divided into teams and given a random project assignment to work on throughout the course of the semester. The professor will serve as the customer’s technical representative.
This format is to give students an opportunity to be exposed to modern software development processes, including team development and Agile software development. Teams will be expected to work in 2 week sprints, presenting at the end of each sprint on their work so far.
Team B’s project is a 2-part banking application. The first part, a standalone application, is for use by bank employees, either administrators or tellers. The second part, a web application, is for customer use.
## Features
Standalone:
Can log in as admin or teller Tellers:
View customer’s bank accounts Deposit into customers accounts Withdraw from customers accounts Transfer funds between accounts
Admins:
View employee account info Create account for admin, teller Delete an employee account Create a bank account for user Delete a bank account for user Delete a user account
Website:
Log in as customer
View all bank accounts and balances Generate report listing account activity Transfer funds between accounts (?) Pay bills (?)
## Instructions:
Steps to run the Bank Management Utility System Web Application.
1.​ ​Install PuTTY if you don’t already have it.
2.​ ​Open up PuTTY.
3.​ ​In the Host Name field type “sd-adb0048.cs.uah.edu” with no quotes.
4.​ ​Ensure the port is set to 22.
5.​ ​In the Saved Sessions field, type “teamb” and click the Save option to the right.
6.​ ​Now, expand the Connection subtree on the left.
7.​ ​Then expand the SSH subtree within it.
8.​ ​Select the Tunnels item in the subtree. A new info pane on the right will show.
9.​ ​In the Source Port field, type in 3306.
10.​ ​In the Destination field, type in localhost:3306.
11.​ ​Then click the Add button.
12.​ ​In the tree explorer on the left, scroll up to the top and select session.
13.​ ​Click the Save button again to save the tunnel options to the teamb session. 14.​ ​Now click Open. A terminal show show up.
15.​ ​Login as “delugach” with password “cs499”.
16.​ ​Once logged in, type in “cd webapp/” and hit Enter.

17.​ ​Now type in “java -jar webdemo-1.0.jar” and hit Enter. 18.​ ​The application is now started.
To view the website, use your real machine’s internet browser of your choice, and go to “bisforbank.pw”.
*Notice: Any customers created from the Standalone while the Web app is running will not have an account in the Web app until the Web app is restarted.
** If you have MySQL on your local machine, ensure that it is stopped.
