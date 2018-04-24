
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Emprestimo;
import model.Estudante;
import model.Livro;
import util.ConnectionJDBC;

public class EmprestimoDAO {
 
    Connection connection;

    public EmprestimoDAO() throws Exception {
        //conexao com  banco
        connection = ConnectionJDBC.getConnection();
    }
    public void save(Emprestimo emprestimo) throws Exception { 
        String SQL = "INSERT INTO EMPRESTIMO (LIVRO_ID, ESTUDANTE_ID, "
                + "DATA_RETIRADA, DATA_DEVOLUCAO, DATA_ENTREGA, STATUS) VALUES (?, ?, ?, ?, ?, ?)";        
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);            
            ps.setInt(1, emprestimo.getLivro().getLivro_id());
            ps.setInt(2, emprestimo.getEstudante().getEstudante_id());
            ps.setDate(3, new java.sql.Date (emprestimo.getData_retirada().getTime()));
            ps.setDate(4, new java.sql.Date (emprestimo.getData_devolucao().getTime()));
            ps.setDate(5, new java.sql.Date (emprestimo.getData_entrega().getTime()));
            ps.setString(6, emprestimo.getStatus());
            ps.execute();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }
    
    public void update(Emprestimo emprestimo) throws Exception { 
        String SQL = "UPDATE EMPRESTIMO SET LIVRO_ID = ?, ESTUDANTE_ID = ?, "
                + "DATA_RETIRADA = ?, DATA_DEVOLUCAO = ?, DATA_ENTREGA = ?, STATUS = ? "
                + "WHERE EMPRESTIMO_ID = ?";
                
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);           
            ps.setInt(1, emprestimo.getLivro().getLivro_id());
            ps.setInt(2, emprestimo.getEstudante().getEstudante_id());
            ps.setDate(3, new java.sql.Date (emprestimo.getData_retirada().getTime()));
            ps.setDate(4, new java.sql.Date (emprestimo.getData_devolucao().getTime()));
            ps.setDate(5, new java.sql.Date (emprestimo.getData_entrega().getTime()));
            ps.setString(6, emprestimo.getStatus());
            ps.setInt(7, emprestimo.getEmprestimo_id());
            ps.execute();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }
    
    public void delete(Emprestimo emprestimo) throws Exception { 
        String SQL = "DELETE FROM EMPRESTIMO WHERE EMPRESTIMO_ID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, emprestimo.getEmprestimo_id());            
            ps.execute();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }
    
    public Emprestimo findById(int id) throws Exception {
        Emprestimo objeto = new Emprestimo();
        String SQL = "SELECT EMPRESTIMO.*, LIVRO.LIVRO_ID, LIVRO.TITULO, ESTUDANTE.ESTUDANTE_ID, ESTUDANTE.NOME "
                + "FROM EMPRESTIMO "
                + "INNER JOIN LIVRO ON (LIVRO.LIVRO_ID = EMPRESTIMO.LIVRO_ID) "
                + "INNER JOIN ESTUDANTE ON (ESTUDANTE.ESTUDANTE_ID = EMPRESTIMO.ESTUDANTE_ID) "
                + "WHERE EMPRESTIMO.EMPRESTIMO_ID = ?";      
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { 
                objeto = new Emprestimo();
                
                objeto.setEmprestimo_id(rs.getInt("emprestimo_id"));
                objeto.setData_retirada(rs.getDate("data_retirada"));
                objeto.setData_devolucao(rs.getDate("data_devolucao"));
                objeto.setData_entrega(rs.getDate("data_entrega"));
                objeto.setStatus(rs.getString("status"));
                
                Livro livro = new Livro();
                livro.setLivro_id(rs.getInt("livro_id"));
                livro.setTitulo(rs.getString("titulo"));
                
                Estudante estudante = new Estudante();
                estudante.setEstudante_id(rs.getInt("estudante_id")); 
                estudante.setNome(rs.getString("nome"));
                
                objeto.setLivro(livro);
                objeto.setEstudante(estudante);              
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }      
        return objeto;
    }
    
    public List<Emprestimo> findAll() throws Exception { 
        List<Emprestimo> list = new ArrayList<>();
        Emprestimo objeto;
        String SQL = "SELECT EMPRESTIMO.*, LIVRO.LIVRO_ID, LIVRO.TITULO, ESTUDANTE.ESTUDANTE_ID, ESTUDANTE.NOME "
                + "FROM EMPRESTIMO "
                + "INNER JOIN LIVRO ON (LIVRO.LIVRO_ID = EMPRESTIMO.LIVRO_ID) "
                + "INNER JOIN ESTUDANTE ON (ESTUDANTE.ESTUDANTE_ID = EMPRESTIMO.ESTUDANTE_ID)";      
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { 
                objeto = new Emprestimo();
                
                objeto.setEmprestimo_id(rs.getInt("emprestimo_id"));
                objeto.setData_retirada(rs.getDate("data_retirada"));
                objeto.setData_devolucao(rs.getDate("data_devolucao"));
                objeto.setData_entrega(rs.getDate("data_entrega"));
                objeto.setStatus(rs.getString("status"));
                
                Livro livro = new Livro();
                livro.setLivro_id(rs.getInt("livro_id"));
                livro.setTitulo(rs.getString("titulo"));
                
                Estudante estudante = new Estudante();
                estudante.setEstudante_id(rs.getInt("estudante_id")); 
                estudante.setNome(rs.getString("nome"));
                   
                objeto.setEstudante(estudante);
                objeto.setLivro(livro);
                
                list.add(objeto);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }      
        return list;
    } 
    
}
