const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

var FCM = require('fcm-push');

var serverKey = 'AAAAi5_TNwA:APA91bH1o5VHhehoElly9IKcUVQqQk2w0521OvoQ-OTeQgdKoNljZkmY7OH-FRIiOf3LUQBqDGDbRkkyR9RTmGpetBkZcLSSiu4A4SAuyPF7i64E1aP3FydC9UaO95UM9u_OULk3S7ZN';
var fcm = new FCM(serverKey);


exports.sendNotification = functions.database.ref("/Notification/{post_id}").onCreate(event => {
    var tokenIDs = [];
    const from_user_id = event.data._delta.from;

    admin.database().ref(`/users`).once('value', result => {
        result.forEach(singleUser => {
            const u_id = singleUser.key;
            if (u_id !== from_user_id) {
                if (singleUser.hasChild('deviceToken')) {
                    tokenIDs.push(singleUser.child('deviceToken').val());
                    console.log(`A device add to list`);
                } else {
                    console.log(`No deviceToken saved`);
                }
            }
        });

        console.log(tokenIDs);

        if (typeof tokenIDs !== 'undefined' && tokenIDs.length > 0) {
            var message = {
                registration_ids: tokenIDs, // required fill with device token or topics
                collapse_key: 'post',
                data: {
                    post_id: event.params.post_id
                },
                notification: {
                    title: 'Kindred',
                    body: event.data._delta.message,
                    icon: 'default',
                    click_action: 'com.kindred.kindred.TargetNotification'
                }
            };

            //promise style
            return fcm.send(message)
                .then(response => {
                    return console.log("Successfully sent with response: ", response);
                })
                .catch(err => {
                    console.log("Something has gone wrong!");
                    return console.error(err);
                })
        } else {
            return 1;
        }
    }, error => {
        return console.log(`An error occur`);
    });


});