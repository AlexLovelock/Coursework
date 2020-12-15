package controllers;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("games/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class Games {

    /*This method selects all the upcoming games from the database using a select
    statement and sends it back to the tournaments.js file to display
    curl -s localhost:8081/games/list
     */
    @GET
    @Path("list")
    public String list() {
        System.out.println("Invoked Games.List()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT GameDescription, GameDate FROM Games");
            ResultSet results = ps.executeQuery();
            System.out.println("results: "+ results);
            int countUsers = results.getInt(1);
            System.out.println("no of users: "+ countUsers);
            while (results.next()==true) {
                JSONObject row = new JSONObject();
                row.put("GameDescription", results.getString(1));
                row.put("GameDate", results.getString(2));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }
    //API call to get one game with GameID
    // $ curl -s localhost:8081/games/getGame/3
    @GET
    @Path("getGame/{GameID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetUser(@PathParam("GameID") Integer GameID) {
        System.out.println("Invoked Users.GetUser() with UserID " + GameID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT GameDescription, GameDate FROM Games WHERE GameID = ?");
            ps.setInt(1, GameID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()== true) {
                response.put("GameDescription", results.getString(1));
                response.put("GameDate", results.getString(2));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }
    //API call to add a user to the database
    //curl -s localhost:8081/games/add -F GameDescription='This game was excellent...' -F GameDate='1/12/20'
    @POST
    @Path("add")
    public String GamesAdd(@FormDataParam("GameDescription") String GameDescription, @FormDataParam("GameDate") String GameDate) {
        System.out.println("Invoked Games.Add()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Games (GameDescription, GameDate) VALUES (?, ?)");
            ps.setString(1, GameDescription);
            ps.setString(2, GameDate);
            ps.execute();
            return "{\"OK\": \"Added user.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }

    /*This method updates a row in the games table by using the SQL update statement
    curl -s localhost:8081/games/update -F GameDescription='This game was excellent...' -F GameDate='1/12/20' -F GameID=3
     */
    @POST
    @Path("update")
    public String update(@FormDataParam("GameID") Integer GameID, @FormDataParam("GameDescription") String GameDescription, @FormDataParam("GameDate") String GameDate) {
        try {
            System.out.println("Invoked Games.Update/ UserID=" + GameID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Games SET GameDescription = ?, GameDate = ? WHERE GameID = ?");
            ps.setString(1, GameDescription);
            ps.setString(2,GameDate);
            ps.setInt(3, GameID);
            ps.executeUpdate();
            return "{\"OK\": \"Games updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("delete/")
    //curl -s localhost:8081/users/delete/6
    public static String delete(@FormDataParam("GameID") Integer GameID) {
        System.out.println("Invoked Games.Delete()");
        try {
            if (GameID == 0) {
                throw new Exception("One or more form data parameters are missing in the HTTP request. answers/delete ");
            }
            System.out.println("/games/delete GameId=" + GameID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Games WHERE GameID = ?");
            ps.setInt(1, GameID);
            ps.executeUpdate();
            //Server.Main.db.commit();
            return "{\"status\": \"OK\"}";
        } catch (Exception ex) {
            System.out.println("Error in Games/delete: " + ex.getMessage());
            return "{\"error\": \"Unable to delete game, please see server console for more info.\"}";
        }
    }

}




