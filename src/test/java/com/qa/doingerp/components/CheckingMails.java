package com.qa.doingerp.components;

import com.github.javafaker.Faker;
import com.sun.mail.imap.IMAPStore;
import lombok.SneakyThrows;
import org.junit.Test;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;

public class CheckingMails {
    @Test
    public void test(){
        String stCHeck = "Nov-[CMP]";
        System.out.println(stCHeck.replace("[","").replace("]",""));
        Faker faker = new Faker();
        String number = faker.regexify("[A-Z]{4}");
        System.out.println(number);

    }

    @SneakyThrows
    public static  String check(String user,
                             String password) throws Exception {
            String url = null;
            Properties props = new Properties();

            props.put("mail.host", "69.175.122.42"); //SMTP Host
            props.put("mail.port", "993"); //TLS Port
            props.put("mail.transport.protocol", "imap");
            Session emailSession = Session.getDefaultInstance(props);
            //create the POP3 store object and connect with the pop server
            IMAPStore store = (IMAPStore) emailSession.getStore("imap");
            store.connect( user, password);
            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            // retrieve the messages from the folder in an array and print it
           // Message[] messages = emailFolder.getMessages();
           Message[] messages = emailFolder.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            System.out.println("messages.length---" + messages.length);

            int counter =0;
            while(true){
                counter++;
                Thread.sleep(2000);
                System.out.println("Waited for "+counter*2+" seconds");
                messages = emailFolder.search(
                        new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                if(messages.length >0 || counter>=60)
                    break;
            }

            for(Message e: messages) {


                if (e.getSubject().contains("Login code")) {
                    System.out.println(e.getSubject());
                    System.out.println(e.getMessageNumber());
                    System.out.println(e.getContent().toString());
                    //System.out.println(getText(e));
                     url = "https" + getText(e).split("https")[1].split("<br>")[0].trim();
                    System.out.println(url);
                    System.out.println("-------------");
                    break;
                }
            }



            //close the store and folder objects
            //mark all read message as true
            emailFolder.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
            //close the store and folder objects
            emailFolder.close(false);
            store.close();

    return url;
    }


    public  static String mail() throws Exception{

        String host = "69.175.122.42";// ip address of mail.doingerp.com
        String mailStoreType = "pop3";
        String username = "thanajayan@doingerp.com";// change accordingly
        String password = "Welc0me@12345";// change accordingly

        return check(username, password);

    }

    private static boolean textIsHtml = false;

    /**
     * Return the primary text content of the message.
     */
    private static String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }

}
