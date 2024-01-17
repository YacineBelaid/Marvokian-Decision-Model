Markovian Decision Model - Artificial Intelligence

You will each Markovian process model GenPolitique1.java and GenPolitique2.java inside the Java repertory

To launch the graphical interface simply use this command :
java -jar SerpentsEchelles.jar

To test the markovian processes do:
java GenPolitique1 < plateau1.txt > plateau1_pol1.txt
java -cp SerpentsEchelles.jar TestPolitique1 plateau1.txt plateau1_pol1.txt 100000
java -cp SerpentsEchelles.jar GenPolitique2 < plateau1.txt > plateau1_pol2.txt
java -cp SerpentsEchelles.jar TestPolitique2 plateau1.txt plateau1_pol1_optimale.txt plateau1_pol1_optimale.txt 1000000
java -cp SerpentsEchelles.jar TestPolitique3 plateau4.txt plateau4_pol1_naive.txt plateau4_pol1_optimale.txt 1000000


Author : Yacine Belaid