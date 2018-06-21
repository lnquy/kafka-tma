# Kafka Security (SSL & SASL)
No code change must me made to enable these security features on Kafka brokers, Kafka clients and Zookeeper but you must confgigure everything correctly to make it work :)
Please take a look on some notes below as well as read the Kafka/Confluent documentation for security carefully.

##### KafkaSSL

- No code change must be made to enable Kafka SSL encryption and authentication, but you must generate SSL key/certificate, and configure correctly to make thing works. Details below:
- https://docs.confluent.io/current/kafka/authentication_ssl.html

##### KafkaKerberos (SASL)

- https://docs.confluent.io/current/kafka/authentication_sasl_gssapi.html
- https://www.confluent.io/blog/apache-kafka-security-authorization-authentication-encryption/
- Same as KafkaSSL, no code change must be made to enable Kafka SASL (Kerberos) authentication but you should keep in mind some notes below:

  1. ​Make sure the `krb5-workstation` and `krb5-libs` are available on the machine performing Kerberos auth request (client).  
     P/s: All commands is on RHEL 7, it may differ on other OS.

     ```shell
     $ # yum list | grep krb5
     krb5-libs.x86_64                        1.15.1-18.el7              @/krb5-libs-1.15.1-18.el7.x86_64
     krb5-workstation.x86_64                 1.15.1-18.el7              @/krb5-workstation-1.15.1-18.el7.x86_64

     # Install these libs if not avalable
     $ sudo yum install krb5-libs krb5-workstation
     ```

  2. Make sure you have correct Kerberos configuration on `/etc/krb5.conf` file.  
     The simplest way is you just have to copy the file on your Kerberos KDC server and overwrite it on your clients at `/etc/krb5.conf`.

     ```shell
     $ scp kdc-user@kdc.server.address:/etc/krb5.conf /etc/krb5.conf
     ```

  3. Should pass the argument `-Djava.security.krb5.conf=/etc/krb5.conf` to your Java process requesting Kerberos authentication.

  4. Make sure you pass the correct path to the keytab file on your Kafka Kerberos configuration.  
     The keytab file must be readable by your Java process (make sure user who starting your Java process must have READ permission on the keytab file).  
     **Most of time you will fail at this step, so be careful!**

  5. Make sure you have the correct principal to authenticate on keytab file. You can check keytab's principal by:

     ```shell
     # Make sure user who executing klist command also have read permission on keytab file, or you just have to run it as root :D
     $ klist -ket /path/to/your/keytab/file
     Keytab name: FILE:vsadm.keytab
     KVNO Timestamp           Principal
     ---- ------------------- ------------------------------------------------------
        4 11/06/2018 15:19:18 vsadm@LOCALDOMAIN (aes128-cts-hmac-sha1-96) 
        4 11/06/2018 15:19:19 vsadm@LOCALDOMAIN (des3-cbc-sha1) 
        4 11/06/2018 15:19:19 vsadm@LOCALDOMAIN (arcfour-hmac) 
        4 11/06/2018 15:19:19 vsadm@LOCALDOMAIN (camellia256-cts-cmac) 
        4 11/06/2018 15:19:19 vsadm@LOCALDOMAIN (camellia128-cts-cmac) 
        4 11/06/2018 15:19:19 vsadm@LOCALDOMAIN (des-hmac-sha1) 
        4 11/06/2018 15:19:19 vsadm@LOCALDOMAIN (des-cbc-md5)
     ```

  6. Make sure the time on your machine is in-synced with time on Kerberos KDC server (use NTP here).  
     **This is also the step you will likely to failed at.**
