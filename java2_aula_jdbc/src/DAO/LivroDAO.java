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
import model.Editora;
import model.Livro;
import util.ConnectionJDBC;

public class LivroDAO {

    Connection connection;

    public LivroDAO() throws Exception {
        //conexao com  banco
        connection = ConnectionJDBC.getConnection();
    }

    //modelo com generator
    public void save(Livro livro) throws Exception {
        String SQL = "INSERT INTO LIVRO(editora_id, titulo, ano, descricao) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, livro.getEditora().getEditora_id());
            p.setString(2, livro.getTitulo());
            p.setInt(3, livro.getAno());
            p.setString(4, livro.getDescricao());
            p.execute();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }

    public void saveAutorLivro(Autor autor, Livro livro) throws Exception {
        String SQL = "INSERT INTO AUTOR_LIVRO VALUES(?, ?)";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, autor.getAutor_id());
            p.setInt(2, livro.getLivro_id());
            p.execute();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
        
    }
    
    public void update(Livro livro) throws Exception {
        String SQL = "UPDATE LIVRO SET EDITORA_ID=?, TITULO=?, ANO=?, DESCRICAO=? WHERE LIVRO_ID=? ORDER BY LIVRO_ID";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, livro.getEditora().getEditora_id());
            p.setString(2, livro.getTitulo());
            p.setInt(3, livro.getAno());
            p.setString(4, livro.getDescricao());
            p.setInt(5, livro.getLivro_id());
            p.execute();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }

    public void delete(Livro livro) throws Exception {
        String SQL = "DELETE FROM LIVRO WHERE LIVRO_ID=? ORDER BY LIVRO_ID";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, livro.getLivro_id());
            p.execute();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }

    public void deleteAutorLivro(Livro livro, Autor autor) throws Exception {
        //implementar
        String SQL = "DELETE FROM AUTOR_LIVRO WHERE LIVRO_ID=? AND AUTOR_ID=?";
        try{
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, livro.getLivro_id());
            p.setInt(2, autor.getAutor_id());
            p.execute();
            p.close();
        }catch (SQLException ex){
            throw new Exception(ex);
        }
    }
    
    public Livro findById(int id) throws Exception {
        Livro objeto = new Livro();
        String SQL = "SELECT L.*, E.NOME, E.MUNICIPIO FROM LIVRO L "
                + "INNER JOIN EDITORA E ON E.EDITORA_ID = L.EDITORA_ID "
                + "WHERE LIVRO_ID = ? ORDER BY LIVRO_ID";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                objeto = new Livro();
                objeto.setLivro_id(rs.getInt("livro_id"));
                objeto.setTitulo(rs.getString("titulo"));
                objeto.setAno(rs.getInt("ano"));
                objeto.setDescricao(rs.getString("descricao"));

                Editora editora = new Editora();
                editora.setEditora_id(rs.getInt("editora_id"));
                editora.setNome(rs.getString("nome"));
                editora.setMunicipio(( rs.getString("municipio")));
                
                objeto.setEditora(editora);
            }
            rs.close();
            p.close();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }

        objeto.setAutores( findAutoresLivro( objeto.getLivro_id()) );
        return objeto;
    }

    public List<Livro> findAll() throws Exception {
        List<Livro> list = new ArrayList<>();
        Livro objeto;
        String SQL = "SELECT L.*, E.NOME FROM LIVRO L INNER JOIN EDITORA E ON E.EDITORA_ID = L.EDITORA_ID ORDER BY LIVRO_ID";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                objeto = new Livro();
                objeto.setLivro_id(rs.getInt("livro_ID"));
                objeto.setTitulo(rs.getString("titulo"));
                objeto.setAno(rs.getInt("ano"));
                objeto.setDescricao(rs.getString("descricao"));

                Editora editora = new Editora();
                editora.setEditora_id(rs.getInt("editora_id"));
                editora.setNome(rs.getString("nome"));

                objeto.setEditora(editora);

                list.add(objeto);
            }
            rs.close();
            p.close();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
        return list;
    }
        private List<Autor> findAutoresLivro(int livroId) throws Exception {
            List<Autor> lista = new ArrayList<>();
            String SQL = "SELECT AL.AUTOR_ID, A.NOME FROM AUTOR_LIVRO AS AL "
                    + "INNER JOIN AUTOR AS A ON A.AUTOR_ID = AL.AUTOR_ID "
                    + "WHERE LIVRO_ID = ? ";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, livroId);
            ResultSet rs = p.executeQuery();
            
            while( rs.next()) {
                Autor a = new Autor();
                a.setAutor_id( rs.getInt("autor_id"));
                a.setNome( rs.getString("nome"));
                lista.add(a);
                       
            }
            rs.close();
            p.close();
            
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
            
            
            return lista;
        }
        
      
    //TESTAR SE O FINDALL ESTA FUNCIONANDO  
    public static void main(String[] args) {
        try {
            new LivroDAO().findAll();
        } catch (Exception ex) {
            Logger.getLogger(LivroDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
