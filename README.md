# FabricDependencyHacker
Does your favorite mod say it requires minecraft 1.17, but you are pretty sure that it should 
work on 1.17.1? Well then this is for you. You can change dependencies however you like! You can 
even add them!

##Disclaimer
There is no guarantee that mods with edited dependencies will work properly. It's very likely that a dependency is 
actually necessary. Modifying a jar is also likely to void any warranty the original had. I am not responsible for 
any damage caused by the software or any of outputs. This should be used for testing or personal 
use only, for modpacks or production servers.

##Usage
Once you have downloaded a jar from the releases page, you'll need to make sure that you have
java 16 installed. You can get it from [here](https://adoptopenjdk.net/?variant=openjdk16&jvmVariant=hotspot). After 
that you should be able to run the jar by double-clicking on it. If you get a JNI error, make 
sure that java 16 is set as the default app for `.jar` files. 
Once you have the program open, you can open a file with the `load` button, edit dependencies in 
the table and save using the `save` button. The mod will be saved as `original-name-hacked.jar` 
but you are free to rename it.

##Note 
Mods usually break with major releases of minecraft. You can usually get away with changeing 
dependencies for minor releases or snapshots, but that once again is not a guarantee.