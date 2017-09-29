** Number of Nodes (m bits system) is hard codes as 3 for now. Will be made configurable after testing.
Its in ChordNode.java

public class ChordNode implements Node {

   public static final int SYSTEM_SIZE_IN_BITS = 3;



** How to Run:
==============

1. Go to bin directory. For example d:\Bhavin\CSU\cs555_A2\ChordSystem\bin

2. Run Discovery node on some port. Lets say 5000
    d:\Bhavin\CSU\cs555_A2\ChordSystem\bin>java A2.DiscoveryNode 5000

3. Run Node with id 1
    d:\Bhavin\CSU\cs555_A2\ChordSystem\bin>java A2.ChordNode 169.254.215.195 5000 1

4. Run Node with id 7
    d:\Bhavin\CSU\cs555_A2\ChordSystem\bin>java A2.ChordNode 169.254.215.195 5000 7

Please enter "details" / "fingertable" to print node specic detail on respective command prompt.

5. Run Node with id 4
    d:\Bhavin\CSU\cs555_A2\ChordSystem\bin>java A2.ChordNode 169.254.215.195 5000 4


TODO:
- Not consistent.
- Reduce duplication in commands
- Simplify CommandFactory. Load class from command name as Convention.
- Synchronozation?
- Thread to serve each request
- Test for 5 bit / 7 bit ids
- As of now id's are in int.
- Handling of image files.