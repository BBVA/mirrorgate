# Collect feedback

## How to send a review

The following api endpoint allows users to send feedback via an html form, so that teams can collect it and read it on their dashboard.

http://localhost:8080/mirrorgate/reviews/foobar/{appid}

where {appid} corresponds to the name of the application.

The html form must send three fields of type "text":
- rating: contains the application score (must be a number from 1 to 5).
- comment: contains the user comment about the application.
- url: contains the url to be redirected after sending feedback.

An example below of a simple html form (for a hypothetical 'foobar' application):
```
<html>
<header>
<title>Send feedback to MirrorGate</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</header>
<form method="POST" action="http://localhost:8080/mirrorgate/reviews/foobar" id="form" name ="form">
    Rating: <input type="text" id="rating" name="rating" value="" />
    Comment: <input type="text" id="comment" name="comment" value="" />
    Url: <input type="text" id="url" name="url" value="" />
    <input type="submit" value="Submit"/>
</form>
</html>
```