
# FOS Visualization 

## Presentation 
This is a data visualization school project, allowing to display Field of Study (FOS) of some research paper 
## Dataset 
The dataset is composed of two CSV files. 
### dblp.v11.csv 
This csv lists many research papers. Each research paper is composed of the following fields:
"id","title","authors","year","n_citation","fos"
- id
- title
- authors -> authors with their given id
- year -> published year of this paper
- n_citation -> number of citation of this paper
- fos -> fields of studies in the format name:weight. Weights range  from 0 to 1

In this project, I only used the year (to display data by year), n_citations and the FOSs.

### fos.csv
This csv lists all the FOSs and their number of occurrence. Since there are 18901 FOS, I filtered them to keep only the ones that have at least 100 occurences.
When I encounter a filtered FOS in the `dblp.v11.csv` file, I simply ignore it.


## Visualisation (not finished yet)
![screenshot](https://raw.githubusercontent.com/tambapps/fos-visualisation/master/screenshots/screen1.png "Screenshot")

Each bubble represent one FOS. The the FOS were frequent in all of the articles parsed, the bigger the bubble is. 

Each bubble is colored from blue to red. Blue means that among all articles containing this FOS, these articles weren't cited a lot. 
In opposite, a red color would mean that theses articles were cited a lot. 

There may be some link between two bubbles (FOSs) If the two FOSs were frequently seen in the same articles, the link will be thick, if they were only saw a few times, it will be thin, and if they never have been in the same article, there will be no link
The text is still missing: each bubble should have its FOS displayed.

## Interaction
You can interact with the data in many ways
### Zoom
by pressing `z` and up/down arrow, you can zoom in/out the screen

### Links threshold
By default, all links are displayed. Where there are many links, the application starts to become slow, so you can press `l` and up arrow to rise the links threshold and hide all links  below this threshold. press `l` + down to lower it

### Bubbles threshold
Same as the link threshold, you can press `b` + up/down to modify the value of the bubble threshold. All bubble hidden will also have their links hidden too.

### Shuffle
You don't like how the bubbles are disposed? Shuffle them by clicking on `s`.

### Move a bubble
Click on a bubble and while you've pressed down, move the mouse, the bubble will move along. Release the mouse button to stop moving the bubble

Note that clicking on a bubble will also display its FOS in the console

### Go through years
You can change the current year displayed by clicking on `y` (+1) or `h` (-1). 

When you change a year, we compute the bubbles added, and the one removed to add/remove them with an animation. 

All bubbles that still remains in the new year stays in the same place, so that you don't get confused each time you change year.


## How to run
The LWjgl (and OpenGL) configuration is by a generated pom.xml. You can find this tool [here](https://www.lwjgl.org/customize).

I used the dependencies for Linux x64.

This project uses Java 11. Before going next, make sure your JAVA_HOME is set to your java 11 path.
Once your pom is well configured and you use Java 11,  run

```bash
mvn install
```
This will build the jar, and then run

```bash
java -jar target/papernet-1.0-SNAPSHOT-jar-with-dependencies.jar
```
