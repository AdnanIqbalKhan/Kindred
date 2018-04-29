const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

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
            data:{
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