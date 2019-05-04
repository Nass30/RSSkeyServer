package com.rsskey.server.DatabaseUtils;

import com.rsskey.server.Utils.SQLHelper;

import java.sql.*;
import java.util.Dictionary;
import java.util.Map;

public class DbConnect {
    private static DbConnect ourInstance = new DbConnect();

    public static DbConnect getInstance() {
        return ourInstance;
    }

    private Connection conn;

    private DbConnect() {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://localhost:5432/JavaServer";
            String user = "postgres";
            String passwd = "azerty123";


            this.conn = DriverManager.getConnection(url, user, passwd);
            SQLHelper.connect = this.conn;
            System.out.println("Connexion effective !");

        } catch (Exception e) {
            e.printStackTrace();
            this.conn = null;
        }
    }

    public Connection getCon() {
        return this.conn;
    }

    public ResultSet ExecSqlCommand(String query, Map<Integer, Object> params) {
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = conn.prepareStatement(query);

            /*
            for ( Map.Entry<Integer, Object> entry : params.entrySet() ) {
                String key = entry.getKey();
                Label value = entry.getValue();
                preparealue = entry.getValue();
            }*/

            //On remplace le premier trou par le nom du professeur
            prepare.setString(1, "MAMOU");
            //On remplace le deuxième trou par l'identifiant du professeur
            prepare.setInt(2, 2);
            //On affiche la requête exécutée
            System.out.println(prepare.toString());
            //On modifie le premier trou
            prepare.setString(1, "TOTO");
            //On affiche à nouveau la requête exécutée
            System.out.println(prepare.toString());
            //On modifie le deuxième trou
            prepare.setInt(2, 159753);
            //On affiche une nouvelle fois la requête exécutée
            System.out.println(prepare.toString());

            prepare.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
