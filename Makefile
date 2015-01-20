JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Cluster.java \
	Kmeans.java \
	Average.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
