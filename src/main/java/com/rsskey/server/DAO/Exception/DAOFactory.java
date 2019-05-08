package com.rsskey.server.DAO.Exception;

import com.rsskey.server.Repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOFactory {

    private static final String FICHIER_PROPERTIES       = "./src/main/resources/application.properties";
    private static final String PROPERTY_URL             = "spring.datasource.url";
    private static final String PROPERTY_DRIVER          = "driver";
    private static final String PROPERTY_NOM_UTILISATEUR = "spring.datasource.username";
    private static final String PROPERTY_MOT_DE_PASSE    = "spring.datasource.password";

    private String              url;
    private String              username;
    private String              password;

    DAOFactory( String url, String username, String password ) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /*
     * Méthode chargée de récupérer les informations de connexion à la base de
     * données, charger le driver JDBC et retourner une instance de la Factory
     */
    public static DAOFactory getInstance() throws DAOConfigurationException {
        /*Properties properties = new Properties();
        String url;
        String driver;
        String nomUtilisateur;
        String motDePasse;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new DAOConfigurationException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            url = properties.getProperty( PROPERTY_URL );
            driver = properties.getProperty( PROPERTY_DRIVER );
            nomUtilisateur = properties.getProperty( PROPERTY_NOM_UTILISATEUR );
            motDePasse = properties.getProperty( PROPERTY_MOT_DE_PASSE );
        } catch ( IOException e ) {
            throw new DAOConfigurationException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }

        try {
            Class.forName( driver );
        } catch ( ClassNotFoundException e ) {
            throw new DAOConfigurationException( "Le driver est introuvable dans le classpath.", e );
        }*/

        DAOFactory instance = new DAOFactory( "jdbc:postgresql://ec2-23-23-195-205.compute-1.amazonaws.com:5432/dlpb4374hpjs6", "buthoxyboesyxm", "314f4b7b014979cdc87b25945df38af7c50c4f43ade2bb8feb1af68b5f28aa18" );
        return instance;
    }

    /* Méthode chargée de fournir une connexion à la base de données */
    /* package */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection( url, username, password );
    }

    /*
     * Méthodes de récupération de l'implémentation des différents DAO (un seul
     * pour le moment)
     */
    public UserRepository getUserRepository() {
        return new UserRepository( this );
    }
}