package uk.ac.dundee.computing.aec.instagrim.lib;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.*;

public final class Keyspaces {

    public Keyspaces() {

    }

    public static void SetUpKeySpaces(Cluster c) {
        try {
            //Add some keyspaces here
            String createKeyspace = "create keyspace if not exists instagrim  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
            String createPicTable = "CREATE TABLE if not exists instagrim.Pics ("
                    + " user varchar,"
                    + " picid uuid, "
                    + " interaction_time timestamp,"
                    + " title varchar,"
                    + " image blob,"
                    + " thumb blob,"
                    + " processed blob,"
                    + " imagelength int,"
                    + " thumblength int,"
                    + " processedlength int,"
                    + " type varchar,"
                    + " name varchar,"
                    + " width int,"
                    + " height int,"
                    + " PRIMARY KEY (picid)"
                    + ")";
            String createUserPicList = "CREATE TABLE if not exists instagrim.userpiclist (\n"
                    + "picid uuid,\n"
                    + "user varchar,\n"
                    + "pic_added timestamp,\n"
                    + "PRIMARY KEY (user,pic_added)\n"
                    + ") WITH CLUSTERING ORDER BY (pic_added desc);";
            String createAddressType = "CREATE TYPE if not exists instagrim.address (\n"
                    + "      street text,\n"
                    + "      city text,\n"
                    + "      zip int\n"
                    + "  );";
            String createUserProfile = "CREATE TABLE if not exists instagrim.userprofiles (\n"
                    + "      login text PRIMARY KEY,\n"
                    + "      password text,\n"
                    + "      first_name text,\n"
                    + "      last_name text,\n"
                    + "      email text,\n"
                    + "      is_email_private boolean,\n"
                    + "      addresses  map<text, frozen <address>>,\n"
                    + "      profilePicID uuid\n"
                    + "  );";
            
            String createComments = "CREATE TABLE if not exists instagrim.picturecomments (\n"
                + "      picid uuid,\n"
                + "      comment text,\n"
                + "      username text,\n"
                + "      time timestamp,\n"
                + " PRIMARY KEY (picid, time)"
                + "  ) WITH CLUSTERING ORDER BY (time DESC);";
            String createFollows = "CREATE TABLE if not exists instagrim.follows (\n"
                + "      user1 text,\n"
                + "      user2 text,\n"
                + "      time timestamp,\n"
                + " PRIMARY KEY (user1, time, user2)"
                + "  ) WITH CLUSTERING ORDER BY (time DESC);";
            String createRecentPics = "CREATE TABLE if not exists instagrim.recentpics (\n"
                    + "time timestamp,\n"
                    + "picid uuid,\n"
                    + "p_key int,\n" // this is a hack, since cassandra can't order by the partition key.
                    + "PRIMARY KEY (p_key, time)\n" // so we have a common primary key and use clustering order instead.
                    + ") WITH CLUSTERING ORDER BY (time DESC);";
            

            Session session = c.connect();
            try {
                PreparedStatement statement = session
                        .prepare(createKeyspace);
                BoundStatement boundStatement = new BoundStatement(
                        statement);
                ResultSet rs = session
                        .execute(boundStatement);
                System.out.println("created instagrim ");
            } catch (Exception et) {
                System.out.println("Can't create instagrim " + et);
            }

            //now add some column families 
            System.out.println("" + createPicTable);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(createPicTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create pic table " + et);
            }
            System.out.println("" + createUserPicList);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(createUserPicList);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user pic list table " + et);
            }
            System.out.println("" + createAddressType);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(createAddressType);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address type " + et);
            }
            System.out.println("" + createUserProfile);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(createUserProfile);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Profile " + et);
            }
            System.out.println("" + createComments);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(createComments);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Comments " + et);
            }
            
            try {
                SimpleStatement cqlQuery = new SimpleStatement(createFollows);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Follows " + et);
            }
            
            try {
                SimpleStatement cqlQuery = new SimpleStatement(createRecentPics);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Recent Pics " + et);
            }     


            session.close();

        } catch (Exception et) {
            System.out.println("Other keyspace or column definition error" + et);
        }

    }
}
