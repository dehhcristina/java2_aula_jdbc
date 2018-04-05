package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Autor;
import util.ConnectionJDBC;

public class AutorDAO {
    
    Connection connection;
    
    public AutorDAO() throws Exception {
        //Obtem uma conexao
        connection = ConnectionJDBC.getConnection();
    }
    //modelo com generator
    public void save(Autor autor) throws Exception {
        String SQL = "INSERT INTO AUTOR(NOME) VALUES (?)";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setString(1, autor.getNome());
            p.execute();
            p.close();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }
    
    public void update(Autor autor) throws Exception {
        String SQL = "UPDATE AUTOR SET NOME=? WHERE AUTOR_ID=?";
         try {
             PreparedStatement p = connection.prepareStatement(SQL);
             p.setString(1, autor.getNome());
             p.setInt(2, autor.getAutor_id());
             p.execute();
             p.close();
         } catch (SQLException ex) {
             throw new Exception(ex);
         }
    }
    
    public void delete(Autor autor) {
        
    }
    
    
    public List<Autor> findAll() throws Exception {
        List<Autor> list = new ArrayList<>();
        Autor objeto;
        String SQL = "SELECT * FROM AUTOR ORDER BY AUTOR_ID";
        try{
            PreparedStatement p = connection.prepareStatement(SQL);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                objeto = new Autor();
                objeto.setAutor_id(rs.getInt("autor_id"));
                objeto.setNome(rs.getString("nome"));
                
                list.add(objeto);
            }
            rs.close();
            p.close();
        }catch (SQLException ex){
            throw new Exception(ex);
        }
        
        return list;
    }
}
