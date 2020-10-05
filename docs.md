**Analysis:**

The problem statement has 3 specific operations.
1st is to parse the file and get the lines from that
2nd is to call the outside api and get response from that
3rd is to write it in the output file

All 3 are impurities as they deal with IO and because of that I have thought of using zio to handle fuctional effects.  
Also I have used **dependency inversion principle** in the application.
It can be viewed that the domain package is actually not dependent on anything outside.
It has few declaration(skeletons), generic error class and a service which deals with the high level trait not the implementation
This approach helped me in writting proper isolated tests for the entire application.

For all the 3 IO tasks I used 2 different services to group them and the InputFileService is depends on them to execute the operation.
Zio is really suitable for this kind of application creation as it has a dependency parameter apart from the error and success types.

As any kind of streaming library was not allowed and also the input order matters(if not I could have used parallel execution) I just used chunksize in hitting the 3rd party api
I have noticed that if the chunksize is more sometimes the outiside application was not responding properly(which can happen in real-time application as well) I used small chunksize.
Of course that is part of the config and can be configurable.

I have catagorized all types of errors to 3 types and they are Validation error, Server error and File error and as the name suggests the errors belong to those specific types.
This helps the application produce proper logs at the place where the application is deployed and necessary action can be taken on top of that.

**Docs:**

All class have small description at the start which defines what that particular class is responsible for.
For tests instead of using any mocking library I used in-memory implementation. This is actually another feature of having a **zio-based application**
Specs/unit tests use in-memory implementation
Integration specs use the live implentation
Service spec is also written which uses in-memory runtime
LiveRuntime should be running to check the result of the application. It expects 2 program arguments which should be passed like:   
`input.txt output.txt`  
If directly running from command line then it can be started like:   
`sbt run input.txt output.txt`  
With any kind of errors(file error, validation error, outside server error) the proper message will be displayed in the console.
