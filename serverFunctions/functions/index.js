const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

var FCM = require('fcm-push');
var serverKey = 'AAAAi5_TNwA:APA91bH1o5VHhehoElly9IKcUVQqQk2w0521OvoQ-OTeQgdKoNljZkmY7OH-FRIiOf3LUQBqDGDbRkkyR9RTmGpetBkZcLSSiu4A4SAuyPF7i64E1aP3FydC9UaO95UM9u_OULk3S7ZN';
var fcm = new FCM(serverKey);

exports.sendNotification = functions.database.ref("/SingleNotification/{post_id}/{to_id}/{from_id}/{notification_id}").onCreate(event => {
    const to_id = event.params.to_id;
    const notification_id = event.params.notification_id;
    const deviceToken = admin.database().ref(`/users/${to_id}/deviceToken`).once('value');

    return deviceToken.then(result => {
        const token_id = result.val();
        const payload = {
            notification: {
                title: 'Kindred',
                body: event.data._delta.message,
                icon: 'default',
                click_action: 'com.kindred.kindred.TargetNotification'
            },
            data: {
                post_id: event.params.post_id,
                detail_msg: event.data._delta.detail_msg,
                from_user_id: event.params.from_id
            }
        };

        return admin.messaging().sendToDevice(token_id, payload).then(reponse => {
            return console.log(`A new notification for ${to_id}`);
        });
    });
});

exports.orderNotification = functions.database.ref("/OrderNotification/{post_id}").onCreate(event => {

    var tokenIDs = [];
    const from_user_id = event.data._delta.from;

    admin.database().ref(`/users`).once('value', result => {
        result.forEach(singleUser => {
            const u_id = singleUser.key;
            if (u_id !== from_user_id) {
                const orderNotify = singleUser.child('notifyOrder').val().toString();
                if (orderNotify === "true") {
                    if (singleUser.hasChild('deviceToken')) {
                        tokenIDs.push(singleUser.child('deviceToken').val());
                        console.log(`A device add to list`);
                    } else {
                        console.log(`No deviceToken saved`);
                    }
                }else{
                    console.log(singleUser.child('name').val() + ` not subscribed to order notification`);
                }
            }
        });

        console.log(tokenIDs);

        if (typeof tokenIDs !== 'undefined' && tokenIDs.length > 0) {
            var message = {
                registration_ids: tokenIDs, // required fill with device token or topics
                collapse_key: 'post',
                data: {
                    post_id: event.params.post_id,
                    detail_msg: event.data._delta.detail_msg
                },
                notification: {
                    title: 'Kindred',
                    body: event.data._delta.message,
                    icon: 'order',
                    click_action: 'com.kindred.kindred.TargetOrderNotification'
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
            return console.error("Error");
        }
    }, error => {
        return console.log(`An error occur`);
    });


});