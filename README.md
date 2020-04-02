# Challonge API

ChallongeAPI for java is a simple API to create matches and update for [Challonge](https://challonge.com) Written by Bl0k and Rj (Blok#3221 & Rj#8314)

## Installation

Maven

Repo
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Depenency
```	
<dependency>
	    <groupId>com.github.blok601</groupId>
	    <artifactId>ChallongeAPI</artifactId>
	    <version>LATEST</version>
        <scope>COMPILE</scope>
	</dependency>
```

## Usage

# Constructor
```java
Challonge challonge = new Challonge("api-key", "username", "url", "Name", "description", GameType.DOUBLE);
```
# Methods
`challonge.post();`
Will create the bracket on challonge


`challonge.getParticipants().add("name");`
Will add the participant "name" to the list of participants

`challonge.addParticpants();`
This will add all the participants to the bracket online

`challonge.randomize();` Will randomize the seeds

`challonge.start();` Will set the tournament as started


`challonge.updateMatch(4, "test15")` Will update match 4 with the winner as test15

`challonge.getMatch(4);` Will return a jsonresponse of the match infomation

`challonge.getUrl();` Returns the url for the match


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
