# Collect feedback

MirrorGate provides a markets collector that is able to capture feedback from the iOS and Android maketplaces. As there might be a lot other sources for user feedback, it also enables to send reviews directly using a simple HTTP post mechanism.

## How to send a review directly

The following api endpoint allows users to send feedback via an html form, so that teams can collect it and read it on their dashboard.

http://[mirrorgate-host]/mirrorgate/reviews/{appid}

where {appid} corresponds to the name of the application.

The html form must send three parameters:
- rate: contains the application score (must be a number from 1 to 5).
- comment: contains the user comment about the application.
- url (optional): contains the url to be redirected after sending feedback.

An example below of a simple html form (for a hypothetical 'foobar' application):
```html
<html>
    <body>
        <header>
            <title>Send feedback to MirrorGate</title>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        </header>
        <form method="POST" action="http://localhost:8080/mirrorgate/reviews/foobar" id="form" name ="form">
            Rate: <input type="text" id="rate" name="rate" value="" />
            Comment: <input type="text" id="comment" name="comment" value="" />
            <input type="hidden" id="url" name="url" value="http://localhost:8080/mirrorgate/" />
            <input type="submit" value="Submit"/>
        </form>
    </body>
</html>
```

> In order to display the feedback in MirrorGate, the "Mirrorgate/" prefix must be included before the application name in the "Application Markets" section of dashboard configuration. Example (for the same 'foobar' application): "Mirrorgate/foobar".