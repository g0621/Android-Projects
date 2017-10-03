'use strict'


const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/Notifications/{receiver_id}/{notification_id}')
    .onWrite(event =>
    {
       const receiver_id = event.params.receiver_id;
       const notification_id = event.params.notification_id;

       console.log('we have a notification to send to ' , receiver_id);

       if (!event.data.val()){
           return console.log('A notification has been deleted from the database ',notification_id);
       }

       const sender_id = admin.database().ref(`/Notifications/${receiver_id}/${notification_id}`).once('value');
       return sender_id.then(fromUserResult =>
           {
                const from_sender_id =  fromUserResult.val().from;

                const sent_query = admin.database().ref(`/Users/${from_sender_id}`).once('value');
                return sent_query.then(UserNameResult =>
                    {
                        const sender_name = UserNameResult.val().user_name;
                        const sender_status = UserNameResult.val().user_status;
                        const sender_image = UserNameResult.val().user_image;
                        const deviceToken = admin.database().ref(`/Users/${receiver_id}/device_token`).once('value');
                        return deviceToken.then(result =>
                                                {
                                                         const token_id = result.val();
                                                            const payload =
                                                                {
                                                                    data :
                                                                        {
                                                                            click_action : "com.example.gyan.intouch_TARGET_NOTIFICATION",
                                                                            from_sender_id : from_sender_id,
                                                                            sender_name : sender_name,
                                                                            sender_image : sender_image,
                                                                            sender_status : sender_status
                                                                        }
                                                                };
                                                                return admin.messaging().sendToDevice(token_id,payload).then(response =>
                                                                                            {
                                                                                                     console.log("done sending notification..");
                                                                                            });
                                                });
                });
            });



    });

