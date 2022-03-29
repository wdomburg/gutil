INSTALL=/usr/bin/install
TAR=/usr/bin/tar

class:
	./gradlew

clean:
	./gradlew clean

jar:
	./gradlew jar

doc:
	./gradlew javadoc
