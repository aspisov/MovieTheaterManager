# Movie Theater Manager

A console application designed for movie theater employees to manage various aspects of a theater.

## Installation

To install this project, download and unzip the provided archive. Run `src/Main.kt` to start the application.

## Usage

Upon launching the app, you'll arrive at the registration menu, where you'll have three options: register, log in, or
exit.

Once you've completed the registration or login process, you will be directed to the main menu.

The main menu contains 8 options:

1. **Show all movies (edit):** Display all available movies. You have the option to delete or edit the details (name,
   duration) of each movie.
2. **Add new movie:** Add a new movie by entering its name, duration, and a brief description.
3. **Show and edit active sessions:** View all active sessions. Here, you can edit or delete selected sessions.
4. **Show all sessions:** View all sessions, including those that have ended and those currently active. Detailed
   information about each session is available.
5. **Add new session:** Create a new session. Select a movie and specify the start time.
6. **Sell ticket:** Sell tickets for an active session. Choose a session, then specify the seat number for ticket
   booking.
7. **Return ticket:** Process ticket returns for sessions that haven't started yet. Select the relevant session and
   specify the seat number.
8. **Scan ticket:** Scan tickets at the theater entrance. Tickets can be scanned starting one hour before the session
   begins.

To choose an option, enter its index number. Selecting an option not listed will return you to the registration menu.

To further enhance your understanding of this application you are welcome to look at class and use-case diagrams in
`/diagrams` directory.

## Movies and sessions data

All data about movies and session is stored in `/data` in JSON.<br>
The reasons why JSON was chosen for data storage are:

1. JSON can represent more complex and hierarchical data structures
2. JSON is more human-readable and easier to work with compared to XML
3. JSON has simpler parsing and serialization in Kotlin especially compared to XML

## User registration

Password encryption is implemented using hash and salt, inspired by concepts covered in this video:<br>
https://youtu.be/NuyzuNBFWxQ?si=_3KpfyEsHI0Fr3Yp