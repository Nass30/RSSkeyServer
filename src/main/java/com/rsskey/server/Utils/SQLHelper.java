package com.rsskey.server.Utils;

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

    public static String stringMakerByAttr(String query, String attr)
    {
        query.replaceAll("TEST", attr);
        // query.replaceAll("@ATTRVALUE", value);
        return query;
    }
}
