package com.rsskey.server.Utils;

import com.rsskey.server.DAO.Exception.DAOException;
import com.rsskey.server.Models.IModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLHelper {

    public static Connection connect;

    public static List<Map<String,Object>> ExecSqlCommand(String query, Map<Integer, Object> params) {
        List<Map<String, Object>> map = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        try {
            Statement state = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = connect.prepareStatement(query);


            for ( Map.Entry<Integer, Object> entry : params.entrySet() ) {
                prepare.setObject(entry.getKey(),entry.getValue());
            }
            ResultSet res = prepare.executeQuery();

            ResultSetMetaData resultMeta = res.getMetaData();

            //On affiche le nom des colonnes
            for(int i = 1; i <= resultMeta.getColumnCount(); i++)
                columns.add(resultMeta.getColumnName(i).toUpperCase());


            while (res.next()) {
                Map<String,Object> tmpmap = new HashMap<>();

                for(int i = 1; i <= resultMeta.getColumnCount(); i++)
                    tmpmap.put(columns.get(i - 1),res.getObject(i));
                map.add(tmpmap);
            }
            prepare.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static ArrayList<Long> executeNonQuery(Connection connexion, String sql, boolean returnGeneratedKeys, Object... objets )
    {
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        ArrayList<Long> ids = new ArrayList<>();

        try {
            /* Récupération d'une connexion depuis la Factory */
            preparedStatement = initialisationRequetePreparee( connexion, sql, returnGeneratedKeys, objets );
            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }
            /* Récupération de l'id auto-généré par la requête d'insertion */
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                /* Puis initialisation de la propriété id du bean Utilisateur avec sa valeur */
                ids.add( valeursAutoGenerees.getLong( 1 ) );
            } /* else {
                throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
            } */
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            closeCleanning( valeursAutoGenerees, preparedStatement, connexion );
        }
        return ids;
    }

    public static Object executeQuery(Connection connexion, String sql, boolean returnGeneratedKeys, IModel model, Object... objets )
    {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object DBModel = null;

        try {

            preparedStatement = initialisationRequetePreparee( connexion, sql, returnGeneratedKeys, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                DBModel = model.map( resultSet );
                System.out.print("TEST SQL EXECUTION !!");
                System.out.print(model.toString());
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            closeCleanning( resultSet, preparedStatement, connexion );
        }
        return DBModel;
    }
    /*
     * Initialise la requête préparée basée sur la connexion passée en argument,
     * avec la requête SQL et les objets donnés.
     */
    public static PreparedStatement initialisationRequetePreparee( Connection connexion, String sql, boolean returnGeneratedKeys, Object... objets ) throws SQLException {
        System.out.print("passe par la !" + objets.length);
        PreparedStatement preparedStatement = connexion.prepareStatement( sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
        for ( int i = 0; i < objets.length; i++ ) {
            preparedStatement.setObject( i + 1, objets[i] );
        }
        return preparedStatement;
    }

    /* Fermeture silencieuse du resultset */
    public static void closeCleanning( ResultSet resultSet ) {
        if ( resultSet != null ) {
            try {
                resultSet.close();
            } catch ( SQLException e ) {
                System.out.println( "Échec de la fermeture du ResultSet : " + e.getMessage() );
            }
        }
    }

    /* Fermeture silencieuse du statement */
    public static void closeCleanning( Statement statement ) {
        if ( statement != null ) {
            try {
                statement.close();
            } catch ( SQLException e ) {
                System.out.println( "Échec de la fermeture du Statement : " + e.getMessage() );
            }
        }
    }

    /* Fermeture silencieuse de la connexion */
    public static void closeCleanning( Connection connexion ) {
        if ( connexion != null ) {
            try {
                connexion.close();
            } catch ( SQLException e ) {
                System.out.println( "Échec de la fermeture de la connexion : " + e.getMessage() );
            }
        }
    }

    /* Fermetures silencieuses du statement et de la connexion */
    public static void closeCleanning( Statement statement, Connection connexion ) {
        closeCleanning( statement );
        closeCleanning( connexion );
    }

    /* Fermetures silencieuses du resultset, du statement et de la connexion */
    public static void closeCleanning( ResultSet resultSet, Statement statement, Connection connexion ) {
        closeCleanning( resultSet );
        closeCleanning( statement );
        closeCleanning( connexion );
    }

}
