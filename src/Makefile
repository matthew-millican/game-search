JC = javac
FO = .java
FC = .class
J = java
OPTIONS = -g
DIR = -d out
JAR = junit-platform-console-standalone-1.7.0.jar
CLASSPATH = -cp $(JAR)
JARFILE = -jar $(JAR)
SCAN = --class-path out --scan-class-path
SCANEVAL = --class-path eval --scan-class-path

all: tests evaluation

run:
	$(J) BHMain


build:
	$(JC) $(OPTIONS) BHMain$(FO)

tests: build stacscheck jar junit

evaluation:	perform analysis

junit:
	$(J) $(JARFILE) $(SCAN)


stacscheck:
	 stacscheck /cs/studres/CS3105/Practicals/P1/Tests/


jar:
	$(JC) $(DIR) $(CLASSPATH) layouts/*.java types/*.java common/*.java test/*.java functions/*.java

perform:
	$(JC) -d eval $(CLASSPATH) layouts/*.java types/*.java common/*.java performance/*.java functions/*.java

analysis:
	$(J) $(JARFILE) $(SCANEVAL)


clean:
	rm ./*$(FC)
