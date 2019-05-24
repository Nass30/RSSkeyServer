package com.rsskey.server.Repository;

import com.rsskey.server.DAO.Exception.DAOException;
import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Models.Category;
import com.rsskey.server.Models.RSSFeedItem;
import com.rsskey.server.Utils.SQLHelper;

import java.sql.Timestamp;
import java.util.*;

public class CategoryRepository extends ARepository<Category> {

    public CategoryRepository(DAOFactory dao) {
        super(dao);
    }

    public Category add(Category model) {
        String query = "INSERT INTO public.category(\"userID\",\"category\") VALUES (?, ?)";
        Category DBModel = null;
        ArrayList<Long> result = null;
        RssFeedItemRepository repoRssItem = DAOFactory.getInstance().getRssFeedItemRepository();

        try {
            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,true, "categoryID", model.getUserID(),model.getCategory());

            if (result != null && result.size() > 0 ) {
                DBModel = this.findbyID(result.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return DBModel;
    }

    @Override
    public Boolean delete(Long ID) throws DAOException {
        Boolean toreturn = false;
        ArrayList<Long>  result = null;
        String query = "DELETE FROM public.category WHERE \"categoryID\"=?";

        try {
            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,false, null, ID);
            if (result != null && result.size() > 0)
                toreturn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    @Override
    public Category findbyID(Long ID) throws DAOException {
        Category cat = new Category();

        String query = "SELECT * FROM public.category where \"categoryID\"=?";
        System.out.println("Find category by Id" + ID);
        try {
            cat = (Category)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, cat ,ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cat;
    }

    public Category findCategoryByTitle(String categoryName, Long userID) {
        Category cat = new Category();

        String query = "SELECT * from public.category where \"category\"=? and \"userID\"=?";
        try {
            cat = (Category)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, cat ,categoryName, userID);

        } catch (Exception e) {
            e.printStackTrace();
            cat = null;
        }
        return cat;
    }

    public Boolean suscriber(Long catID, Long rssID) {

        String query = "INSERT INTO public.categorysuscriber(\"rssID\",\"categoryID\") VALUES (?, ?)";
        Boolean insert = true;
        RssFeedItemRepository repoRssItem = DAOFactory.getInstance().getRssFeedItemRepository();

        try {
            SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,false,"", rssID, catID);

        } catch (Exception e) {
            insert = false;
            e.printStackTrace();
        }
        return insert;
    }
    public List<Category> getAllCategories(Long userID) throws DAOException {
        Category category = new Category();
        List<Category> categories = new ArrayList<Category>();
        String query = "SELECT * FROM public.category where \"userID\"=?";

        try {
            categories = (ArrayList<Category>)(ArrayList<?>)SQLHelper.executeQuery(this.daoFactory.getConnection(),query, category, userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category update(Category model) throws DAOException {
        return null;
    }



}
