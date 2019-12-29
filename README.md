# Java Naming Analysis
Project attempting to determine the popularity of class names in Java-based projects on GitHub.

## Data Collection Approach
One big obstacle in this project were GitHub's rate limits for its Search API (see [documentation](https://developer.github.com/v3/search/)):
> The Search API has a custom rate limit. For requests using Basic Authentication, OAuth, or client ID and secret, you can make up to 30 requests per minute. For unauthenticated requests, the rate limit allows you to make up to 10 requests per minute.

Additionally it is not only the number of request that is rate-limited, requests with a lot of results also 
have a high probability (and by that I mean it is basically guaranteed that they will) of timing out. 
And not only that, the response won't feature all results in one batch, but instead group them by pages 
with a maximum of a 30 results by default, meaning you need multiple queries to retrieve all pages.

All of these limits make it somewhat problematic and very hard to retrieve large 
amounts of data that can be analysed (and you manage to trigger 
abuse detection mechanisms very easily when testing possible approaches to retrieve it), seeing as GitHub has over 147M repositories, many of which will be in Java 
and contain multiple Java classes.

There are several approaches that could be used to try and collect useful data, for example:
- get all Java-based repositories, then query each for Java files they contain: 
here the problem is the large number of requests needed to query every single found repository.
- directly search for Java files: here one query will return be very incomplete because of the large number of results.

There are also several ways to deal with the limitations:
- in terms of the number of requests, if completeness is valued, time your requests and wait every time a rate limit is reached.
- in terms of time-outs, divide your data into "chunks", e.g according to date created or file size.
- generally you have to accept that you probably won't be able to get large amounts of data, and it is better to sample a small subset as an example.

The approach I decided to go with is searching for code in Java files directly (containing the work "class" since you 
can't search code without a keyword), sampling according to file sizes (since annoyingly the creation date parameter 
doesn't seem to work for code search), and sorting according to most recently indexed files, ignoring any timeouts.

The name of a class will be defined as the file name in this case (e.g "Test.java" counts as the class name "Test") since 
analysis of code directly to find instances of e.g "class Test { ... }" would be more complex.

## Setup

TODO

## Results

TO DO