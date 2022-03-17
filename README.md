# RSA Shamir Sharding CLI
## About
This project was completed entirely in Java. I chose Java since it is easy to understand, runs on virtually any machine, and is my best language. Using an object-oriented language allowed me to take advantage of object-oriented design patterns such as the model-view-controller and command design patterns, which I implemented to make the project maintainable, extensible, and modular. <br>
I found this project very rewarding, as it allowed me to think critically about some of the challenges and design choices regarding implementing a cryptosystem such as RSA in software. <br>
In the future, I'd love to flesh out the functionality of the encryption and decryption methods. I currently use the RSA public key to encrypt and RSA private key to encrypt messages as the assignment asked, but want to implement the use of AES keys that are encrypted using RSA keys to make the CLI able to encrypt and decrypt longer messages more efficiently. Furthermore, needing to use the RSA public key to encrypt messages meant I didn't have the access I would have liked to padding schemes, which would make my encryption secure against chosen plaintext attacks. Using an AES key to encrypt longer (padded) messages would solve this issue.

## How To Build The App
Building is easy, since the whole project relies on Maven dependencies to implement outside libraries. The best way to build the project is to pull the entire project from the public GitHub repo (<code>git pull https://github.com/amburke815/RSAShamirShardCLI.git</code>), and open the project in a Java IDE with Maven support such as IntelliJ or Eclipse. Run the maven dependencies included in <code>/pom.xml</code> to set up all of the libraries, and then build the project targeting <code>src/java/com/company/Main.java</code>. <br>
Alternatively, this can be done from the command line:
<code>sudo apt install maven</code> to install maven, <br>
<code>cd</code> into <code>/RSAShamirShardCLI</code>, <br>
<code>mvn package</code> to build dependencies, <br>
<code>javac src/java/com/company/Main.java</code> to build the java project

## How to Run The CLI Program
Pull <code>out/artifacts/RSAShamirShardCLI_jar/RSAShamirShardCLI.jar</code> from the public GitHub repo and... <br>
<i>Windows:</i> Navigate to the above directory in file explorer, right-click on
<code>RSAShamirShardCLI.jar</code> and select <b>Open with</b>, then <b>Java(TM) Platform SE binary</b>. If this fails you may need to install java [here](#https://www.java.com/download/ie_manual.jsp). <br>
<i>Linux:</i> <code>cd</code> into the directory containing RSAShamirShardCLI.jar and run <code>java -jar RSAShamirShardCLI.jar</code>

## How To Run the Unit Test
The best way to run the unit test is to do so from within an IDE with Maven support such as IntelliJ or Eclipse. Simply pull the public GitHub repo, run the <code>/pom.xml</code> using Maven to build the testing library, and natively execute the unit test in /test/java/tests/KeyShardingTests.java called <code>testProgramWorksCorrectly()</code>. <br>
Alternatively, you can do this from the command line: <br>
<code>$ sudo apt install maven</code> to install maven, <br>
<code>$ cd</code> into <code>/RSAShamirShardCLI</code>, <br>
<code>$ mvn package</code> to build dependencies, <br>
<code>$ javac src/java/com/company/Main.java</code> to build the java project <br>
<code>$ mvn -Dtest=KeyShardingTests#testProgramWorksCorrectly() test</code> to run the unit test


